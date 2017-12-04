package com.shakenbeer.nutrition.usda;


import com.shakenbeer.nutrition.model.Food;
import com.shakenbeer.nutrition.model.Nutrient;
import com.shakenbeer.nutrition.model.UsdaDataSource;
import com.shakenbeer.nutrition.model.UsdaFood;
import com.shakenbeer.nutrition.model.UsdaItem;
import com.shakenbeer.nutrition.model.UsdaReportV2;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class UsdaPresenter extends UsdaContract.Presenter {

    private final UsdaService usdaService;
    /*
    You may retrieve up to 50 foods per request
    https://ndb.nal.usda.gov/ndb/doc/apilist/API-FOOD-REPORTV2.md
     */
    private final int max = 50;
    private int offset = 0;
    private boolean isLoading;
    private boolean lastPage;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private String actualQuery;
    private UsdaDataSource actualSource;


    public UsdaPresenter(UsdaService usdaService) {
        this.usdaService = usdaService;
    }

    @Override
    public void detachView() {
        disposables.clear();
        super.detachView();
    }

    @Override
    void searchNewFoods(String query, UsdaDataSource source) {
        if (!isLoading) {
            actualQuery = query;
            actualSource = source;
            offset = 0;
            lastPage = false;
            searchMoreFoods();
        }
    }

    @Override
    void searchMoreFoods() {
        if (!isLoading && !lastPage) {
            disposables.add(
                    obtainFoods(actualQuery, actualSource.getName())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(disposable -> {
                                isLoading = true;
                                getMvpView().showLoading();
                            })
                            .doAfterTerminate(() -> {
                                isLoading = false;
                                getMvpView().hideLoading();
                            })
                            .doOnSuccess(foods -> {
                                if (offset == 0) {
                                    getMvpView().clearFoods();
                                }
                                offset += max;
                                lastPage = (foods.size() < max);
                            })
                            .subscribe(foods -> getMvpView().showFoods(foods),
                                    throwable -> getMvpView().showError(throwable.getMessage()))
            );
        }
    }

    @Override
    void onFoodClick(Food food) {

    }

    private Single<List<Food>> obtainFoods(String query, String source) {
        return usdaService.search(query, source, max, offset)
                .map(usdaSearch -> {
                    if (usdaSearch.getErrors() != null) {
                        throw new Exception(usdaSearch.getErrorMessage());
                    } else {
                        return usdaSearch.getList().getItems();
                    }
                })
                .flatMapObservable(Observable::fromIterable)
                .map(UsdaItem::getNdbno)
                .toList()
                .flatMap(usdaService::report)
                .map(UsdaReportV2::getFoods)
                .flatMapObservable(Observable::fromIterable)
                .map(this::fromUsdaFood)
                .toList();

    }

    private Food fromUsdaFood(UsdaFood usdaFood) {
        Food result = new Food();
        int counter = 0;
        result.setName(usdaFood.getDesc().getName());
        result.setUnit(100);
        result.setUnitName("grams");
        for (Nutrient nutrient : usdaFood.getNutrients()) {
            if (nutrient.isEnergy()) {
                result.setKcalPerUnit(Float.valueOf(nutrient.getValue()));
                counter++;
            } else if (nutrient.isCarbs()) {
                result.setCarbsPerUnit(Float.valueOf(nutrient.getValue()));
                counter++;
            } else if (nutrient.isProtein()) {
                result.setProteinPerUnit(Float.valueOf(nutrient.getValue()));
                counter++;
            } else if (nutrient.isFat()) {
                result.setFatPerUnit(Float.valueOf(nutrient.getValue()));
                counter++;
            }

            if (counter == 4) {
                break;
            }
        }
        return result;
    }
}

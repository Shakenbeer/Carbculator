package com.shakenbeer.nutrition.foodlist;


import com.shakenbeer.nutrition.data.NutritionLab2;
import com.shakenbeer.nutrition.model.Food;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class FoodListPresenter extends FoodListContract.Presenter {

    private static final int OFFSET = 100;
    private final NutritionLab2 nutritionLab2;
    private int page = 0;
    private boolean everythingIsHere = false;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    FoodListPresenter(NutritionLab2 nutritionLab2) {
        this.nutritionLab2 = nutritionLab2;
    }

    @Override
    public void detachView() {
        disposables.clear();
        super.detachView();
    }

    @Override
    void obtainFoods() {
        if (everythingIsHere) return;
        disposables.add(
                nutritionLab2.getFoodsRx(page, OFFSET)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::processFoods, throwable
                                -> getMvpView().showError(throwable.getLocalizedMessage()))
        );
    }

    private void processFoods(List<Food> foods) {
        page++;
        getMvpView().showFoods(foods);
        if (foods.size() < OFFSET) {
            thatsAll();
        }
    }

    private void thatsAll() {
        everythingIsHere = true;
    }

    @Override
    void onFoodClick(Food food) {
        getMvpView().showFoodUi(food);
    }

    @Override
    void onNewFoodClick() {
        getMvpView().showFoodUi(new Food());
    }

    @Override
    void onRemoveFood(int position, Food food) {
        disposables.add(
                nutritionLab2.deleteFoodRx(food)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> getMvpView().removeFood(position, food), throwable ->
                                getMvpView().showError(throwable.getLocalizedMessage()))
        );
    }

    @Override
    void onFoodUpdated(long foodId, List<Food> foods) {
        disposables.add(
                nutritionLab2.getFoodRx(foodId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(food -> processFood(food, foods), throwable
                                -> getMvpView().showError(throwable.getLocalizedMessage()))
        );
    }

    private void processFood(Food food, List<Food> foods) {
        for (int i = 0; i < foods.size(); i++) {
            if (food.getId() == foods.get(i).getId()) {
                getMvpView().showFoodUpdated(food, i);
                return;
            }
        }
        getMvpView().showNewFood(food);
    }

    @Override
    void onNewFood(long foodId) {
        disposables.add(
                nutritionLab2.getFoodRx(foodId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(food -> getMvpView().showNewFood(food), throwable
                                -> getMvpView().showError(throwable.getLocalizedMessage()))
        );
    }
}

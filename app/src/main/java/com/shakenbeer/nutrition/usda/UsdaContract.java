package com.shakenbeer.nutrition.usda;


import com.shakenbeer.nutrition.model.Food;
import com.shakenbeer.nutrition.model.UsdaDataSource;
import com.shakenbeer.nutrition.presentation.BasePresenter;
import com.shakenbeer.nutrition.presentation.MvpView;

import java.util.List;

public interface UsdaContract {

    interface View extends MvpView {
        void showLoading();
        void hideLoading();
        void showFoods(List<Food> foods);
        void showError(String message);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void searchMoreFoods(String query, UsdaDataSource source);
        abstract void onFoodClick(Food food);
    }
}

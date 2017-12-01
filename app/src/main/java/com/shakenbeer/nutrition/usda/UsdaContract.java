package com.shakenbeer.nutrition.usda;


import com.shakenbeer.nutrition.presentation.BasePresenter;
import com.shakenbeer.nutrition.presentation.MvpView;

public interface UsdaContract {

    interface View extends MvpView {

    }

    abstract class Presenter extends BasePresenter<View> {

    }
}

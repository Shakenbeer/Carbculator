package com.shakenbeer.nutrition.usda;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shakenbeer.nutrition.CarbculatorApplication;
import com.shakenbeer.nutrition.R;
import com.shakenbeer.nutrition.databinding.ActivityUsdaBinding;
import com.shakenbeer.nutrition.model.Food;
import com.shakenbeer.nutrition.model.UsdaDataSource;
import com.shakenbeer.nutrition.util.ui.EndlessRecyclerViewScrollListener;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sviatoslav on 12/1/17.
 */

public class UsdaActivity extends AppCompatActivity implements UsdaContract.View {


    @SuppressWarnings("WeakerAccess")
    @Inject
    UsdaContract.Presenter presenter;
    @Inject
    UsdaFoodAdapter adapter;

    private ActivityUsdaBinding binding;
    private AlertDialog loadingDialog;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_usda);
        injectDependencies();
        initUi();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void injectDependencies() {
        DaggerUsdaComponent.builder()
                .applicationComponent(CarbculatorApplication.get(this).getComponent())
                .usdaModule(new UsdaModule())
                .build()
                .inject(this);
    }

    private void initUi() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.commonRadioButton.setChecked(true);
        binding.brandedRadioButton.setChecked(false);
        binding.commonRadioButton.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.brandedRadioButton.setChecked(!isChecked));
        binding.brandedRadioButton.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.commonRadioButton.setChecked(!isChecked));

        binding.button.setOnClickListener(v -> newSearch());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.foodsRecyclerVew.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.searchMoreFoods();
            }
        };
        binding.foodsRecyclerVew.addOnScrollListener(scrollListener);
        binding.foodsRecyclerVew.setAdapter(adapter);
    }

    private void newSearch() {
        scrollListener.resetState();
        String query = binding.queryEditText.getText().toString();
        UsdaDataSource source = binding.commonRadioButton.isChecked() ? UsdaDataSource.STANDARD :
                UsdaDataSource.BRANDED;
        presenter.searchNewFoods(query, source);
    }

    private void initPresenter() {
        presenter.attachView(this);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new AlertDialog.Builder(this, R.style.LoadingDialog)
                    .setCancelable(false)
                    .setView(R.layout.dialog_loading)
                    .create();
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showFoods(List<Food> foods) {
        adapter.addItems(foods);
    }

    @Override
    public void clearFoods() {
        adapter.clear();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

package com.shakenbeer.nutrition.usda;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shakenbeer.nutrition.databinding.ItemFoodBinding;
import com.shakenbeer.nutrition.databinding.ItemUsdaFoodBinding;
import com.shakenbeer.nutrition.model.Food;
import com.shakenbeer.nutrition.util.ui.BindingAdapter;
import com.shakenbeer.nutrition.util.ui.BindingViewHolder;

import javax.inject.Inject;

public class UsdaFoodAdapter extends BindingAdapter<Food> {

    @Inject
    public UsdaFoodAdapter() {
    }

    @Override
    protected ViewDataBinding bind(LayoutInflater inflater, ViewGroup parent, int viewType) {
        ItemUsdaFoodBinding binding = ItemUsdaFoodBinding.inflate(inflater, parent, false);
        return binding;
    }

    @Override
    public void onBindViewHolder(final BindingViewHolder holder, int position) {
        ItemFoodBinding binding = (ItemFoodBinding) holder.binding;
        binding.setFood(items.get(position));
        holder.binding.executePendingBindings();
    }
}

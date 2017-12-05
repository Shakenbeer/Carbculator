package com.shakenbeer.nutrition.meal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shakenbeer.nutrition.CarbculatorApplication;
import com.shakenbeer.nutrition.R;
import com.shakenbeer.nutrition.databinding.ActivityMealBinding;
import com.shakenbeer.nutrition.model.Component;
import com.shakenbeer.nutrition.model.Food;
import com.shakenbeer.nutrition.model.Meal;
import com.shakenbeer.nutrition.usda.UsdaActivity;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class MealActivity extends AppCompatActivity implements MealContract.View {

    private static final String MEAL_EXTRA = "com.shakenbeer.nutrition.meal.mealExtra";
    private static final int USDA_FOODS_REQUEST_CODE = 9404;
    public static final String MEAL_ID_EXTRA = "com.shakenbeer.nutrition.meal.mealIdExtra";

    @SuppressWarnings("WeakerAccess")
    @Inject
    ComponentAdapter adapter;
    @SuppressWarnings("WeakerAccess")
    @Inject
    MealContract.Presenter presenter;
    private ActivityMealBinding binding;

    private final ComponentListener componentListener = new ComponentListener() {
        @Override
        public void onDelete(int position) {
            presenter.onRemoveComponent(adapter.getItem(position), position);
        }

        @Override
        public void onSelectFood(int position) {
            showFoodChooser(position);
        }

        @Override
        public void onAmountChanged(int position, String text) {
            int amount = text.isEmpty() ? 0 : Integer.parseInt(text);
            presenter.onComponentAmountChanged(adapter.getItem(position), amount);
        }
    };

    private final DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    presenter.onMealDateSelected(year, month, dayOfMonth);
                }
            };
    private final TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    presenter.onMealTimeSelected(hourOfDay, minute);
                }
            };

    public static void start(Context context, Meal meal) {
        Intent starter = new Intent(context, MealActivity.class);
        starter.putExtra(MEAL_EXTRA, meal);
        context.startActivity(starter);
    }

    @SuppressWarnings("SameParameterValue")
    public static void startForResult(Activity activity, Meal meal, int requestCode) {
        Intent starter = new Intent(activity, MealActivity.class);
        starter.putExtra(MEAL_EXTRA, meal);
        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_meal);
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
        DaggerMealComponent.builder()
                .applicationComponent(CarbculatorApplication.get(this).getComponent())
                .mealModule(new MealModule(componentListener))
                .build()
                .inject(this);
    }

    private void initUi() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.componentsRecyclerView.setLayoutManager(layoutManager);
        binding.componentsRecyclerView.
                addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        binding.componentsRecyclerView.setAdapter(adapter);
        binding.addComponentButton.setOnClickListener(v -> presenter.onAddComponent());
        binding.saveEatingButton.setOnClickListener(v -> presenter.onSaveClick());
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.eating_names,
                R.layout.spinner_item_eating);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_eating);
        binding.eatingNameSpinner.setAdapter(arrayAdapter);
        binding.eatingNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onMealTypeSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.eatingDateButton.setOnClickListener(v -> showDateDialog());
        binding.eatingTimeButton.setOnClickListener(v -> showTimeDialog());
    }

    private void showDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(binding.getMeal().getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        dialog.show();
    }

    private void showTimeDialog() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(binding.getMeal().getDate());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, hourOfDay, minute, true);
        dialog.show();
    }

    private void initPresenter() {
        presenter.attachView(this);
        Meal meal = getIntent().getParcelableExtra(MEAL_EXTRA);
        presenter.obtainComponents(meal);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            presenter.onAddComponent();
            return true;
        }
        if (item.getItemId() == R.id.save) {
            presenter.onSaveClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFoodChooser(final int position) {
        FoodChooserDialog.newInstance(new FoodChooserDialog.Callbacks() {
            @Override
            public void onItemSelected(Food food) {
                presenter.onComponentFoodSelected(position, adapter.getItem(position), food);
            }

            @Override
            public void onFindOnlineSelected(String query) {
                presenter.onOnlineSearch(position, adapter.getItem(position));
                UsdaActivity.startForResult(MealActivity.this, USDA_FOODS_REQUEST_CODE);
            }
        }).show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USDA_FOODS_REQUEST_CODE && resultCode == RESULT_OK) {
            long foodId = data.getLongExtra(UsdaActivity.FOOD_ID_EXTRA, 0);
            if (foodId > 0) {
                presenter.onOnlineFound(foodId);
            }
        }
    }

    @Override
    public void showMeal(Meal meal) {
        binding.setMeal(meal);
        binding.eatingNameSpinner.setSelection(meal.getNumber());
    }

    @Override
    public void showComponents(List<Component> components) {
        adapter.setItems(components);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showNewComponent(Component component) {
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        binding.componentsRecyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void removeComponent(Component component, int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPreviousUi(long mealId, boolean changed) {
        if (changed) {
            Intent data = new Intent();
            data.putExtra(MEAL_ID_EXTRA, mealId);
            setResult(RESULT_OK, data);
        }
        onBackPressed();
    }

    @Override
    public void updateComponent(int position) {
        adapter.notifyItemChanged(position);
    }

}

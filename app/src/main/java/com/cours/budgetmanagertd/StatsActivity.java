package com.cours.budgetmanagertd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.cours.budgetmanagertd.datas.History;
import com.cours.budgetmanagertd.datas.HistoryViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;

    private List<PieEntry> categories_entries;
    private List<BarEntry> month_evolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        categories_entries = new ArrayList<>();
        month_evolution = new ArrayList<>();

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);

        final HistoryViewModel viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        final LifecycleOwner activity = this;
        categoryViewModel.getAll().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(final List<Category> categories) {
                for (int i = 0; i < categories.size(); i += 1) {
                    final Category category = categories.get(i);
                    final int index = i;
                    viewModel.getCountByCategory(category.getId()).observe(activity, new Observer<Float>() {
                        @Override
                        public void onChanged(Float count) {
                            if (count > 0) {
                                categories_entries.add(new PieEntry(count, category.getName()));
                            }
                            updatePieChart();
                        }
                    });

                }
            }
        });

        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endDate = Calendar.getInstance();
        for (int i = 0; i < 12; i += 1) {
            final int index = i;
            startDate.set(Calendar.MONTH, index);
            endDate.set(Calendar.MONTH, index);
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            viewModel.getSumIncomeBetweenDate(startDate.getTime(), endDate.getTime()).observe(this, new Observer<Float>() {
                @Override
                public void onChanged(final Float inFloat) {
                    Calendar startDate = Calendar.getInstance();
                    startDate.set(Calendar.DAY_OF_MONTH, 1);
                    Calendar endDate = Calendar.getInstance();
                    startDate.set(Calendar.MONTH, index);
                    endDate.set(Calendar.MONTH, index);
                    endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                    viewModel.getSumOutcomeBetweenDate(startDate.getTime(), endDate.getTime()).observe(activity, new Observer<Float>() {
                        @Override
                        public void onChanged(Float outFloat) {
                            float value = inFloat != null ? inFloat : 0;
                            value -= outFloat != null ? outFloat : 0;
                            month_evolution.add(new BarEntry(index, value));
                            updateBarChart();
                        }
                    });
                }
            });
        }

    }

    private void updatePieChart() {
        PieDataSet set = new PieDataSet(categories_entries, "Categories");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(new PieData(set));
        pieChart.invalidate();
    }

    private void updateBarChart() {
        BarDataSet set = new BarDataSet(month_evolution, "Evolution");
        barChart.setData(new BarData(set));
        barChart.invalidate();
    }
}

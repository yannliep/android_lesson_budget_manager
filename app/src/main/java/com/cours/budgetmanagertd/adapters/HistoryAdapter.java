package com.cours.budgetmanagertd.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cours.budgetmanagertd.R;
import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.cours.budgetmanagertd.datas.History;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class HistoryAdapter extends ArrayAdapter {
    private CategoryViewModel viewModel;

    public HistoryAdapter(@NonNull Context context, List<History> histories, CategoryViewModel viewModel) {
        super(context, 0, histories);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        History history = (History) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_adapter_item, parent, false);
        }

        final TextView name = convertView.findViewById(R.id.name);
        final TextView value = convertView.findViewById(R.id.value);
        final TextView date = convertView.findViewById(R.id.date);

        if (history != null) {
            name.setText(history.getName());
            value.setText(String.valueOf(history.getValue()));
            if (history.getDate() != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                date.setText(format.format(history.getDate()));
            }
            final View finalConvertView = convertView;
            viewModel.getById(history.getCategoryId()).observe((FragmentActivity) getContext(), new Observer<Category>() {
                @Override
                public void onChanged(Category category) {
                    if (category != null) {
                        if (category.isIncome()) {
                            value.setTextColor(finalConvertView.getResources().
                                    getColor(android.R.color.holo_green_light));
                        } else {
                            value.setTextColor(finalConvertView.getResources().
                                    getColor(android.R.color.holo_red_light));
                        }
                    }
                }
            });
        }

        return convertView;
    }
}

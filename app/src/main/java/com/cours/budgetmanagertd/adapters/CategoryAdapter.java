package com.cours.budgetmanagertd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cours.budgetmanagertd.R;
import com.cours.budgetmanagertd.datas.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryAdapter extends ArrayAdapter {

    public CategoryAdapter(@NonNull Context context, List<Category> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = (Category) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_adapter_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);

        if (category != null) {
            name.setText(category.getName());
            if (category.isIncome()) {
                name.setTextColor(convertView.getResources().
                        getColor(android.R.color.holo_green_light));
            } else {
                name.setTextColor(convertView.getResources().
                        getColor(android.R.color.holo_red_light));
            }
        }

        return convertView;
    }
}

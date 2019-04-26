package com.cours.budgetmanagertd;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cours.budgetmanagertd.adapters.CategoryAdapter;
import com.cours.budgetmanagertd.datas.Category;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {

    private ListView categoriesListView;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        final Activity activity = getActivity();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CategoryEditActivity.class);
                activity.startActivity(intent);
            }
        });

        categoriesListView = view.findViewById(R.id.categories);

        updateList();

        return view;
    }

    private void updateList() {
        CategoryViewModel viewModel = ViewModelProviders.of(this)
                .get(CategoryViewModel.class);

        viewModel.getAll().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
                categoriesListView.setAdapter(adapter);
            }
        });
    }

    private ListView.OnClickListener onClickListener = new ListView.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

}

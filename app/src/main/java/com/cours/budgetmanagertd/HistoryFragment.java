package com.cours.budgetmanagertd;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cours.budgetmanagertd.adapters.HistoryAdapter;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.cours.budgetmanagertd.datas.History;
import com.cours.budgetmanagertd.datas.HistoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ListView historyListView;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        final Activity activity = getActivity();
        FloatingActionButton floatingActionButton = view.findViewById(R.id.edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HistoryEditActivity.class);
                activity.startActivity(intent);
            }
        });

        //On récupère la liste et on ajoute la gestion du clic
        historyListView = view.findViewById(R.id.history);
        historyListView.setOnItemClickListener(onItemClickListener);

        //On met à jour la liste
        updateList();

        return view;
    }

    private void updateList() {
        HistoryViewModel viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        viewModel.getAll().observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                CategoryViewModel categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
                HistoryAdapter adapter = new HistoryAdapter(getContext(), histories, categoryViewModel);
                historyListView.setAdapter(adapter);
            }
        });
    }

    private ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), HistoryEditActivity.class);
            //On récupère l'entrée sélectionnée
            History history = (History) parent.getItemAtPosition(position);
            intent.putExtra(Intent.EXTRA_UID, history.getId());
            startActivity(intent);
        }
    };

}

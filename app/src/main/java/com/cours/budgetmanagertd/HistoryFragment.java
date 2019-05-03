package com.cours.budgetmanagertd;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cours.budgetmanagertd.adapters.HistoryAdapter;
import com.cours.budgetmanagertd.datas.CategoryViewModel;
import com.cours.budgetmanagertd.datas.History;
import com.cours.budgetmanagertd.datas.HistoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ListView historyListView;

    private HistoryViewModel viewModel;

    private TextView totalTextView;

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
        historyListView.setOnItemLongClickListener(onItemLongClickListener);

        totalTextView = view.findViewById(R.id.total);

        Calendar calendar = Calendar.getInstance();
        //On met à jour la liste
        updateList(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));

        return view;
    }

    public void updateList(int year, int month) {
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        final Calendar starDate = Calendar.getInstance();
        starDate.set(year, month, 1);
        final Calendar endDate = Calendar.getInstance();
        endDate.set(year, month, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        viewModel.getAllBetweenDate(starDate.getTime(), endDate.getTime()).observe(this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                CategoryViewModel categoryViewModel = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
                HistoryAdapter adapter = new HistoryAdapter(getContext(), histories, categoryViewModel);
                historyListView.setAdapter(adapter);
            }
        });
        final LifecycleOwner owner = this;
        viewModel.getSumIncomeBetweenDate(starDate.getTime(), endDate.getTime()).observe(owner, new Observer<Float>() {
            @Override
            public void onChanged(final Float inFloat) {
                viewModel.getSumOutcomeBetweenDate(starDate.getTime(), endDate.getTime()).observe(owner, new Observer<Float>() {
                    @Override
                    public void onChanged(Float outFloat) {
                        if (inFloat != null && outFloat != null) {
                            totalTextView.setText(Float.toString(inFloat - outFloat));
                        }
                    }
                });
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

    private ListView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final AdapterView<?> finalParent = parent;
            final int finalPosition = position;

            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.delete_title)
                    .setMessage(R.string.delete_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            History history = (History) finalParent.getItemAtPosition(finalPosition);
                            viewModel.delete(history);
                        }
                    }).setNegativeButton(R.string.no, null).show();
            return true;
        }
    };

}

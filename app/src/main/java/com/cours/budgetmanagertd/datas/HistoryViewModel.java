package com.cours.budgetmanagertd.datas;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryDAO historyDAO;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        historyDAO = Database.getInstance(application.getApplicationContext()).historyDAO();
    }

    public LiveData<List<History>> getAll() {
        return historyDAO.getAll();
    }

    public LiveData<List<History>> getAllBetweenDate(Date startDate, Date endDate) {
        return historyDAO.getAllBetweenDate(startDate, endDate);
    }

    public LiveData<Float> getSumIncomeBetweenDate(Date startDate, Date endDate) {
        return historyDAO.getSumIncomeBetweenDate(startDate, endDate);
    }

    public LiveData<Float> getSumOutcomeBetweenDate(Date startDate, Date endDate) {
        return historyDAO.getSumOutcomeBetweenDate(startDate, endDate);
    }

    public LiveData<History> getById(int id) {
        return historyDAO.getById(id);
    }

    public LiveData<Float> getCountByCategory(int cat_id) {
        return  historyDAO.getCountByCategory(cat_id);
    }

    public void insertOrUpdate(History history) {
        new InsertOrUpdateTask(historyDAO).execute(history);
    }

    public void delete(History history) {
        new DeleteTask(historyDAO).execute(history);
    }

    private static class InsertOrUpdateTask extends
            AsyncTask<History, Void, Void> {
        private HistoryDAO historyDAO;

        public InsertOrUpdateTask(HistoryDAO historyDAO) {
            this.historyDAO = historyDAO;
        }

        @Override
        protected Void doInBackground(History... histories) {
            History history = histories[0];
            if (history.getId() > 0) {
                historyDAO.update(history);
            } else {
                historyDAO.insert(history);
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<History, Void, Void> {
        private HistoryDAO historyDAO;

        public DeleteTask(HistoryDAO historyDAO) {
            this.historyDAO = historyDAO;
        }

        @Override
        protected Void doInBackground(History... histories) {
            History history = histories[0];
            historyDAO.delete(history);
            return null;
        }
    }
}

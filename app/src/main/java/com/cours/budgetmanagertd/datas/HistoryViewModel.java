package com.cours.budgetmanagertd.datas;

import android.app.Application;
import android.os.AsyncTask;

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

    public LiveData<History> getById(int id) {
        return historyDAO.getById(id);
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

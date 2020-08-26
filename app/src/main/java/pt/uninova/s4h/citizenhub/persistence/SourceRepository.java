package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SourceRepository {
    private SourceDao sourceDAO;

    public SourceRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);
        sourceDAO = citizenHubDatabase.sourceDao();
    }

    public void insert(Source source) {
        new InsertSourceAsyncTask(sourceDAO).execute(source);
    }

    public void update(Source source) {
        new UpdateSourceAsyncTask(sourceDAO).execute(source);
    }

    public void delete(Source source) {
        new DeleteSourceAsyncTask(sourceDAO).execute(source);

    }

    public void deleteAll() {
        new DeleteAllSourcesAsyncTask(sourceDAO).execute();

    }

    public LiveData<List<Source>> getAllSourcesLive() {
        return sourceDAO.getSources();

    }

    private static class InsertSourceAsyncTask extends AsyncTask<Source, Void, Void> {
        private SourceDao sourceDAO;

        private InsertSourceAsyncTask(SourceDao sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.addSource(sources[0]);
            return null;
        }
    }

    private static class UpdateSourceAsyncTask extends AsyncTask<Source, Void, Void> {
        private SourceDao sourceDAO;

        private UpdateSourceAsyncTask(SourceDao sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.updateSource(sources[0]);
            return null;
        }
    }

    private static class DeleteSourceAsyncTask extends AsyncTask<Source, Void, Void> {
        private SourceDao sourceDAO;

        private DeleteSourceAsyncTask(SourceDao sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.deleteSource(sources[0]);
            return null;
        }
    }

    private static class DeleteAllSourcesAsyncTask extends AsyncTask<Void, Void, Void> {
        private SourceDao sourceDAO;

        private DeleteAllSourcesAsyncTask(SourceDao sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sourceDAO.deleteAllSources();
            return null;
        }


    }

}

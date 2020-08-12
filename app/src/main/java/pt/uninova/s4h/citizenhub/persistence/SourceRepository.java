package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SourceRepository {
    private SourceDAO sourceDAO;
    private LiveData<List<Source>> allSources;

    public SourceRepository(Application application) {
        CitizenDatabaseClient citizenDbClient = CitizenDatabaseClient.getInstance(application);
        sourceDAO = citizenDbClient.getDatabase().sourceDAO();
        allSources = sourceDAO.getSources();
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

    public LiveData<List<Source>> getAllSources() {
        return allSources;
    }

    private static class InsertSourceAsyncTask extends AsyncTask<Source, Void, Void> {
        private SourceDAO sourceDAO;

        private InsertSourceAsyncTask(SourceDAO sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.addSource(sources[0]);
            return null;
        }
    }

    private static class UpdateSourceAsyncTask extends AsyncTask<Source, Void, Void> {
        private SourceDAO sourceDAO;

        private UpdateSourceAsyncTask(SourceDAO sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.updateSource(sources[0]);
            return null;
        }
    }

    private static class DeleteSourceAsyncTask extends AsyncTask<Source, Void, Void> {
        private SourceDAO sourceDAO;

        private DeleteSourceAsyncTask(SourceDAO sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Source... sources) {
            sourceDAO.deleteSource(sources[0]);
            return null;
        }
    }

    private static class DeleteAllSourcesAsyncTask extends AsyncTask<Void, Void, Void> {
        private SourceDAO sourceDAO;

        private DeleteAllSourcesAsyncTask(SourceDAO sourceDAO) {
            this.sourceDAO = sourceDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sourceDAO.deleteAllSources();
            return null;
        }


    }

}

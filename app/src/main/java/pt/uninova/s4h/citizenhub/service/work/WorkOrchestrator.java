package pt.uninova.s4h.citizenhub.service.work;

import android.content.Context;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class WorkOrchestrator {

    private final WorkManager workManager;

    public WorkOrchestrator(WorkManager workManager) {
        this.workManager = workManager;
        workManager.cancelAllWork();
    }

    public void addPeriodicWork(Class<? extends ListenableWorker> worker, int time, TimeUnit timeUnit) {
        final PeriodicWorkRequest.Builder periodicWorkBuilder = new PeriodicWorkRequest.Builder(worker,
                time,
                timeUnit);

        final PeriodicWorkRequest periodicWork = periodicWorkBuilder.build();

        workManager.enqueueUniquePeriodicWork("smartbearuploader",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork);
    }

    public void cancelWork(String workName) {
        WorkManager.getInstance().cancelAllWorkByTag(workName);
    }

    public WorkInfo.State getWorkers(@NonNull Context context, String workName) throws ExecutionException, InterruptedException {
        //to check if work is running:
        //there always only one, if it's running, we should get a state
        if (WorkManager.getInstance(context).getWorkInfosForUniqueWork(workName)
                .get().size() < 1) {
            System.out.println("No instances running.");
            //work is not running -> is WorkInfo.State.CANCELLED or WorkInfo.State.FAILED or WorkInfo.State.BLOCKED
            return WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWork(workName).get().get(0).getState();
        } else {
            System.out.println(WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWork(workName).get().get(0).getState());
            return WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWork(workName).get().get(0).getState();
            //returns WorkInfo.State.ENQUEUED if scheduled, WorkInfo.State.RUNNING if running
        }
    }
}
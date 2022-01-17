package pt.uninova.s4h.citizenhub.service.work;

import android.content.Context;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class WorkOrchestrator {

    public static String WORK_RESULT = "work_result";

    public void addPeriodicWork(@NonNull Context context, int time, TimeUnit timeUnit, String workName){
        WorkManager reportWorkManager = WorkManager.getInstance(context);

        //request
        PeriodicWorkRequest.Builder periodicWorkBuilder =
                new PeriodicWorkRequest.Builder(CitizenHubWorker.class,
                        time,
                        timeUnit); // minimum time period is 15 minutes
        PeriodicWorkRequest periodicWork = periodicWorkBuilder.build();
        // the enqueueUniquePeriodicWork() ensures only one active request with this name

        //enqueue
        reportWorkManager.enqueueUniquePeriodicWork(workName,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWork);
    }

    public void addOneTimeWork(@NonNull Context context){
        WorkManager reportWorkManager = WorkManager.getInstance(context);
        OneTimeWorkRequest requestWorkManager =
                new OneTimeWorkRequest.Builder(CitizenHubWorker.class).build();
        reportWorkManager.enqueue(requestWorkManager);
    }

    public void cancelWork(String workName){
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
        }
        else {
            System.out.println(WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWork(workName).get().get(0).getState());
            return WorkManager.getInstance(context)
                    .getWorkInfosForUniqueWork(workName).get().get(0).getState();
            //returns WorkInfo.State.ENQUEUED if scheduled, WorkInfo.State.RUNNING if running
        }
    }
}
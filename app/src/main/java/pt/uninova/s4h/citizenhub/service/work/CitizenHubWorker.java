package pt.uninova.s4h.citizenhub.service.work;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CitizenHubWorker extends Worker {

    public CitizenHubWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //work
        /*
         * DO WORK HERE!
         */

        //log for testing purposes. TODO erase this when fully implemented
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date currentTime = new Date(System.currentTimeMillis());
        System.out.println(getClass().getSimpleName()
                + " has done some work @ "
                + formatter.format(currentTime) + ".");

        //outputData
        Data outputData = new Data.Builder().putString(WorkOrchestrator.WORK_RESULT, "Work is Finished").build();
        return Result.success(outputData);
    }
}

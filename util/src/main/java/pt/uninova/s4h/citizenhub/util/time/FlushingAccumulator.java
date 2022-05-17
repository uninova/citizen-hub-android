package pt.uninova.s4h.citizenhub.util.time;

import android.os.Handler;
import android.os.Looper;

import java.time.Instant;

public class FlushingAccumulator<T> extends Accumulator<T> {

    private Thread runningThread;
    private long interval;

    public FlushingAccumulator(long interval) {
        this.interval = interval;
    }

    @Override
    public void set(T value, Instant timestamp) {
        if (this.runningThread == null) {
            startThread();
        }

        super.set(value, timestamp);
    }

    private void startThread() {
        runningThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Handler handler = new Handler(Looper.myLooper());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning()) {
                            flush();
                            handler.postDelayed(this, interval);
                        } else {
                            runningThread.interrupt();
                            runningThread = null;
                        }
                    }
                }, interval);

                Looper.loop();
            }
        };

        runningThread.start();
    }
}

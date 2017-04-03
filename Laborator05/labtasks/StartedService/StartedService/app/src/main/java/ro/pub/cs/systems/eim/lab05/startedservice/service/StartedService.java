package ro.pub.cs.systems.eim.lab05.startedservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ro.pub.cs.systems.eim.lab05.startedservice.general.Constants;


public class StartedService extends Service {

    ProcessingThread processingThread;
    public static boolean isRunning;

    @Override
    public void onCreate() {
        Log.d(Constants.TAG, "Super onCreate() method was invoked");
        super.onCreate();
        Log.d(Constants.TAG, "onCreate() method was invoked");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.TAG, "onBind() method was invoked");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(Constants.TAG, "onUnbind() method was invoked");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(Constants.TAG, "onRebind() method was invoked");
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.TAG, "onDestroy() method was invoked");
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constants.TAG, "onStartCommand() method was invoked");
        // TODO: exercise 5 - implement and start the ProcessingThread
        isRunning = true;
        processingThread = new ProcessingThread(this);
        processingThread.start();
        return START_REDELIVER_INTENT;
    }

}

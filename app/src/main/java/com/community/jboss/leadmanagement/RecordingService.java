package com.community.jboss.leadmanagement;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class RecordingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    void startRecording() {
        //TODO: recording
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

}

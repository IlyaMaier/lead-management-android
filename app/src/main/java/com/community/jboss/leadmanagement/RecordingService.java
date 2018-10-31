package com.community.jboss.leadmanagement;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.aykuttasil.callrecord.CallRecord;

import java.util.Calendar;

public class RecordingService extends Service {

    static CallRecord callRecord;

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

    @Override
    public void onDestroy() {
        callRecord.stopCallReceiver();
        Toast.makeText(this, "Recording ended", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    void startRecording() {
        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("RecordFileName")
                .setRecordDirName("RecordDirName")
                .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
                .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // optional & default value
                .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB) // optional & default value
                .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION) // optional & default value
                .setShowSeed(true) // optional & default value ->Ex: RecordFileName_incoming.amr || RecordFileName_outgoing.amr
                .build();

        callRecord.startCallRecordService();
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
    }

}

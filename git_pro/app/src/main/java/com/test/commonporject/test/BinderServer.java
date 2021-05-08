package com.test.commonporject.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BinderServer extends Service {
    private static final String TAG = "BinderServer";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mRemote;
    }

    private final TestStub mRemote = new TestStub() {
        @Override
        public void test(String content) {
            Log.i(TAG, "test: " + content);
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: ");
    }
}

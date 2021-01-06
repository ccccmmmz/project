package com.test.commonporject.test;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class TestStub extends Binder implements IServer {

    private static final String TAG = "TestStub";

    public static final String DESCRIPTOR = "test";

    public TestStub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IServer asInterface(IBinder obj) {
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (iin instanceof IServer) {
            return (IServer) iin;
        }
        return new Proxy(obj);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) {
        switch (code) {
            case 165:
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                this.test(_arg0);
                reply.writeInt(100);
                return true;

        }
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i(TAG, "onTransact: ");
            return false;
        }
    }

}

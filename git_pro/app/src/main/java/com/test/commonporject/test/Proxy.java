package com.test.commonporject.test;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class Proxy implements IServer {
    private static final String TAG = "Proxy";
    private IBinder mRemote;

    public Proxy(IBinder remote) {
        mRemote = remote;
    }

    @Override
    public void test(String content) {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(TestStub.DESCRIPTOR);
            _data.writeString(content);
            mRemote.transact(165, _data, _reply, 0);
            int i = _reply.readInt();
            Log.i(TAG, "test: 回复 = " + i);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            _data.recycle();
            _reply.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}

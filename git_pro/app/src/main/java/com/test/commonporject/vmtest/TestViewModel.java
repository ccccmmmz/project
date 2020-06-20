package com.test.commonporject.vmtest;


import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class TestViewModel extends ViewModel {

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> editContent = new ObservableField<>();


    public TestViewModel() {
        super();
        name.set("初始化");
        //name.setValue("初始化");
        Log.i("ligen", "TestViewModel: 初始化");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void test() {
        name.set("初始化1");
        Log.i("ligen", "test: 相同的值");
    }
}

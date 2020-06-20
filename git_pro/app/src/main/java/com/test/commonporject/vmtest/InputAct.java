package com.test.commonporject.vmtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import com.example.common.mvvm_component.BaseVmActivity;
import com.example.common.mvvm_component.DataBindingConfig;
import com.test.commonporject.R;
import com.test.commonporject.databinding.ActivityInputBinding;

public class InputAct extends BaseVmActivity<TestViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_input);
        viewDataBinding.setLifecycleOwner(this);
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return null;
    }
}
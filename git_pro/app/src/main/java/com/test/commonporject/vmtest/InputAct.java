package com.test.commonporject.vmtest;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.test.commonporject.R;

import project.common.mvvm_component.BaseVmActivity;
import project.common.mvvm_component.DataBindingConfig;
import project.common.softinput.SoftProvider;
import project.common.system.SystemHelper;

public class InputAct extends BaseVmActivity<TestViewModel> {


    private ViewDataBinding mViewDataBinding;

    private boolean mSoftShow = false;

    private View mEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_input);
        mViewDataBinding.setLifecycleOwner(this);
        mEditView = mViewDataBinding.getRoot().findViewById(R.id.edit);

        SoftProvider softProvider = new SoftProvider(this, mViewDataBinding.getRoot());
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.activity_input);
    }

    public void screen(View view) {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void showsoft(View view) {
        mSoftShow = !mSoftShow;
        if (mSoftShow) {
            mEditView.setVisibility(View.VISIBLE);
            mEditView.requestFocus();
            SystemHelper.showSoftInput(mEditView);
        } else {
            mEditView.setVisibility(View.GONE);
            SystemHelper.hideSoftInput(mEditView);
        }

    }
}
package com.test.commonporject.vmtest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;

import com.example.common.mvvm_component.BaseVmActivity;
import com.example.common.mvvm_component.ClickProxy;
import com.example.common.mvvm_component.DataBindingConfig;
import com.test.commonporject.BR;
import com.test.commonporject.R;
import com.test.commonporject.test.ApiService;
import com.test.commonporject.test.InfoBean;

import http.ApiDisposableObserver;
import http.BaseResponse;
import http.ResponseThrowable;
import util.ApiKit;
import util.Utils;


public class ViewModelAct extends BaseVmActivity<TestViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        mVM.name.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.i("ligen", "onPropertyChanged: 修改了 ");
            }
        });
        mVM.test();

        ApiKit.getInstance().create(ApiService.class).test().observe(this, new Observer<BaseResponse<InfoBean>>() {
            @Override
            public void onChanged(BaseResponse<InfoBean> infoBeanBaseResponse) {
                System.out.println("成功1");
            }
        });

        ApiKit.execute(ApiKit.getInstance().create(ApiService.class).test1(), new ApiDisposableObserver<Object>() {
            @Override
            public void onResult(Object o) {
                System.out.println("成功2");
            }

            @Override
            public void onError(ResponseThrowable responseThrowable) {
                System.out.println("失败");
            }
        });

    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.act_vm).addBindingParam(BR.vm, mVM).addBindingParam(BR.clickEvent, new ClickProxy() {
            @Override
            public void onFilterClick(View view) {
                Log.i("ligen", "onFilterClick: 点击了" + mVM.editContent.get());
            }
        });
    }
}

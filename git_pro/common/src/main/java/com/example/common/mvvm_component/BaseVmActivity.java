package com.example.common.mvvm_component;

import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <VM> DB自动成功的DatabingImpl是根据布局文件生成的
 */
public abstract class BaseVmActivity<VM extends ViewModel> extends AppCompatActivity {

    protected VM mVM;
    private ViewModelProvider mActivityProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

    }


    protected abstract DataBindingConfig getDataBindingConfig();

    //beforeOncreate的操作
    @SuppressWarnings("unused")
    protected void beforeOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @SuppressWarnings("all")
    private void init(Bundle savedInstanceState) {
        initViewModel();
        DataBindingConfig dataBindingConfig = getDataBindingConfig();
        if (dataBindingConfig == null) {
            throw new IllegalStateException("you must call getDataBindingConfig in your subclass");
        }
        ViewDataBinding binding = DataBindingUtil.setContentView(this, dataBindingConfig.getLayout());

        SparseArray bindingParams = dataBindingConfig.getBindingParams();
        for (int i = 0; i < bindingParams.size(); i++) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
        }

        binding.setLifecycleOwner(this);

        binding.unbind();

    }


    //通过type初始化对应vm
    @SuppressWarnings("all")
    private void initViewModel() {
        ParameterizedType paraType = getParaType(this.getClass());
        if (paraType == null) {
            return;
        }
        Type[] actualTypeArguments = paraType.getActualTypeArguments();
        if (actualTypeArguments.length > 0) {
            try {
                Type actualTypeArgument = actualTypeArguments[0];
                mVM = (VM) getActivityViewModel(((Class) actualTypeArgument));
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
    }

    @SuppressWarnings("all")
    private ParameterizedType getParaType(@NonNull Class cls) {
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        }

        Class superclass = cls.getSuperclass();
        if (superclass != null) {
            return null;
        }
        return getParaType(superclass);
    }

    protected <T extends ViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication()));
        }
        return mActivityProvider.get(modelClass);
    }
}

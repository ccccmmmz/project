package project.common.handler;

import com.test.commonporject.R;

import project.common.mvvm_component.BaseVmActivity;
import project.common.mvvm_component.DataBindingConfig;

public class HandlerActivity extends BaseVmActivity<HandlerViewModel> {
    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return new DataBindingConfig(R.layout.act_handler);
    }
}

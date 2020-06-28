/*
 * Copyright 2018-2020 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package project.common.mvvm_component;

import android.util.SparseArray;

import androidx.lifecycle.ViewModel;

/**
 * 不在子类中暴露DataBinding的中间件
 */
public class DataBindingConfig {

    private int layout;

    //private ViewModel stateViewModel;

    private SparseArray bindingParams = new SparseArray();

    private DataBindingConfig(int layout, ViewModel stateViewModel) {
        this.layout = layout;
        //this.stateViewModel = stateViewModel;
    }

    public DataBindingConfig(int layout) {
        //this.layout = layout;
        this(layout, null);
    }

    public int getLayout() {
        return layout;
    }

    //public ViewModel getStateViewModel() {
    //    return stateViewModel;
    //}

    public SparseArray getBindingParams() {
        return bindingParams;
    }

    public DataBindingConfig addBindingParam(int variableId, Object object) {
        if (bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, object);
        }
        return this;
    }

    public void recycler() {

    }
}

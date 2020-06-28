package project.common.http.http;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {
    private static final String TAG = LiveDataCallAdapter.class.getSimpleName();
    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<T> adapt(final Call<T> call) {
        return new LiveData<T>() {
            AtomicBoolean start = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (start.compareAndSet(false, true)) {
                    call.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(Call<T> call, Response<T> response) {
                            boolean success = response.code() == 200;
                            BaseResponse apiResponse = new BaseResponse(response.code(), response.message(), success ? response.body() : response.errorBody());
                            postValue(success ? response.body() : (T) apiResponse);

                        }

                        @Override
                        public void onFailure(Call<T> call, Throwable t) {
                            Log.e(TAG, t.getMessage());

                            BaseResponse apiResponse = new BaseResponse(-1, t.getMessage(), null);
                            postValue((T) apiResponse);
                        }
                    });
                }
            }
        };
    }


}

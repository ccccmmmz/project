package com.test.commonporject.test;

import androidx.lifecycle.LiveData;

import http.BaseResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    //@GET("banner/json")
    //LiveData<BaseResponse<List<BannerModel>>> test();

    @GET("api/base/my")
    LiveData<BaseResponse<InfoBean>> test();

    @GET("api/base/my")
    Observable<Object> test1();
}

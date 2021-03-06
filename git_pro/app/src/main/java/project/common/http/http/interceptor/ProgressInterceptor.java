package project.common.http.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import project.common.http.http.download.ProgressResponseBody;

/**
 * Created by goldze on 2017/5/10.
 */

public class ProgressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body()))
                .build();
    }
}

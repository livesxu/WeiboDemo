package com.example.weibodemo.WeiboHome;

import android.util.Log;

import com.example.weibodemo.WeiboLogin.WeiboAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainModel {

    interface RequestResult {

        void requestSuccess(WeiboContent content);

        void requestFail();
    }

    void requestList(RequestResult requestResult) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.weibo.com/").build();
        MainListRequest_Interface mainListRequest_interface = retrofit.create(MainListRequest_Interface.class);
        Call<WeiboContent> call = mainListRequest_interface.getList("2.00ZJoCFCadEPxBdf6e733c1aU7mwCD");//WeiboAccount.getInstance().getAccess_token()
        call.enqueue(new Callback<WeiboContent>() {
            @Override
            public void onResponse(Call<WeiboContent> call, Response<WeiboContent> response) {

                Log.v("weibo",response.toString());
                requestResult.requestSuccess(response.body());
            }

            @Override
            public void onFailure(Call<WeiboContent> call, Throwable t) {

                Log.v("weibo",call.request().toString());
                requestResult.requestFail();
            }
        });
    }
}

interface MainListRequest_Interface {

    @GET("2/statuses/home_timeline.json")
    Call<WeiboContent> getList(@Query("access_token") String token);
}

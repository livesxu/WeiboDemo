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

    String accessToken;

    interface RequestResult {

        void requestSuccess(WeiboContent content,int page);

        void requestFail();
    }

    interface RequestUserResult {

        void requestUserSuccess(WeiboContent.StatusesBean.UserBean user);

        void requestUserFail();
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;

    }

    void requestList(RequestResult requestResult,int page) {

        if (accessToken == null || accessToken.length() == 0) {

            return;
        }

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.weibo.com/").build();
        MainListRequest_Interface mainListRequest_interface = retrofit.create(MainListRequest_Interface.class);
        Call<WeiboContent> call = mainListRequest_interface.getList(accessToken,"60",String.format("%d",page));//WeiboAccount.getInstance().getAccess_token()
        call.enqueue(new Callback<WeiboContent>() {
            @Override
            public void onResponse(Call<WeiboContent> call, Response<WeiboContent> response) {

                Log.v("weibo",response.toString());
                requestResult.requestSuccess(response.body(),page);
            }

            @Override
            public void onFailure(Call<WeiboContent> call, Throwable t) {

                Log.v("weibo",call.request().toString());
                requestResult.requestFail();
            }
        });
    }

    void requestMainUser (RequestUserResult requestResult) {

        if (WeiboAccount.getInstance().getAccess_token() == null || WeiboAccount.getInstance().getAccess_token().length() == 0) {

            return;
        }

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.weibo.com/").build();
        MainUserRequest_Interface mainUserRequest_interface = retrofit.create(MainUserRequest_Interface.class);
        Call<WeiboContent.StatusesBean.UserBean> call = mainUserRequest_interface.getUser(WeiboAccount.getInstance().getAccess_token(),WeiboAccount.getInstance().getUid());//
        call.enqueue(new Callback<WeiboContent.StatusesBean.UserBean>() {
            @Override
            public void onResponse(Call<WeiboContent.StatusesBean.UserBean> call, Response<WeiboContent.StatusesBean.UserBean> response) {

                Log.v("weibo",response.toString());
                requestResult.requestUserSuccess(response.body());
            }

            @Override
            public void onFailure(Call<WeiboContent.StatusesBean.UserBean> call, Throwable t) {

                Log.v("weibo",call.request().toString());
                requestResult.requestUserFail();
            }
        });


    }
}

interface MainListRequest_Interface {

    @GET("2/statuses/home_timeline.json")
    Call<WeiboContent> getList(@Query("access_token") String token,@Query("count") String count,@Query("page") String page);
}

interface MainUserRequest_Interface {

    @GET("2/users/show.json")
    Call<WeiboContent.StatusesBean.UserBean> getUser(@Query("access_token") String token,@Query("uid") String uid);
}

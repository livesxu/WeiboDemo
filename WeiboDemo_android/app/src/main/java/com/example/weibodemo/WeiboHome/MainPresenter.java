package com.example.weibodemo.WeiboHome;

import com.example.weibodemo.WeiboLogin.WeiboAccount;

enum DataHandleType {

    DataHandleRefresh,
    DataHandleAppend
}

public class MainPresenter implements MainModel.RequestResult,MainModel.RequestUserResult {

    MainView mainView;

    MainModel mainModel;

    boolean isHavedToken = false;

    public int page = 1;

    public MainPresenter(MainView mainView) {

        this.mainView = mainView;

        mainModel = new MainModel();

        intoAccess_token(WeiboAccount.getInstance().getAccess_token());
    }

    //传入access_token
    public void intoAccess_token (String access_token) {

        if (access_token == null || access_token.length() == 0 || isHavedToken == true) {

            return;
        }
        isHavedToken = true;

        mainModel.setAccessToken(access_token);
        mainModel.requestList(this,1);
        mainModel.requestMainUser(this);
    }

    //请求数据
    public void requestDatas(DataHandleType type) {

        if (type == DataHandleType.DataHandleRefresh) {

            mainModel.requestList(this,1);

        } else  {

            mainModel.requestList(this,page +1);
        }
    }

    @Override
    public void requestSuccess(WeiboContent content, int page) {

        this.page = page;
        mainView.refreshItems(content.getStatuses(), page);
    }

    @Override
    public void requestFail() {

        mainView.toastError();
    }

    @Override
    public void requestUserSuccess(WeiboContent.StatusesBean.UserBean user) {

        mainView.refreshMainUser(user);
    }

    @Override
    public void requestUserFail() {


    }
}

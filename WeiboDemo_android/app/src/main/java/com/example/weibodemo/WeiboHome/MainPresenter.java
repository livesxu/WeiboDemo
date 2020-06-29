package com.example.weibodemo.WeiboHome;

public class MainPresenter implements MainModel.RequestResult {

    MainView mainView;

    MainModel mainModel;

    public MainPresenter(MainView mainView) {

        this.mainView = mainView;

        mainModel = new MainModel();
        mainModel.requestList(this);
    }

    @Override
    public void requestSuccess(WeiboContent content) {

        mainView.refreshItems(content.getStatuses());
    }

    @Override
    public void requestFail() {

        mainView.toastError();
    }
}

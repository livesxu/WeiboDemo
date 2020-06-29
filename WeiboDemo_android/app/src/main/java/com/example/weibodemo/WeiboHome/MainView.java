package com.example.weibodemo.WeiboHome;

import java.util.List;

public interface MainView {

    void refreshItems(List<WeiboContent.StatusesBean> statuses);

    void toastError();
}

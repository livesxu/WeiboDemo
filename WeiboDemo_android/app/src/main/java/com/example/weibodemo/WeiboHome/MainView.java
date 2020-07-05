package com.example.weibodemo.WeiboHome;

import java.util.List;

public interface MainView {

    void refreshMainUser(WeiboContent.StatusesBean.UserBean user);

    void refreshItems(List<WeiboContent.StatusesBean> statuses,int page);

    void toastError();
}

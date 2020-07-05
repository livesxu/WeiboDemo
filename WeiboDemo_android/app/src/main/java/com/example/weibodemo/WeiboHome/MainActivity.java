package com.example.weibodemo.WeiboHome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.flutter.embedding.android.FlutterActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.example.weibodemo.R;
import com.example.weibodemo.WeiboLogin.WeiboAccount;
import com.example.weibodemo.WeiboLogin.WeiboDismissEvent;
import com.example.weibodemo.WeiboLogin.WeiboFlutterActivity;
import com.example.weibodemo.WeiboLogin.WeiboInstallEvent;
import com.example.weibodemo.WeiboLogin.WeiboLoginEvent;
import com.example.weibodemo.databinding.ActivityMainBinding;
import com.example.weibodemo.databinding.RecyclerItemBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView, OnRefreshListener, OnLoadMoreListener {

    ActivityMainBinding activityMainBinding;

    SwipeToLoadLayout swipeToLoadLayout;

//    @Inject
    MainPresenter mainPresenter;

    RecyclerView recyclerView;

    MainRecyclerAdapter recyclerAdapter;

    List<WeiboContent.StatusesBean> showStatuses;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeiboInstallEvent event) {

        Log.v("weibo","eventBus receive auth");

        if (event.access_token != null && event.access_token.length() > 0) {

            mainPresenter.intoAccess_token(event.access_token);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        swipeToLoadLayout = activityMainBinding.swipeToLoadLayout;

        swipeToLoadLayout.setOnRefreshListener(this);

        swipeToLoadLayout.setOnLoadMoreListener(this);

        EventBus.getDefault().register(this);

        userInfo();

        mainPresenter = new MainPresenter(this);

        recyclerView = activityMainBinding.swipeTarget;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerAdapter = new MainRecyclerAdapter(showStatuses,this);

        recyclerAdapter.setHasStableIds(true);//配合getItemId使用可防止复用错乱

        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    void userInfo () {

        UserItemView userItemView = activityMainBinding.userTop;

        if (WeiboAccount.getInstance().getAccess_token() != null && WeiboAccount.getInstance().getAccess_token().length() > 0) {

            //显示当前用户信息


        } else {

            userItemView.setName("未登录");
            userItemView.setClickable(true);
            userItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //这个方法创建的activity就是FlutterActivity,如果需要继承FlutterActivity则重写，如下：
                    //startActivity(FlutterActivity.withCachedEngine("weibo_engine").build(MainActivity.this));

                    //需要监听事件让activity返回，所以就定义了一个WeiboFlutterActivity
                    startActivityForResult(WeiboFlutterActivity.engine().build(MainActivity.this),123);
                     EventBus.getDefault().post(new WeiboLoginEvent());

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == 12138) {

            String access_token = data.getStringExtra("access_token");

            mainPresenter.intoAccess_token(access_token);
        }
    }

    @Override
    public void refreshMainUser(WeiboContent.StatusesBean.UserBean user) {

        UserItemView userItemView = activityMainBinding.userTop;
        //获取到数据正常展示
        userItemView.setImageUrl(user.getProfile_image_url());
        userItemView.setName(user.getScreen_name());

        userItemView.setClickable(false);
    }

    @Override
    public void refreshItems(List<WeiboContent.StatusesBean> statuses, int page) {

        //请求成功 - 刷新状态去除
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
        if (page == 1) {

            showStatuses = statuses;
        } else {

            showStatuses.addAll(statuses);
        }

        recyclerAdapter.showStatuses = showStatuses;

        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastError() {

        //请求失败 - 刷新状态去除
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
        Toast.makeText(this,"请求失败",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {

        //刷新数据
        mainPresenter.requestDatas(DataHandleType.DataHandleRefresh);
    }

    @Override
    public void onLoadMore() {

        //加载更多数据
        mainPresenter.requestDatas(DataHandleType.DataHandleAppend);
    }
}

package com.example.weibodemo.WeiboHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weibodemo.R;
import com.example.weibodemo.WeiboLogin.WeiboFlutterActivity;
import com.example.weibodemo.WeiboLogin.WeiboLoginEvent;
import com.example.weibodemo.databinding.ActivityMainBinding;
import com.example.weibodemo.databinding.RecyclerItemBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView {

//    @Inject
    MainPresenter mainPresenter;

    RecyclerView recyclerView;

    MainRecyclerAdapter recyclerAdapter;

    List<WeiboContent.StatusesBean> showStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        mainPresenter = new MainPresenter(this);

        recyclerView = activityMainBinding.recycler;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerAdapter = new MainRecyclerAdapter(showStatuses,this);

        recyclerAdapter.setHasStableIds(true);//配合getItemId使用可防止复用错乱

        recyclerView.setAdapter(recyclerAdapter);

//        activityMainBinding.btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //这个方法创建的activity就是FlutterActivity,如果需要继承FlutterActivity则重写，如下：
////                startActivity(FlutterActivity.withCachedEngine("weibo_engine").build(MainActivity.this));
//
//                //需要监听事件让activity返回，所以就定义了一个WeiboFlutterActivity
//                startActivity(WeiboFlutterActivity.engine().build(MainActivity.this));
//
//                EventBus.getDefault().post(new WeiboLoginEvent());
//            }
//        });
    }

    @Override
    public void refreshItems(List<WeiboContent.StatusesBean> statuses) {

        showStatuses = statuses;

        recyclerAdapter.showStatuses = showStatuses;

        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastError() {

    }
}

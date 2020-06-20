package com.example.weibodemo.WeiboHome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.weibodemo.WeiboLogin.WeiboFlutterActivity;
import com.example.weibodemo.WeiboLogin.WeiboLoginEvent;
import com.example.weibodemo.databinding.ActivityMainBinding;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

//    @Inject
//    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
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
}

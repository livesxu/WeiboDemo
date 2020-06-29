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

    RecyclerView.Adapter recyclerAdapter;

    List<WeiboContent.StatusesBean> showStatuses;

    int picWidth = 0;
    int picSpec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        //获取每个微博中图片的宽 一行三张图，边、间隔10dp
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int screenWidth = point.x;
        //因为设置图片宽度是以px为单位的，需要转换
        final float scale = getResources().getDisplayMetrics().density;
        picSpec = (int)(scale*10 + 0.5f);
        picWidth = (screenWidth - 4*picSpec)/3;

        mainPresenter = new MainPresenter(this);

        recyclerView = activityMainBinding.recycler;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerAdapter = new RecyclerView.Adapter() {

            GridAdapter gridAdapter;

            class GridAdapter extends BaseAdapter {

                List<WeiboContent.StatusesBean.PicUrlsBean> images;

                public void setImages(List<WeiboContent.StatusesBean.PicUrlsBean> images) {
                    this.images = images;

                    this.notifyDataSetChanged();
                }

                @Override
                public int getCount() {

                    return 9;
                }

                @Override
                public Object getItem(int i) {
                    return null;
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {

                    ImageView imageView;
                    if (view == null) {

                        imageView = new ImageView(viewGroup.getContext());
                        imageView.setBackgroundColor(Color.RED);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWidth,picWidth);
                        imageView.setLayoutParams(layoutParams);

                    } else {

                        imageView = (ImageView)view;
                    }
                    if (images.size() > i) {
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(viewGroup.getContext()).load(images.get(i).getThumbnail_pic()).into(imageView);
                    } else {
                        imageView.setVisibility(View.INVISIBLE);
                    }
                    return imageView;
                }
            }

            class ItemHolder extends RecyclerView.ViewHolder {

                public ItemHolder(@NonNull View itemView) {
                    super(itemView);
                }
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                ConstraintLayout view = (ConstraintLayout)getLayoutInflater().inflate(R.layout.recycler_item,parent,false);

                gridAdapter = new GridAdapter();

                GridView gridView = new GridView(parent.getContext());
                gridView.setId(R.id.grid_images);
                gridView.setNumColumns(3);
                gridView.setVerticalSpacing(picSpec);
                gridView.setHorizontalSpacing(picSpec);

                ConstraintLayout.LayoutParams gridViewLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,picWidth*3 + picSpec *2);
                gridViewLayoutParams.topToBottom = R.id.text;
                gridViewLayoutParams.topMargin = picSpec/2;
                gridViewLayoutParams.leftToLeft = R.id.recycler_item;
                gridViewLayoutParams.leftMargin = picSpec;
                gridViewLayoutParams.rightToRight = R.id.recycler_item;
                gridViewLayoutParams.rightMargin = picSpec;

                gridView.setLayoutParams(gridViewLayoutParams);
                view.addView(gridView);

                gridView.setAdapter(gridAdapter);

                return new ItemHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

                WeiboContent.StatusesBean statusesBean = showStatuses.get(position);

                View view = holder.itemView;

                UserItemView userItemView = view.findViewById(R.id.user);

                userItemView.setImageUrl(statusesBean.getUser().getProfile_image_url());
                userItemView.setName(statusesBean.getUser().getScreen_name());

                TextView textView = view.findViewById(R.id.text);

                textView.setText(statusesBean.getText());

                gridAdapter.setImages(statusesBean.getPic_urls());
            }

            @Override
            public int getItemCount() {

                if (showStatuses == null) {

                    return 0;
                }
                return showStatuses.size();
            }
        };

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

        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void toastError() {

    }
}

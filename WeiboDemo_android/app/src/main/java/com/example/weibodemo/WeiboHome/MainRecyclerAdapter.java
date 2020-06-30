package com.example.weibodemo.WeiboHome;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.weibodemo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MainRecyclerAdapter extends RecyclerView.Adapter {

    int picSpec = 0;
    int picWidth = 0;


    List<WeiboContent.StatusesBean> showStatuses;

    Activity activity;

    public MainRecyclerAdapter(List<WeiboContent.StatusesBean> showStatuses, Activity activity) {

        this.showStatuses = showStatuses;
        this.activity = activity;

        //获取每个微博中图片的宽 一行三张图，边、间隔10dp
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int screenWidth = point.x;
        //因为设置图片宽度是以px为单位的，需要转换
        final float scale = activity.getResources().getDisplayMetrics().density;
        picSpec = (int)(scale*10 + 0.5f);
        picWidth = (screenWidth - 4*picSpec)/3;
    }

    class GridAdapter extends BaseAdapter {

        List<WeiboContent.StatusesBean.PicUrlsBean> images;

        int indexGride = 0;

        public void setImages(List<WeiboContent.StatusesBean.PicUrlsBean> images) {
            this.images = images;

            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            if (images == null) {

                return 0;
            }

            return images.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i + indexGride;
        }

        @Override
        public boolean hasStableIds() {
            return true;
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

            Glide.with(viewGroup.getContext()).load(images.get(i).getThumbnail_pic()).centerCrop().transform(new RoundedCorners(8)).into(imageView);

            return imageView;
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ConstraintLayout recyclerItemView;
        GridAdapter gridAdapter;
        public ItemHolder(ConstraintLayout recyclerItemView,GridAdapter gridAdapter) {
            super(recyclerItemView);

            this.recyclerItemView = recyclerItemView;
            this.gridAdapter = gridAdapter;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout view = (ConstraintLayout)activity.getLayoutInflater().inflate(R.layout.recycler_item,parent,false);

        GridAdapter gridAdapter = new GridAdapter();

        GridView gridView = view.findViewById(R.id.grid_images);

        gridView.setAdapter(gridAdapter);

        return new ItemHolder(view,gridAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemHolder itemHolder = (ItemHolder)holder;

        WeiboContent.StatusesBean statusesBean = showStatuses.get(position);

        View view = itemHolder.recyclerItemView;

        GridAdapter gridAdapter = itemHolder.gridAdapter;

        UserItemView userItemView = view.findViewById(R.id.user);

        userItemView.setImageUrl(statusesBean.getUser().getProfile_image_url());
        userItemView.setName(statusesBean.getUser().getScreen_name());

        TextView textView = view.findViewById(R.id.text);

        textView.setText(statusesBean.getText());

        GridView gridView = view.findViewById(R.id.grid_images);
        if (statusesBean.getPic_urls().size() > 0) {

            int line = (statusesBean.getPic_urls().size() - 1)/3 + 1;
            gridView.getLayoutParams().height = line * (picWidth + picSpec);
        } else {
            gridView.getLayoutParams().height = 0;
        }

        gridAdapter.indexGride = position * 1000;
        gridAdapter.setImages(statusesBean.getPic_urls());
    }

    @Override
    public int getItemCount() {

        if (showStatuses == null) {

            return 0;
        }
        return showStatuses.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

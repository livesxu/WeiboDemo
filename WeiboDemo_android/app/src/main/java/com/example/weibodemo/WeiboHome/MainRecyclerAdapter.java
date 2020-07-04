package com.example.weibodemo.WeiboHome;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.CalendarContract;
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
import androidx.recyclerview.widget.GridLayoutManager;
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

    class GridAdapter extends RecyclerView.Adapter {

        List<WeiboContent.StatusesBean.PicUrlsBean> images;

        int indexGride = 0;

        public void setImages(List<WeiboContent.StatusesBean.PicUrlsBean> images) {
            this.images = images;

            this.notifyDataSetChanged();
        }

        class ImageHolder extends RecyclerView.ViewHolder {

            public ImageHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        //onMeasure 返回widthMeasureSpec使宽高相等
        class EqualImageView extends androidx.appcompat.widget.AppCompatImageView {

            public EqualImageView(Context context) {
                super(context);
            }

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            EqualImageView imageView = new EqualImageView(parent.getContext());
            imageView.setBackgroundColor(Color.RED);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(picWidth,0);
            //对Recyclerview设置一些间距也可以直接在item里面设置，也可以recyclerView.addItemDecoration
            layoutParams.leftMargin = picSpec;
            layoutParams.bottomMargin = picSpec;
            imageView.setLayoutParams(layoutParams);

            return new ImageHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            EqualImageView equalImageView = (EqualImageView)holder.itemView;

            Glide.with(equalImageView.getContext()).load(images.get(position).getThumbnail_pic()).centerCrop().transform(new RoundedCorners(10)).into(equalImageView);
        }

        @Override
        public int getItemCount() {
            if (images == null) {

                return 0;
            }

            return images.size();
        }

        @Override
        public long getItemId(int position) {

            return position + indexGride;
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

        RecyclerView gridView = view.findViewById(R.id.grid_images);
        gridView.setBackgroundColor(Color.GREEN);
        gridView.setLayoutManager(new GridLayoutManager(parent.getContext(),3));
        //todo: addItemDecoration 对RecyclerView的item进行布局优化!
        gridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);

//                outRect.set(picSpec,0,0,picSpec);
//                int childPosition = parent.getChildAdapterPosition(view);
//                GridLayoutManager gridLayoutManager = (GridLayoutManager)parent.getLayoutManager();
//                int spanCount = gridLayoutManager.getSpanCount();
//                int itemCount = parent.getAdapter().getItemCount();
//
//                if (childPosition%spanCount == 0) {//最左边的那个
//
//                    outRect.set(picSpec,0,picSpec/2,picSpec);
//                } else if (childPosition%spanCount == spanCount - 1) {//最右边的那个
//
//                    outRect.set(picSpec/2,0,picSpec,picSpec);
//                } else {
//
//                    outRect.set(picSpec/2,0,picSpec/2,picSpec);
//                }
            }
        });

        gridAdapter.setHasStableIds(true);
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

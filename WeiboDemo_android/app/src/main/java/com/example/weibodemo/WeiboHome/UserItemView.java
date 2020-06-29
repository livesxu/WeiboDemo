package com.example.weibodemo.WeiboHome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weibodemo.R;

import androidx.annotation.Nullable;

public class UserItemView extends LinearLayout {

    String imageUrl;
    String name;

    public UserItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.user_item,this,true);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;

        ImageView imageView = findViewById(R.id.user_image);

        Glide.with(getContext()).load(this.imageUrl).circleCrop().into(imageView);
    }

    public void setName(String name) {
        this.name = name;

        TextView nameTextView = findViewById(R.id.user_name);
        nameTextView.setText(name);
    }
}

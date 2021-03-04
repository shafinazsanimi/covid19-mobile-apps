package com.example.covid19apps.guest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.covid19apps.R;

public class PostVideoViewHolder extends BaseViewHolder {

    private TextView textTime;
    private ImageView imageUser;

    public PostVideoViewHolder(@NonNull View itemView) {
        super(itemView);
        textTime = itemView.findViewById(R.id.post_video_time);
        imageUser = itemView.findViewById(R.id.post_video_image);
    }

    @Override
    void setData(TimelineItem item) {
        PostVideoItem post = item.getPostVideoItem();
        textTime.setText(post.getTime());
        Glide.with(itemView.getContext()).load(post.getUserImage()).into(imageUser);
    }

}

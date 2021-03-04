package com.example.covid19apps.guest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.covid19apps.R;

public class PostTextViewHolder extends BaseViewHolder {

    private TextView textTime, textContent;
    private ImageView imageUser;

    public PostTextViewHolder(@NonNull View itemView) {
        super(itemView);
        textTime = itemView.findViewById(R.id.post_text_time);
        textContent = itemView.findViewById(R.id.post_text_content);
        imageUser = itemView.findViewById(R.id.post_text_image);
    }

    @Override
    void setData(TimelineItem item) {
        PostTextItem post = item.getPostTextItem();
        textTime.setText(post.getTime());
        textContent.setText(post.getPostText());
        Glide.with(itemView.getContext()).load(post.getImageUser()).into(imageUser);
    }

}

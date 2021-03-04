package com.example.covid19apps.guest;

import com.example.covid19apps.R;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static List<TimelineItem> getTimelineData() {
        List<TimelineItem> mdata = new ArrayList<>();

        HeaderTextItem itemHeader = new HeaderTextItem("Yesterday");
        TimelineItem headerTimelineItem = new TimelineItem(itemHeader);

        PostTextItem postTextItem = new PostTextItem("All the description of the post will be displayed here. All the description of the post will be displayed here. All the description of the post will be displayed here.", R.drawable.ic_profile, "10:15");
        TimelineItem postTextTimelineItem = new TimelineItem(postTextItem);

        PostVideoItem postVideoItem = new PostVideoItem("", R.drawable.ic_profile, "7:30");
        TimelineItem postVideoTimelineItem = new TimelineItem(postVideoItem);

        mdata.add(headerTimelineItem);
        mdata.add(postTextTimelineItem);
        mdata.add(postVideoTimelineItem);

        return mdata;
    }

}

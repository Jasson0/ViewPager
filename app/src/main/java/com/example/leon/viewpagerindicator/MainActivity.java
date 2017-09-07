package com.example.leon.viewpagerindicator;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ViewPager viewPager;
    MyIndicator myIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        myIndicator = findViewById(R.id.my_indicator);
        List<View> listView = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.view1_layout, null);
        View view2 = inflater.inflate(R.layout.view2_layout, null);
        View view3 = inflater.inflate(R.layout.view3_layout, null);
        listView.add(view1);
        listView.add(view2);
        listView.add(view3);
        List<String> titleList = new ArrayList<>();
        titleList.add("leon");
        titleList.add("lulu");
        titleList.add("jasson");
        MyAdapter myAdapter = new MyAdapter(listView, titleList);
        viewPager.setAdapter(myAdapter);
        myIndicator.init(viewPager, this);
    }
}

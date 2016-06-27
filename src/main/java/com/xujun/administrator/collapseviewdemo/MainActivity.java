package com.xujun.administrator.collapseviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xujun.administrator.collapseviewdemo.view.CollapseView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mRoot;
    private RelativeLayout.LayoutParams mLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoot = findView(R.id.root);
        CollapseView collapseView = geneCollapseView("唐嫣大美女", "1");
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.tangyan_11);
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(mLayoutParams);
        collapseView.setContent(imageView);

        mRoot.addView(collapseView);

        CollapseView collapseView2 = geneCollapseView("唐嫣大美女", "2");
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.mipmap.tangyan_1);
        mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(mLayoutParams);
        collapseView2.setContent(imageView2);
        mRoot.addView(collapseView2);
    }

    private CollapseView geneCollapseView(String title,String number) {
        CollapseView collapseView = new CollapseView(this);
        collapseView.setTitle(title);
        collapseView.setNumber(number);
        return collapseView;
    }

    public <T extends View> T  findView (int id){
        return (T)findViewById(id);
    }
}

package com.example.phoneticsystem.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseTitleActivity;
import com.example.phoneticsystem.util.ClickUtil;
import com.example.phoneticsystem.util.ToastUtil;

public class MainActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initData() {
        super.initData();
        Toolbar toolbar=getToolbar();
        toolbar.setTitle("首页");
        toolbar.setNavigationIcon(R.mipmap.ic_search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ClickUtil.isFastClick()){
                    return;
                }
                ToastUtil.successShortToast("24324324");
//                startActivity(SearchActivity.class);
            }
        });
    }

    @Override
    protected void showBackMenu() {

    }
}

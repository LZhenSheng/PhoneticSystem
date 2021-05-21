package com.example.phoneticsystem.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.phoneticsystem.R;

import butterknife.BindView;

public abstract class BaseTitleFragment extends BaseCommonFragment {
    /**
     * 标题控件
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBar(R.color.main_color);
    }

    @Override
    protected void initData() {
        super.initData();

        setTitleCenter(toolbar);
        setHasOptionsMenu(true);
    }



    /**
     * 是否显示返回按钮
     * @return
     */
    protected boolean isShowBackMenu() {
        return true;
    }


    public static void setTitleCenter(Toolbar toolbar) {
        String title = "title";
        final CharSequence originalTitle = toolbar.getTitle();
        toolbar.setTitle(title);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.equals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
            toolbar.setTitle(originalTitle);
        }
    }

}

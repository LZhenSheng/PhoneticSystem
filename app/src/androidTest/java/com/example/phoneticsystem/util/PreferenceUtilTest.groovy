package com.example.phoneticsystem.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.phoneticsystem.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4.class)
public class PreferenceUtilTest  {
    @Test
    public void init(){
        Constant constant=new Constant();
        TimeUtil timeUtil=new TimeUtil();
        TimeUtil.commonFormat(DateUtil.getDate());
        UUIDUtil uuidUtil=new UUIDUtil();
        UUIDUtil.getUniquePsuedoID();
        UUIDUtil.generateFileName();
        ToastUtil toastUtil=new ToastUtil();
        ToastUtil.successShortToast(R.string.add_message);
        ToastUtil.successShortToast("第三方");
        ToastUtil.errorShortToast("dslfj");
        ToastUtil.errorShortToast(R.string.add_message);
        LiteORMUtil liteORMUtil= LiteORMUtil.getInstance(this);
        liteORMUtil.queryUser();
        liteORMUtil.createOrUpdate(null);
        liteORMUtil.deleteAllUser();
        LogUtil d=new LogUtil();
        LogUtil.d("dljf","dlkjf");
        ClickUtil clickUtil=new ClickUtil();
        ClickUtil.isFastClick();
        KeyboardUtil.hideKeyboard(this);
        KeyboardUtil keyboardUti=new KeyboardUtil();
        StringUtil stringUtil=new StringUtil();
        StringUtil.isPhone("dslkfjs");

        ThreadUtil.getInstance().init(this);
        ThreadUtil.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}

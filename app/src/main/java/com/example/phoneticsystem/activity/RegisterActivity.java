package com.example.phoneticsystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseActivity;
import com.example.phoneticsystem.bmob.Account;
import com.example.phoneticsystem.util.ClickUtil;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.util.StringUtil;
import com.example.phoneticsystem.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {


    /***
     * 绑定对象
     */
    @BindView(R.id.r_account)
    EditText rAccount;
    @BindView(R.id.r_password1)
    EditText rPassword1;
    @BindView(R.id.r_password2)
    EditText rPassword2;

    @OnClick(R.id.r_login)
    public void onViewClicked(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        String account1=rAccount.getText().toString();
        String password1=rPassword1.getText().toString();
        String password2=rPassword2.getText().toString();
        if(account1!=null&&password1!=null){
            if(password2!=null){
                if(StringUtil.isPhone(account1)){
                    if(password1.equals(password2)){
                        Account account=new Account();
                        account.setAccount(account1);
                        account.setPassword(password1);
                        account.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    ToastUtil.successShortToast(R.string.register_success);
                                    PreferenceUtil.setAccount(account1);
                                    PreferenceUtil.setPassword(password1);
                                    startActivityAfterFinishThis(LoginActivity.class);
                                }else{
                                    ToastUtil.errorShortToast(R.string.register_failure+e.toString());
                                }
                            }
                        });
                    }else{
                        ToastUtil.errorShortToast(R.string.confirm_password);
                    }
                }else{
                    ToastUtil.errorShortToast(R.string.err_phone_format);
                }
            }else{
                ToastUtil.errorShortToast("请确认密码");
            }
        }else{
            ToastUtil.errorShortToast("请补全账号和密码");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initData() {
        super.initData();
        //隐藏状态栏
        hideStatusBar();
        lightStatusBar(R.color.location_login);
    }
}

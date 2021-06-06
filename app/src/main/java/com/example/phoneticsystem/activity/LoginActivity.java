package com.example.phoneticsystem.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.EditText;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseActivity;
import com.example.phoneticsystem.bmob.Account;
import com.example.phoneticsystem.util.ClickUtil;
import com.example.phoneticsystem.util.LogUtil;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.util.StringUtil;
import com.example.phoneticsystem.util.ToastUtil;
import com.google.gson.Gson;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.l_account)
    EditText l_account;

    @BindView(R.id.l_password)
    EditText l_password;

    @OnClick(R.id.l_login)
    public void Login(){
        if(ClickUtil.isFastClick()){
            return;
        }
        String account=l_account.getText().toString();
        String password=l_password.getText().toString();
        LogUtil.d("2324234",account+"   "+password+" "+account.length());
//        if(account!=null&&account.length()>0){
//
//            if(password!=null&&password.length()>0){
//                if(password.length()<6||password.length()>18){
//                    ToastUtil.errorShortToast("密码格式错误");
//                    return;
//                }
//                for(int i=0;i<password.length();i++){
//                    char c=password.charAt(i);
//                    if(c==' '){
//                        ToastUtil.errorShortToast("密码格式错误");
//                        return;
//                    }
//                }
//                if(password.equals("121212")){
//                    ToastUtil.successShortToast("登录成功，正在前往首页");
//                }else{
//                    ToastUtil.errorShortToast("账号或密码错误");
//                }
//            }else{
//                ToastUtil.errorShortToast("请补全密码");
//            }
//        }else{
//            ToastUtil.errorShortToast("请补全账号");
//        }
        if(account!=null&&password!=null){
            BmobQuery<Account> bmobQuery=new BmobQuery<>();
            bmobQuery.findObjects(new FindListener<Account>() {
                @Override
                public void done(List<Account> list, BmobException e) {
                    if(e==null){
                        if(list!=null){
                            Gson gson=new Gson();
                            PreferenceUtil.setAlluser(gson.toJson(list));
                            for(int i=0;i< list.size();i++){
                                if(list.get(i).getAccount().equals(account)&&list.get(i).getPassword().equals(password)){
                                    PreferenceUtil.setAccount(account);
                                    PreferenceUtil.setPassword(password);
                                    PreferenceUtil.setLogin(true);
                                    list.get(i).setIp(getIPAddress(LoginActivity.this));
                                    list.get(i).update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                        }
                                    });
                                    ToastUtil.successShortToast(R.string.login_success);
                                    startActivityAfterFinishThis(MainActivity.class);
                                    break;
                                }
                                if(i==list.size()){
                                    ToastUtil.errorShortToast(R.string.err_acccount_or_password);
                                    d(account+password+list.toString());
                                }
                            }
                        }
                    }
                }
            });
        }else{
            ToastUtil.errorShortToast("请补全账号和密码");
        }
    }

    @OnClick(R.id.l_register)
    public void Register(){
        if(ClickUtil.isFastClick()){
            return;
        }
        startActivity(RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    @Override
    public void initData() {
        super.initData();
        //隐藏状态栏
        hideStatusBar();
        lightStatusBar(R.color.location_login);
        if(PreferenceUtil.getAccount()!=null){
            l_account.setText(PreferenceUtil.getAccount());
            l_password.setText(PreferenceUtil.getPassword());
        }
    }






}

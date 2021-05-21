package com.example.phoneticsystem.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseActivity;
import com.example.phoneticsystem.bean.ChatModel;
import com.example.phoneticsystem.bean.MessageStack;
import com.example.phoneticsystem.bean.Voice;
import com.example.phoneticsystem.util.DateUtil;
import com.example.phoneticsystem.util.LogUtil;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.util.UUIDUtil;
import com.example.phoneticsystem.view.MediaManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private final int PORT=12345;

    private ServerSocket server = null;
    String savePath;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            // 如果当前activity已经退出，那么我就不处理handler中的消息
            if(isFinishing()) {
                return;
            }

            // 判断进入主页面还是登录页面
            toMainOrLogin();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideStatusBar();
        // 发送2s钟的延时消息
        handler.sendMessageDelayed(Message.obtain(),2000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //FIXME 这里直接更新ui是不行的
                //还有其他更新ui方式,runOnUiThread()等
                Log.d("csl","开始接收");
                try {
                    if(server == null)
                        server = new ServerSocket(PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("csl","ServerSocket error" + e.getMessage());
                }
                if (server != null) {
                    while (true) {
                        try {
                            Socket data = server.accept();
                            InputStream dataStream = data.getInputStream();
                            byte[] buffer = new byte[1024*8];
                            int size = -1;
                            byte[] buf=new byte["1".getBytes().length];
                            dataStream.read(buf);
                            String str=new String(buf);
                            LogUtil.d("csl","TYPE"+str);
                            if(str.equals("1")) {
                                File dir = new File(Environment.getExternalStorageDirectory() + "/recorder_audios/" + UUIDUtil.generateFileName());
                                LogUtil.d("csl", dir.getName() + " ");
                                savePath = dir.getPath(); // 定义完整的存储路径
                                FileOutputStream file = new FileOutputStream(savePath, false);
                                while ((size = dataStream.read(buffer)) != -1) {
                                    Log.d("csl", "received size = " + size);
                                    file.write(buffer, 0, size);
                                }
                                file.close();
                                Voice voice=new Voice(savePath, DateUtil.getDate(),false,3,false);
                                Gson gson=new Gson();
                                List<ChatModel> list=gson.fromJson(PreferenceUtil.getResotreAllRecord(),new TypeToken<List<ChatModel>>(){}.getType());
                                if(list!=null){
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getAccount().equals(PreferenceUtil.getChatId())){
                                            list.get(i).addVoices(voice);
                                            PreferenceUtil.setResotreAllRecord(gson.toJson(list));
                                            break;
                                        }
                                    }
                                }
                                if(PreferenceUtil.getMessage()==null){
                                    List<MessageStack> list1=new ArrayList<>();
                                    MessageStack message=new MessageStack();
                                    message.setAccount(PreferenceUtil.getChatId());
                                    message.addMessages(savePath);
                                    list1.add(message);
                                    PreferenceUtil.setMessage(new Gson().toJson(list1));
                                }else{
                                    List<MessageStack> list2=gson.fromJson(PreferenceUtil.getMessage(),new TypeToken<List<MessageStack>>(){}.getType());
                                    for(int i=0;i<list2.size();i++){
                                        if(list2.get(i).getAccount().equals(PreferenceUtil.getChatId())){
                                            list2.get(i).addMessages(savePath);
                                            PreferenceUtil.setMessage(gson.toJson(list2));
                                            break;
                                        }
                                        if(i==list2.size()-1){
                                            MessageStack message=new MessageStack();
                                            message.setAccount(PreferenceUtil.getChatId());
                                            message.addMessages(savePath);
                                            list2.add(message);
                                            PreferenceUtil.setMessage(gson.toJson(list2));
                                        }
                                    }
                                }
                                MediaManager.playSound(savePath, new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                    }
                                });
                            }
                            dataStream.close();
                            data.close();
                            Log.d("csl","接收完成");
                        } catch (Exception e) {
                            Log.d("csl","接收错误="+e.getMessage());
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁消息
        handler.removeCallbacksAndMessages(null);
    }

    // 判断进入主页面还是登录页面
    private void toMainOrLogin() {
        if(!PreferenceUtil.getGuide()){
            startActivityAfterFinishThis(GuideActivity.class);
        }
        if(PreferenceUtil.getLogin()){
            startActivityAfterFinishThis(MainActivity.class);
        }else{
            startActivityAfterFinishThis(LoginActivity.class);
        }
    }
}

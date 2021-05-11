package com.example.phoneticsystem.activity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.adapter.RecorderAdapter;
import com.example.phoneticsystem.activity.base.BaseTitleActivity;
import com.example.phoneticsystem.util.ToastUtil;
import com.example.phoneticsystem.view.AudioRecorderButton;
import com.example.phoneticsystem.view.MediaManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.phoneticsystem.activity.LoginActivity.getIPAddress;

public class ChatActivity extends BaseTitleActivity {

    RecyclerView rv;
    private RecorderAdapter mAdapter;
    private List<Recorder> mDatas =new ArrayList<>();
    private AudioRecorderButton mAudioRecorderButton;
    private final int PORT=12345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
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
                    Log.d("chenshulin","ServerSocket error" + e.getMessage());
                }
                if (server != null) {
                    while (true) {
                        try {
                            // 接收文件名
                            Socket name = server.accept();
                            InputStream nameStream = name.getInputStream();
                            InputStreamReader streamReader = new InputStreamReader(nameStream);
                            BufferedReader br = new BufferedReader(streamReader);
                            String fileName = br.readLine();
                            br.close();
                            streamReader.close();
                            nameStream.close();
                            name.close();
                            // 接收文件数据
                            Socket data = server.accept();
                            InputStream dataStream = data.getInputStream();
                            // File dir = new File("/sdcard/MyMusic"); // 创建文件的存储路径
                            File dir =new File(Environment.getExternalStorageDirectory() + "/recorder_audios");
                            savePath = dir.getPath(); // 定义完整的存储路径
                            FileOutputStream file = new FileOutputStream(savePath, false);
                            byte[] buffer = new byte[1024*8];
                            int size = -1;
                            while ((size = dataStream.read(buffer)) != -1) {
                                Log.d("csl","received size = "+size);
                                file.write(buffer, 0, size);
                            }
                            file.close();
                            dataStream.close();
                            data.close();
                            ToastUtil.successShortToast("");
                            Log.d("csl","接收完成");

                            //   creatMessageBean("收到消息",false,savePath);
                            mHandler.sendEmptyMessage(RECE_VOICE_MSG);
                        } catch (Exception e) {
                            ToastUtil.successShortToast("接受失败");
                            Log.d("csl","接收完成");

                            Log.d("csl","接收错误="+e.getMessage());
                        }
                    }
                }
            }
        }).start();
    }


    private void initView(){
        rv=findViewById(R.id.rv);
        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        rv.setNestedScrollingEnabled(false);
        //创建分割线
        DividerItemDecoration decoration=new DividerItemDecoration(getMainActivity(),RecyclerView.VERTICAL);
        //添加到控件
        //可以添加多个
        rv.addItemDecoration(decoration);

        //创建适配器
        mAdapter = new RecorderAdapter(R.layout.item_recorder);

        //设置适配器
        rv.setAdapter(mAdapter);

        mAdapter.replaceData(mDatas);

        mAudioRecorderButton = findViewById(R.id.id_recorder_button);

        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                Recorder recorder = new Recorder(seconds,filePath);
                mDatas.add(recorder);
                //更新adapter
                mAdapter.replaceData(mDatas);
                rv.scrollToPosition(mDatas.size()-1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendVoiceMsg(filePath);
                    }
                }).start();
            }
        });
    }

    /**
     * 根据生命周期 管理播放录音
     */
    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    //数据类
    public class Recorder{

        public float time;
        String filePath;

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Recorder(float time, String filePath) {
            super();
            this.time = time;
            this.filePath = filePath;
        }
    }

    private void sendVoiceMsg(String path){
        try {
            Log.d("csl","sendVoiceMsg"+getIPAddress(ChatActivity.this));
            InetAddress inetAddress = InetAddress.getByName("116.1.3.252");

            Socket data = new Socket(inetAddress, PORT);
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);//获取录制好的音频文件
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fileInput.read(buffer, 0, 1024)) != -1) {//发送音频
                outputData.write(buffer, 0, size);
            }
            outputData.close();
            fileInput.close();
            data.close();
            Log.d("csl","发送完成");

        } catch (Exception e) {
            e.getMessage();
            Log.d("csl","发送异常");
            Log.d("csl","e.getMessage()="+e.getMessage());
        }
    }

    private ServerSocket server = null;
    String savePath;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECE_VOICE_MSG:

                    break;
            }
        }
    };

    private static final int RECE_VOICE_MSG = 0X101;
}

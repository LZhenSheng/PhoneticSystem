package com.example.phoneticsystem.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseTitleActivity;
import com.example.phoneticsystem.adapter.RecorderAdapter;
import com.example.phoneticsystem.bean.ChatModel;
import com.example.phoneticsystem.bean.Voice;
import com.example.phoneticsystem.util.DateUtil;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.example.phoneticsystem.view.AudioRecorderButton;
import com.example.phoneticsystem.view.MediaManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChatActivity extends BaseTitleActivity {

    PullToRefreshLayout pullToRefreshLayout;

    RecyclerView rv;
    private RecorderAdapter mAdapter;
    private List<Voice> mDatas =new ArrayList<>();
    private AudioRecorderButton mAudioRecorderButton;
    private final int PORT=12345;
    private final String IP=PreferenceUtil.getChatIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson=new Gson();
        setContentView(R.layout.activity_chat);
        if(PreferenceUtil.getResotreAllRecord()==null){
            ChatModel chatModel=new ChatModel();
            chatModel.setAccount(PreferenceUtil.getChatId());
            List<ChatModel> chatModels=new ArrayList<>();
            PreferenceUtil.setResotreAllRecord(gson.toJson(chatModels));
        }else{
            List<ChatModel> list=gson.fromJson(PreferenceUtil.getResotreAllRecord(),new TypeToken<List<ChatModel>>(){}.getType());
            if(list.size()==0){
                ChatModel chatModel=new ChatModel();
                chatModel.setAccount(PreferenceUtil.getChatId());
                list.add(chatModel);
                PreferenceUtil.setResotreAllRecord(gson.toJson(list));
            }else{
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getAccount().equals(PreferenceUtil.getChatId())){
                        break;
                    }
                    if(i==list.size()-1){
                        ChatModel chatModel=new ChatModel();
                        chatModel.setAccount(PreferenceUtil.getChatId());
                        list.add(chatModel);
                        PreferenceUtil.setResotreAllRecord(gson.toJson(list));
                    }
                }
            }
        }
        d(PreferenceUtil.getResotreAllRecord()+"start"+PreferenceUtil.getChatId());
        initView();
    }


    private void initView(){
        rv=findViewById(R.id.rv);
//尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        rv.setNestedScrollingEnabled(false);

        //禁用嵌套滚动
        rv.setNestedScrollingEnabled(false);

        //创建适配器
        mAdapter = new RecorderAdapter(R.layout.item_recorder);

        //设置适配器
        rv.setAdapter(mAdapter);

        pullToRefreshLayout = findViewById(R.id.activity_main);
        Gson gson=new Gson();
        List<ChatModel> list=gson.fromJson(PreferenceUtil.getResotreAllRecord(),new TypeToken<List<ChatModel>>(){}.getType());
        for(int i=0;i<list.size();i++){
            if(list.get(i).getAccount().equals(PreferenceUtil.getChatId())){
                mDatas.addAll(list.get(i).getVoices());
                d(list.get(i).getVoices().size()+"    "+mDatas.size()+"111");
            }
        }
        d(mDatas.size()+"232323");
        mAdapter.replaceData(mDatas);
        d(mAdapter.getData().size()+"");
        rv.scrollToPosition(mAdapter.getData().size()-1);

        mAudioRecorderButton = findViewById(R.id.id_recorder_button);

        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                //每完成一次录音
                Voice voice = new Voice(filePath, DateUtil.getDate(),true,seconds,true);
                Gson gson=new Gson();
                List<ChatModel> list=gson.fromJson(PreferenceUtil.getResotreAllRecord(),new TypeToken<List<ChatModel>>(){}.getType());
                for(int i=0;i<list.size();i++){
                    if(list.get(i).getAccount().equals(PreferenceUtil.getChatId())){
                        list.get(i).addVoices(voice);
                        d(list.get(i).getVoices().size()+"    "+mDatas.size()+"222");
                        PreferenceUtil.setResotreAllRecord(gson.toJson(list));
                        break;
                    }
                }
                mDatas.add(voice);
                //更新adapter
                next();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendVoiceMsg(filePath);
                    }
                }).start();
            }
        });

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 结束刷新
                        pullToRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        next();
                        // 结束加载更多
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });

    }

    private void next() {
        mAdapter.replaceData(mDatas);
        d(mAdapter.getData().size()+"");
        rv.scrollToPosition(mDatas.size()-1);
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
            Log.d("csl","sendVoiceMsg");
//            InetAddress inetAddress = InetAddress.getByName("221.7.210.217");

            Socket data = new Socket(IP, PORT);
            Log.d("csl","connect_result"+data.isConnected());
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);//获取录制好的音频文件
            int size = -1;
            byte[] buffer = new byte[1024];
            String str=String.valueOf(1);
            outputData.write(str.getBytes(),0,str.getBytes().length);
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


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECE_VOICE_MSG:

                    break;
            }
        }
    };

    @Override
    public void initData() {
        super.initData();
        d(PreferenceUtil.getChatId()+PreferenceUtil.getChatIp());
    }

    private static final int RECE_VOICE_MSG = 0X101;
}

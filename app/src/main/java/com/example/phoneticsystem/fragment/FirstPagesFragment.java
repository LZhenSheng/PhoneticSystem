package com.example.phoneticsystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.ChatActivity;
import com.example.phoneticsystem.activity.SearchActivity;
import com.example.phoneticsystem.adapter.MessageAdapter;
import com.example.phoneticsystem.bean.ChatModel;
import com.example.phoneticsystem.bean.MessageStack;
import com.example.phoneticsystem.bmob.Account;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.util.List;

import butterknife.BindView;
import cn.nekocode.badge.BadgeDrawable;


/***
* 首页Fragment
* @author 胜利镇
* @time 2020/8/7 8:16
*/
public class FirstPagesFragment extends BaseTitleFragment {

    @BindView(R.id.activity_main)
    PullToRefreshLayout pullToRefreshLayout;

    MessageAdapter adapter;
    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    /**
     * 构造方法
     *
     * 固定写法
     *
     * @return
     */
    public static FirstPagesFragment newInstance() {
        Bundle args = new Bundle();

        FirstPagesFragment fragment = new FirstPagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /***
    * 获取View
    */
    @Override
    protected View getLayoutView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_page,container,false);
    }

    /***
     * 初始化数据
     */
    @Override
    protected void initData() {
        super.initData();
        toolbar.setTitle("首页");
        toolbar.inflateMenu(R.menu.search_2);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_search:
                        startActivity(SearchActivity.class);
                }
                return false;
            }
        });
        setTitleCenter(toolbar);

//尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        rv.setLayoutManager(layoutManager);
        rv.setNestedScrollingEnabled(false);

        //禁用嵌套滚动
        rv.setNestedScrollingEnabled(false);

        //创建搜索历史适配器
        adapter = new MessageAdapter(R.layout.item_message);

        //设置搜索历史适配器
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                PreferenceUtil.setChatId(adapter.getData().get(i).getAccount());
                Gson gson = new Gson();
                List<Account> suggests = gson.fromJson(PreferenceUtil.getAlluser(), new TypeToken<List<Account>>() {}.getType());
                for(int j=0;j<suggests.size();j++){
                    if(suggests.get(j).getAccount().equals(PreferenceUtil.getChatId())){
                        PreferenceUtil.setChatIp(suggests.get(j).getIp());
                        break;
                    }
                }
                List<MessageStack> list=gson.fromJson(PreferenceUtil.getMessage(),new TypeToken<List<MessageStack>>(){}.getType());
                if(list!=null){
                    for(int t=0;t<list.size();t++){
                        if(list.get(t).getAccount().equals(PreferenceUtil.getChatId())){
                            list.get(t).setMessages(null);
                            PreferenceUtil.setMessage(gson.toJson(list));
                            break;
                        }
                    }
                }
                startActivity(ChatActivity.class);
            }
        });
        Gson gson = new Gson();
        if(PreferenceUtil.getResotreAllRecord()!=null){
            List<ChatModel> suggests = gson.fromJson(PreferenceUtil.getResotreAllRecord(), new TypeToken<List<ChatModel>>() {}.getType());
            adapter.replaceData(suggests);
        }

        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<ChatModel> suggests = gson.fromJson(PreferenceUtil.getResotreAllRecord(), new TypeToken<List<ChatModel>>() {}.getType());
                        adapter.replaceData(suggests);
                        Gson gson=new Gson();
                        int count=0;
                        List<MessageStack> list=gson.fromJson(PreferenceUtil.getMessage(),new TypeToken<List<MessageStack>>(){}.getType());
                        if(list!=null){
                            for(int i=0;i<list.size();i++){
                                count+=list.get(i).getMessages().size();
                            }
                        }
                        ImageView iv_count=getActivity().findViewById(R.id.iv_count);
                        if (count > 0) {
                            //有未读消息

                            //我的消息未读消息数drawable
                            BadgeDrawable countDrawable = new BadgeDrawable.Builder()
                                    .type(BadgeDrawable.TYPE_NUMBER)
                                    .number(count)

                                    //设置背景颜色
                                    //这里使用了兼容方法获取颜色
                                    .badgeColor(ContextCompat.getColor(getMainActivity(), R.color.main_color))
                                    .build();

                            iv_count.setImageDrawable(countDrawable);
                        } else {
                            //没有未读消息
                            iv_count.setImageDrawable(null);
                        }
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

                        // 结束加载更多
                        pullToRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }
}

package com.example.phoneticsystem.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.phoneticsystem.R;
import com.example.phoneticsystem.activity.base.BaseTitleActivity;
import com.example.phoneticsystem.bmob.Account;
import com.example.phoneticsystem.util.KeyboardUtil;
import com.example.phoneticsystem.util.LogUtil;
import com.example.phoneticsystem.util.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/***
 * 搜索页面--搜索框
 * @author 胜利镇
 * @time 2020/8/15 15:02
 */
public class SearchActivity extends BaseTitleActivity {

    /**
     * 搜索历史适配器
     */
    private SearchHistoryAdapter searchHistoryAdapter;

    /**
     * 搜索建议适配器
     */
    private ArrayAdapter<String> suggestAdapter;

    /**
     * 搜索建议控件
     */
    private SearchView.SearchAutoComplete searchAutoComplete;

    /**
     * 当前搜索关键字
     */
    private String data;


    private static final String TAG = "SearchActivity";
    /**
     * 搜索控件
     */
    private SearchView searchView;

    /**
     * 列表控件
     */
    @BindView(R.id.rv)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    @Override
    public void initData() {

        if (isShowBackMenu()) {
            showBackMenu();
        }

        setTitle("搜索");
        //尺寸固定
        rv.setHasFixedSize(true);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        rv.setLayoutManager(layoutManager);

        //创建搜索历史适配器
        searchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_account);

        //设置搜索历史适配器
        rv.setAdapter(searchHistoryAdapter);

        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                PreferenceUtil.setChatId(searchHistoryAdapter.getData().get(i).getAccount());
                PreferenceUtil.setChatIp(searchHistoryAdapter.getData().get(i).getIp());
                startActivityAfterFinishThis(ChatActivity.class);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    /**
     * 设置搜索数据并搜索
     *
     * @param data
     */
    private void setSearchData(String data) {
        //将内容设置到搜索控件
        //并马上执行搜索
        searchView.setQuery(data, true);

        //进入搜索状态
        searchView.setIconified(false);

        //隐藏软键盘
        KeyboardUtil.hideKeyboard(getMainActivity());
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        //查找搜索按钮
        MenuItem searchItem = menu.findItem(R.id.action_search);

        //查找搜索控件
        searchView = (SearchView) searchItem.getActionView();

        //可以在这里配置SearchView

        //设置搜索监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * 提交了搜索
             * 回车搜索调用两次
             * 点击键盘上搜索
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            /**
             * 搜索输入框文本改变了
             * @param newText
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                fetchSuggestion(newText);
                return true;
            }
        });

        //是否进入界面就打开搜索栏
        //false为默认打开
        //默认为true
        searchView.setIconified(false);

        //查找搜索建议控件
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        //默认要输入两个字符才显示提示，可以这样更改
        searchAutoComplete.setThreshold(1);

        //获取搜索管理器
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        //设置搜索信息
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //设置搜索建议点击回调
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> setSearchData(suggestAdapter.getItem(position)));
        return true;
    }

    /**
     * 执行搜索
     *
     * @param data
     */
    private void performSearch(String data) {
        this.data = data;

        LogUtil.d(TAG, "performSearch:" + data);

        showSearchResultView();


    }

    private void showSearchResultView() {
        List<Account> list=new ArrayList<>();
        Gson gson = new Gson();
        List<Account> suggests = gson.fromJson(PreferenceUtil.getAlluser(), new TypeToken<List<Account>>() {
        }.getType());
        for(int i=0;i<suggests.size();i++){
            if(suggests.get(i).getAccount().contains(data.trim())){
                list.add(suggests.get(i));
            }
        }
        searchHistoryAdapter.replaceData(list);
    }

    /**
     * 菜单点击了
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toolbar返回按钮点击

                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 物理按键返回调用
     */
    @Override
    public void onBackPressed() {
        if (searchView.isIconified()) {
            //不是在搜索状态

            //正常返回
            super.onBackPressed();
        } else {
            //是搜索状态

            //关闭搜索状态
            searchView.setIconified(true);
        }

    }

    /**
     * 获取搜索建议
     *
     * @param data
     */
    private void fetchSuggestion(String data) {
        LogUtil.d(TAG, "fetchSuggestion:" + data);
        Gson gson = new Gson();
        List<Account> suggests = gson.fromJson(PreferenceUtil.getAlluser(), new TypeToken<List<Account>>() {
        }.getType());
        List<Account> result = new ArrayList<>();
        for (int i = 0; i < suggests.size(); i++) {
            if (suggests.get(i).getAccount().contains(data)) {
                result.add(suggests.get(i));
            }
        }
        setSuggest(result);
    }

    /**
     * 设置搜索建议
     *
     * @param data
     */
    private void setSuggest(List<Account> data) {
        LogUtil.d(TAG, "setSuggest:" + data);

        //处理搜索建议数据
        List<String> datum = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            datum.add(data.get(i).getAccount());
        }
        d(datum.toString());
        //创建适配器
        suggestAdapter = new ArrayAdapter<>(getMainActivity(),
                R.layout.item_suggest,
                R.id.tv_title,
                datum);

        //设置到控件
        searchAutoComplete.setAdapter(suggestAdapter);
    }
}

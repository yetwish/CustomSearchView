package com.yetwish.customsearchdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yetwish.customsearchdemo.R;
import com.yetwish.customsearchdemo.activity.adapter.SearchAdapter;
import com.yetwish.customsearchdemo.activity.model.Bean;
import com.yetwish.customsearchdemo.activity.widge.SearchView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements SearchView.SearchViewListener {

    private ListView lvResults;

    private SearchAdapter hintAdapter;
    private SearchAdapter autoCompleteAdapter;
    private SearchAdapter resultAdapter;

    /**
     * 热搜版数据
     */
    private List<Bean> hintData;

    private static int HINT_SIZE = 4;

    public static void setHintSize(int hintSize) {
        HINT_SIZE = hintSize;
    }

    /**
     * 搜索过程中自动补全数据
     */
    private List<Bean> autoCompleteData;

    /**
     *
     */
    private List<Bean> resultData;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initViews() {
        lvResults = (ListView) findViewById(R.id.main_lv_search_results);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
        searchView.setSearchViewListener(this);
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(){
        getHintData();
        getAutoCompleteData();
        getResultData();
    }

    private void getHintData() {
        hintData = new ArrayList<>(HINT_SIZE);
        for (int i = 0; i < HINT_SIZE; i++) {
            hintData.add(new Bean(R.drawable.icon, "习得Android新技能" + i, "自定义搜索方法", "100"));
        }
        hintAdapter = new SearchAdapter(this, hintData, R.layout.item_bean_list);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData() {
        if (autoCompleteData == null) {
            autoCompleteData = new ArrayList<>(HINT_SIZE);
        }
        for (int i = 0; i < HINT_SIZE; i++) {
            autoCompleteData.add(new Bean
                    (R.drawable.icon, "获取Android新技能" + i, "自定义搜索方法", "300"));
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new SearchAdapter(this, autoCompleteData, R.layout.item_bean_list);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索结果data
     */
    private void getResultData() {
        int size = 10;
        if (resultData == null) {
            resultData = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                resultData.add(new Bean
                        (R.drawable.icon, "搜索结果" + i, "自定义搜索方法 :自定义搜索输入框+自定义弹出样式使用spinner配合listView\n" + "自定义数据源格式和搜索算法", "300"));
            }
        }
        if (resultAdapter == null) {
            resultAdapter = new SearchAdapter(this, resultData, R.layout.item_bean_list);
        } else {
            resultAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 当 edit text 文本改变时 触发的回调
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
    }

    /**
     * 点击搜索键时edit text触发的回调
     * @param text
     */
    @Override
    public void onSearch(String text) {
        Toast.makeText(this,"start searching",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTipsItemClick() {
        lvResults.setVisibility(View.VISIBLE);
        //第一次获取结果 还未配置适配器
        if(lvResults.getAdapter() == null){
            //获取搜索数据 设置适配器
            lvResults.setAdapter(resultAdapter);
        }else{
            //更新搜索数据
            resultAdapter.notifyDataSetChanged();
        }
    }
}

package com.yetwish.customsearchdemo.activity.widge;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yetwish.customsearchdemo.R;


/**
 * Created by yetwish on 2015-05-11
 */

public class SearchView extends LinearLayout implements View.OnClickListener {

    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    private ImageView ivDelete;

    /**
     * 返回按钮
     */
    private Button btnBack;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 弹出列表
     */
    private ListView lvTips;

    /**
     * 提示adapter （推荐adapter）
     */
    private ArrayAdapter<String> mHintAdapter;

    /**
     * 自动补全adapter 只显示名字
     */
    private ArrayAdapter<String> mAutoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        initViews();
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnBack = (Button) findViewById(R.id.search_btn_back);
        lvTips = (ListView) findViewById(R.id.search_lv_tips);

        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set edit text
                String text = lvTips.getAdapter().getItem(i).toString();
                etInput.setText(text);
                etInput.setSelection(text.length());
                //hint list view gone and result list view show
                lvTips.setVisibility(View.GONE);
                notifyStartSearching(text);
            }
        });

        ivDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lvTips.setVisibility(GONE);
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lvTips.getAdapter() == null) {
            lvTips.setAdapter(mHintAdapter);
        }
    }

    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
                lvTips.setVisibility(VISIBLE);
                if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {
                    lvTips.setAdapter(mAutoCompleteAdapter);
                }
                //更新autoComplete数据
                if (mListener != null) {
                    mListener.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                ivDelete.setVisibility(GONE);
                if (mHintAdapter != null) {
                    lvTips.setAdapter(mHintAdapter);
                }
                lvTips.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_input:
                lvTips.setVisibility(VISIBLE);
                break;
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
            case R.id.search_btn_back:
                ((Activity) mContext).finish();
                break;
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 提示列表项点击时回调方法 (提示/自动补全)
//         */
//        void onTipsItemClick(String text);
    }

}


//
//import android.app.Activity;
//import android.content.Context;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.yetwish.customsearchdemo.R;
//import com.yetwish.customsearchdemo.activity.adapter.SearchAdapter;
//import com.yetwish.customsearchdemo.activity.model.Bean;
//
///**
// * Created by yetwish on 2015-05-11
// */
//
//public class SearchView extends LinearLayout implements View.OnClickListener {
//
//    private EditText etInput;
//    private ImageView ivDelete;
//    private Button btnBack;
//    private Context mContext;
//
//    private ListView lvTips;
//    private SearchAdapter mHintAdapter;
//    private SearchAdapter mAutoCompleteAdapter;
//
//    private SearchViewListener mListener;
//
//    public void setSearchViewListener(SearchViewListener listener) {
//        mListener = listener;
//    }
//
//    public SearchView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
//        initViews();
//    }
//
//    private void initViews() {
//        etInput = (EditText) findViewById(R.id.search_et_input);
//        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
//        btnBack = (Button) findViewById(R.id.search_btn_back);
//        lvTips = (ListView) findViewById(R.id.search_lv_tips);
//
//        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //set edit text
//                Bean currentBean = (Bean) lvTips.getAdapter().getItem(i);
//                etInput.setText(currentBean.getTitle());
//                //hint list view gone and result list view show
//                lvTips.setVisibility(View.GONE);
//                if (mListener != null) {
//                    mListener.onTipsItemClick();
//                }
//            }
//        });
//
//        ivDelete.setOnClickListener(this);
//        btnBack.setOnClickListener(this);
//
//        etInput.addTextChangedListener(new EditChangedListener());
//        etInput.setOnClickListener(this);
//        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    if (mListener != null) {
//                        mListener.onSearch(etInput.getText().toString());
//                    }
//                }
//                return true;
//            }
//        });
//    }
//
//    /**
//     * 设置热搜版提示 adapter
//     */
//    public void setTipsHintAdapter(SearchAdapter adapter) {
//        this.mHintAdapter = adapter;
//        if (lvTips.getAdapter() == null) {
//            lvTips.setAdapter(mHintAdapter);
//        }
//    }
//
//    /**
//     * 设置自动补全adapter
//     */
//    public void setAutoCompleteAdapter(SearchAdapter adapter) {
//        this.mAutoCompleteAdapter = adapter;
//    }
//
//    private class EditChangedListener implements TextWatcher {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//            if (!"".equals(charSequence + "")) {
//                ivDelete.setVisibility(VISIBLE);
//                lvTips.setVisibility(VISIBLE);
//                if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {
//                    lvTips.setAdapter(mAutoCompleteAdapter);
//                } else {
//                    //更新autoComplete数据
//                    if (mListener != null) {
//                        mListener.onRefreshAutoComplete(charSequence + "");
//                    }
//                }
//            } else {
//                ivDelete.setVisibility(GONE);
//                if (mHintAdapter != null) {
//                    lvTips.setAdapter(mHintAdapter);
//                }
//                lvTips.setVisibility(GONE);
//            }
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.search_et_input:
//                lvTips.setVisibility(VISIBLE);
//                break;
//            case R.id.search_iv_delete:
//                etInput.setText("");
//                ivDelete.setVisibility(GONE);
//                break;
//            case R.id.search_btn_back:
//                ((Activity) mContext).finish();
//                break;
//        }
//    }
//
//    /**
//     * search view回调方法
//     */
//    public interface SearchViewListener {
//
//        void onRefreshAutoComplete(String text);
//
//        void onSearch(String text);
//
//        void onTipsItemClick();
//    }
//
//}

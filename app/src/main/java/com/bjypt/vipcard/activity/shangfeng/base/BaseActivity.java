package com.bjypt.vipcard.activity.shangfeng.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.shangfeng.dialog.LoadingDialog;
import com.bjypt.vipcard.activity.shangfeng.common.enums.UserInformationFields;
import com.bjypt.vipcard.activity.shangfeng.util.SharedPreferencesUtils;
import com.bjypt.vipcard.base.MyApplication;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by Dell on 2018/3/22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected BaseContract.Presenter superPresenter;
    private LoadingDialog loadingDialog;

    protected abstract int getLayoutId();//获取布局id

    protected abstract void initInjector();//注入对象,attach契约view,设置父类的presenter

    protected abstract void init();//初始化

    protected Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        //将Activity实例添加到AppManager的堆栈
        MyApplication.getInstance().addActivity(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initKeyboard();

        initInjector();
        if (superPresenter != null) {
            superPresenter.onStart();
        }

        init();

    }

    public void showProgress() {
        loadingDialog = new LoadingDialog(this, getResources().getString(R.string.wait_a_moment));
        loadingDialog.shows();

    }

    public void hideProgress() {
        if (loadingDialog != null) {
            loadingDialog.dismisss();
            loadingDialog = null;
        }
    }

    private void initKeyboard() {
        //点击空白处隐藏键盘
        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void initBackFinish(){
        findViewById(R.id.ibtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取 versionName
     *
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 设置View禁止粘贴复制
     *
     * @param editText 输入框
     */
    public void prohibitCopy(EditText editText) {
        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        overridePendingTransition(R.anim.left_in_x, R.anim.right_out_x);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in_x, R.anim.right_out_x);
        //将Activity实例从AppManager的堆栈中移除
        MyApplication.getInstance().deleteActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将Activity实例从AppManager的堆栈中移除
        MyApplication.getInstance().deleteActivity(this);
        if(loadingDialog != null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        if (superPresenter != null) {
            superPresenter.onDestroy();
        }
    }

    public boolean isLogin(){
        return "Y".equals(String.valueOf(SharedPreferencesUtils.get(UserInformationFields.IS_LOGIN, "N")));
    }
}


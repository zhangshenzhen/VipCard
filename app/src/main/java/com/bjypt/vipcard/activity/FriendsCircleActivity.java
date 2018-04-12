package com.bjypt.vipcard.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFraActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.bean.CircleBgPic;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.config.AppConfig;
import com.bjypt.vipcard.fragment.FriendsCircleFragment;
import com.bjypt.vipcard.model.CLCommentDetailsBean;
import com.bjypt.vipcard.model.UploadPicResult;
import com.bjypt.vipcard.tool.CompressPic;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.view.RoundImageView;
import com.bjypt.vipcard.view.ToastUtil;
import com.bjypt.vipcard.widget.CoverSelelctPopupWindow;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sinia.orderlang.utils.CacheUtils;
import com.yalantis.ucrop.UCrop;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bjypt.vipcard.activity.ReleaseInformationActivity.REQUEST_CODE;

/**
 * Created by 崔龙 on 2017/11/20.
 * <p>
 * 朋友圈界面
 */
public class FriendsCircleActivity extends BaseFraActivity implements VolleyCallBack {
    private static final int UPDATE_PIC = 201711281;
    private static final int REQUEST_PIC = 201711182;
    private static final String TAG = "FriendsCircleActivity";
    private String[] listName = new String[]{"推荐", "好友动态"};    // tap名字
    private SlidingTabLayout friends_tab;
    private ViewPager vp;
    private LinearLayout ll_finish;
    private ImageView iv_header_pic;
    private ImageView tv_go_release;
    private RoundImageView iv_icon;
    private TextView userIconName;
    private ArrayList<FriendsCircleFragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;  // pagerAdapter
    private CoverSelelctPopupWindow mPopupWindow;
    private View view_black;
    private File sdcardTempFile;
    private String dateTime;
    private String imgPath;
    private String PATH = Environment.getExternalStorageDirectory() + "/vipcard/Camera/";
    private String mSaveDir;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_friends_circle);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        friends_tab = (SlidingTabLayout) findViewById(R.id.friends_tab);
        iv_header_pic = (ImageView) findViewById(R.id.iv_header_pic);
        tv_go_release = (ImageView) findViewById(R.id.tv_go_release);
        userIconName = (TextView) findViewById(R.id.userIconName);
        ll_finish = (LinearLayout) findViewById(R.id.ll_finish);
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        view_black = findViewById(R.id.view_black);
        vp = (ViewPager) findViewById(R.id.vp);

        userIconName.setText(getFromSharePreference(Config.userConfig.nickname));
        ImageLoader.getInstance().displayImage(getFromSharePreference(Config.userConfig.position), iv_icon);
    }

    @Override
    public void afterInitView() {
        requestBgPic();
        mFragments.add(FriendsCircleFragment.getInstance("推荐"));
//        mFragments.add(FriendsCircleFragment.getInstance("好友动态"));
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        vp.setOffscreenPageLimit(mFragments.size());
        friends_tab.setViewPager(vp);
        friends_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mFragments.get(position).setRefresh();
            }

            @Override
            public void onTabReselect(int position) {
                mFragments.get(position).setRefresh();
            }
        });

    }

    @Override
    public void bindListener() {
        tv_go_release.setOnClickListener(this);
        iv_header_pic.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
        iv_icon.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.ll_finish:
                finish();
                break;
            case R.id.tv_go_release:
                Intent intent = new Intent(this, ReleaseInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_icon:
                Intent intent2 = new Intent(this, CircleMyActivity.class);
                intent2.putExtra("uid", getFromSharePreference(Config.userConfig.uid));
                startActivity(intent2);
                break;
            case R.id.iv_header_pic:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "需要授权 ");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        Log.i(TAG, "拒绝过了");
                        // 提示用户如果想要正常使用，要手动去设置中授权。
                        Toast.makeText(this, "请在 设置-应用管理 中开启此应用的拍照授权。", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "进行授权");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
                    }
                } else {
                    Log.i(TAG, "不需要授权 ");
                    // 进行正常操作
                    initDir();
                    mPopupWindow = new CoverSelelctPopupWindow(this, itemsOnClick);
                    view_black.setVisibility(View.VISIBLE);
                    mPopupWindow.showAtLocation(findViewById(R.id.iv_icon), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            view_black.setVisibility(View.GONE);
                        }
                    });
                }

                break;
        }
    }

    private void initDir() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            mSaveDir = Environment.getExternalStorageDirectory()
                    + "/temple";
            File dir = new File(mSaveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            sdcardTempFile = new File(mSaveDir, "hot_pic_" + "bg_img" + ".jpg");
            sdcardTempFile.delete();
            if (!sdcardTempFile.exists()) {
                try {
                    sdcardTempFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(FriendsCircleActivity.this, "图片文件创建失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case UPDATE_PIC:
                requestBgPic();
                break;
            case REQUEST_PIC:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    CircleBgPic circleBgPic = objectMapper.readValue(result.toString(), CircleBgPic.class);
                    String picUrl = circleBgPic.getResultData().getPicUrl();
                    ImageLoader.getInstance().displayImage(picUrl, iv_header_pic, AppConfig.CIRCLE_HOME_IMAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode) {
            case UPDATE_PIC:
                ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                try {
                    CLCommentDetailsBean commentDetailsBean = objectMapper.readValue(result.toString(), CLCommentDetailsBean.class);
                    String resultData = commentDetailsBean.getResultData();
                    ToastUtil.showToast(this, resultData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case REQUEST_PIC:
                ObjectMapper objectMapper1 = ObjectMapperFactory.createObjectMapper();
                try {
                    CLCommentDetailsBean commentDetailsBean = objectMapper1.readValue(result.toString(), CLCommentDetailsBean.class);
                    String resultData = commentDetailsBean.getResultData();
                    ToastUtil.showToast(this, resultData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listName[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_album:
                    getAvataFromAlbum();
                    break;
                case R.id.btn_photo_graph:
                    Date date1 = new Date(System.currentTimeMillis());
                    dateTime = date1.getTime() + "";
                    getAvataFromCamera();
                    break;
            }
        }
    };

    protected void getAvataFromAlbum() {
        Intent intent2 = new Intent(Intent.ACTION_PICK);
        intent2.setType("image/*");
        startActivityForResult(intent2, 102);
    }

    protected void getAvataFromCamera() {
        File f = new File(CacheUtils.getCacheDirectory(this, true, "icon")
                + dateTime + "avatar.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(f);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camera, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    String files = CacheUtils.getCacheDirectory(this, true, "icon")
                            + dateTime + "avatar.jpg";
                    File file = new File(files);
                    if (file.exists() && file.length() > 0) {
                        Uri uri = Uri.fromFile(file);
//                        startPhotoZoom(uri);
                        cropRawPhoto(uri);
                    }
                    break;
                case 102:
                    if (data == null) {
                        return;
                    }
//                    startPhotoZoom(data.getData());
                    cropRawPhoto(data.getData());
                    break;
                case 103:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = extras.getParcelable("data");
                            imgPath = saveToSdCard(bitmap);
                            Log.i("temp", "iconUrl---" + imgPath);
                            iv_header_pic.setImageBitmap(bitmap);
                            updateBackground(imgPath);
                        }
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    String path = UCrop.getOutput(data).getPath();
                    updateBackground(path);
                    break;
                case UCrop.RESULT_ERROR:
                    break;
            }
        }
    }

    public void cropRawPhoto(Uri uri) {

        // 修改配置参数（我这里只是列出了部分配置，并不是全部）
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(getResources().getColor(R.color.red_txt));
        // 修改状态栏颜色
        options.setStatusBarColor(getResources().getColor(R.color.red_txt));
        // 隐藏底部工具
        options.setHideBottomControls(true);
        // 图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置图片压缩质量
        options.setCompressionQuality(100);
        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(true);

        // 设置源uri及目标uri
        UCrop.of(uri, Uri.fromFile(new File(PATH, System.currentTimeMillis() + ".jpg")))
                // 长宽比
                .withAspectRatio(1, 1)
                // 图片大小
                .withMaxResultSize(800, 800)
                // 配置参数
                .withOptions(options)
                .start(this);
    }

    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils.getCacheDirectory(this, true, "icon")
                + dateTime + "_11.jpg";
        File file = new File(files);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void updateBackground(String imgPath) {
        if (imgPath != null) {
            FinalHttp http = new FinalHttp();
            AjaxParams params = new AjaxParams();
            String compressUrl = Environment.getExternalStorageDirectory() + File.separator;
            String s = CompressPic.compressDynamicImage(imgPath, compressUrl, 900);
//            String s = CompressPic.compressImage1(imgPath, 100, compressUrl, 1000);
            try {
                params.put("image", new File(s));
                http.post(Config.web.request_uploadImage_information, params, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object t) {
//                        ToastUtil.showToast(ReleaseInformationActivity.this, t.toString());
                        try {
                            UploadPicResult uploadPicResult = getConfiguration().readValue(t.toString(), UploadPicResult.class);
                            String fileName = uploadPicResult.getResultData().getFileName();
                            upDatePic(fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        if (errorNo == 0) {
                        }
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void upDatePic(String fileName) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", getFromSharePreference(Config.userConfig.uid));
        params.put("bgpic", fileName);
        Wethod.httpPost(this, UPDATE_PIC, Config.web.request_update_pic, params, this, View.GONE);
    }

    private void requestBgPic() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", getFromSharePreference(Config.userConfig.uid));
        Wethod.httpPost(this, REQUEST_PIC, Config.web.request_bg_pic, params, this, View.GONE);
    }
}

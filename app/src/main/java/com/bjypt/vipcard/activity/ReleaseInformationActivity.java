package com.bjypt.vipcard.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.adapter.GridViewAdapter;
import com.bjypt.vipcard.adapter.PhotoAdapter;
import com.bjypt.vipcard.base.BaseActivity;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.dialog.IntegralToast;
import com.bjypt.vipcard.dialog.UploadDialog;
import com.bjypt.vipcard.model.CommentDetailsClBean;
import com.bjypt.vipcard.model.IntegralBean;
import com.bjypt.vipcard.model.UploadPicResult;
import com.bjypt.vipcard.tool.CompressPic;
import com.bjypt.vipcard.tool.GlideImageLoader;
import com.bjypt.vipcard.tool.MainConstant;
import com.bjypt.vipcard.utils.BroadCastReceiverUtils;
import com.bjypt.vipcard.utils.LogUtil;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.uploadfile.AsyncUploadFile;
import com.bjypt.vipcard.utils.uploadfile.FormFile;
import com.bjypt.vipcard.view.ToastUtil;
import com.gallerypick.config.GalleryConfig;
import com.gallerypick.config.GalleryPick;
import com.gallerypick.inter.IHandlerCallBack;
import com.sinia.orderlang.utils.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**
 * Created by 崔龙 on 2017/9/27.
 * <p>
 * 图片选择与删除上传界面
 */

public class ReleaseInformationActivity extends BaseActivity implements VolleyCallBack, PhotoAdapter.delPhoto {

    private static final String TAG = "ReleaseInformationActivity";
    private static final int CREATE_TOPIC = 20179281;
    private static final int RESULT_OK_OK = 20171092;
    private static final int REQUEST_INTEGRAL = 784761461;
    public static final int REQUEST_CODE = 200;
    private GalleryConfig galleryConfig;
    private ImageView mAdd;
    private Button mPost;
    private EditText et_content;
    private GridView gridView;
    private LinearLayout ll_finish;
    private GridViewAdapter mGridViewAddImgAdapter; //展示上传的图片的适配器
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private Queue<String> uploadFilePaths = new LinkedList<>();
    private List<UploadPicResult.ResultDataBean> mUploadPicResultItemList = new ArrayList<>();
    private UploadDialog mDialog = new UploadDialog(this);

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_release_information);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mPost = (Button) findViewById(R.id.post);
        mAdd = (ImageView) findViewById(R.id.add_pic);
        gridView = (GridView) findViewById(R.id.gridView);
        ll_finish = (LinearLayout) findViewById(R.id.ll_finish);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void afterInitView() {
        initGallery();
    }

    @Override
    public void bindListener() {
        mPost.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        ll_finish.setOnClickListener(this);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.post:
                mDialog.showLoading();
                if (mPicList.size() == 0 && StringUtil.isEmpty(et_content.getText().toString())) {
                    ToastUtil.showToast(this, "请输入内容再发圈！");
                    mDialog.hideLoading();
                } else {
                    if (mPicList.size() == 0 && StringUtil.isNotEmpty(et_content.getText().toString())) {
                        String title = et_content.getText().toString();
                        Map<String, String> params = new HashMap<>();
//                        params.put("uid", "21");
                        params.put("uid", getFromSharePreference(Config.userConfig.uid));
                        params.put("message", title);
                        Wethod.httpPost(this, CREATE_TOPIC, Config.web.request_create_topic, params, this, View.GONE);
                    } else {
                        sendTiePic();
                    }
                }
                break;
            case R.id.ll_finish:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        switch (reqcode) {
            case CREATE_TOPIC:
                mDialog.hideLoading();
//                ToastUtil.showToast(ReleaseInformationActivity.this, "发布成功！");
                BroadCastReceiverUtils broadCastReceiverUtils = new BroadCastReceiverUtils();
                broadCastReceiverUtils.sendBroadCastReceiver(ReleaseInformationActivity.this, "RESULT_OK");
//                requestIntegral();
                ObjectMapper objectMapper2 = ObjectMapperFactory.createObjectMapper();
                try {
                    IntegralBean integralBean = objectMapper2.readValue(result.toString(), IntegralBean.class);
                    if (StringUtil.isNotEmpty(integralBean.getResultData().getVirtualBalance())) {
                        String resultData = integralBean.getResultData().getVirtualBalance();
                        if (!resultData.equals("0")) {
                            IntegralToast.getIntegralToast().ToastShow(ReleaseInformationActivity.this, null, "发布成功！加" + resultData + "积分");
                            finish();
                        } else {
                            finish();
                        }
                    } else {
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_INTEGRAL:
                break;
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        switch (reqcode) {
            case CREATE_TOPIC:
                mDialog.hideLoading();
                Log.e(TAG, "onFailed:::" + result.toString());
                ToastUtil.showToast(ReleaseInformationActivity.this, "发布失败！");
                break;
            case REQUEST_INTEGRAL:
                ObjectMapper objectMapper2 = ObjectMapperFactory.createObjectMapper();
                try {
                    CommentDetailsClBean commentDetailsClBean = objectMapper2.readValue(result.toString(), CommentDetailsClBean.class);
                    if (StringUtil.isNotEmpty(commentDetailsClBean.getResultData())) {
                        String resultData = commentDetailsClBean.getResultData();
                        ToastUtil.showToast(ReleaseInformationActivity.this, resultData);
                        finish();
                    } else {
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    /**
     * 初始化图片选择器
     */
    private void initGallery() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .pathList(mPicList)                         // 记录已选的图片
                .provider("com.bjypt.vipcard.fileprovider")
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, MainConstant.MAX_SELECT_PIC_NUM - mPicList.size())                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size())                             // 配置多选时 的多选数量。    默认：9
                .crop(true)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();
        initGridView();
    }

    public IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {
        @Override
        public void onStart() {
            Log.e(TAG, "onStart: 开启");
        }

        @Override
        public void onSuccess(List<String> photoList) {
            for (String s : photoList) {
                Log.i(TAG, s);
            }
            mPicList.clear();
            for (String s : photoList) {
                Log.i(TAG, s);
                mPicList.add(s);
            }
            mGridViewAddImgAdapter.notifyDataSetChanged();
            mPicList.size();
        }

        @Override
        public void onCancel() {
            Log.e(TAG, "onCancel: 取消");
        }

        @Override
        public void onFinish() {
            Log.e(TAG, "onFinish: 结束");
        }

        @Override
        public void onError() {
            Log.e(TAG, "onError: 出错");
        }
    };

    @Override
    public void del(int position) {
        mPicList.remove(position);
        mGridViewAddImgAdapter.notifyDataSetChanged();
    }


    /**
     * 打开图片选择器
     */
    private void openGallery() {
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
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }


    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(this, PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }

    //初始化展示上传图片的GridView
    private void initGridView() {
        mGridViewAddImgAdapter = new GridViewAdapter(this, mPicList);
        gridView.setAdapter(mGridViewAddImgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == MainConstant.MAX_SELECT_PIC_NUM) {
                        //最多添加5张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        openGallery();
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 发帖
     */
    private void sendTiePic() {
        uploadFilePaths.addAll(mPicList);
        uploadPic();
    }

    private void uploadPic() {
        // 拿着图片路径上传了
        if (!uploadFilePaths.isEmpty()) {
            String compressUrl = Environment.getExternalStorageDirectory() + File.separator;
            String s = CompressPic.compressDynamicImage(uploadFilePaths.poll(), compressUrl, 900);
            File file = new File(s);
            Map<String, String> params = new HashMap<>();
            FormFile[] files = new FormFile[1];
            params.put("uid", getFromSharePreference(Config.userConfig.uid));
            files[0] = new FormFile(file.getName(), file, "image", "image/jpeg");
            AsyncUploadFile asyncUploadFile = new AsyncUploadFile(new AsyncUploadFile.HttpHandler() {
                @Override
                public void callBackFunction(String result, int requestCode) {
                    LogUtil.debugPrint("uploadPic:" + result);
                    try {
                        UploadPicResult uploadPicResult = getConfiguration().readValue(result.toString(), UploadPicResult.class);
                        mUploadPicResultItemList.add(uploadPicResult.getResultData());
                        uploadPic();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, Config.web.request_release_information, params, files);
            asyncUploadFile.execute("0");
        } else {
            sendTieContent();
        }
    }

    private void sendTieContent() {
        Log.e(TAG, "sendTieContent " + getPicAdis());
        String title = et_content.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("uid", getFromSharePreference(Config.userConfig.uid));
        params.put("message", title);
        params.put("aids", getPicAdis());
        Wethod.httpPost(this, CREATE_TOPIC, Config.web.request_create_topic, params, this, View.GONE);
    }


    private String getPicAdis() {
        StringBuffer aids = new StringBuffer();
        for (int i = 0; i < mUploadPicResultItemList.size(); i++) {
            aids.append(mUploadPicResultItemList.get(i).getAid() + ";");
        }
        if (aids.length() > 0) {
            return aids.substring(0, aids.length() - 1);
        } else {
            return null;
        }
    }

    /**
     * 请求积分
     */
    private void requestIntegral() {
        Map<String, String> param = new HashMap<>();
        param.put("uid", getFromSharePreference(Config.userConfig.uid));
        param.put("action", "share");
        Wethod.httpPost(this, REQUEST_INTEGRAL, Config.web.request_credit, param, this);
    }
}



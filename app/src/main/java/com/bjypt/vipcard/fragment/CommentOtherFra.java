package com.bjypt.vipcard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bjypt.vipcard.R;
import com.bjypt.vipcard.activity.CommentActivity;
import com.bjypt.vipcard.adapter.AssessShowAdapter;
import com.bjypt.vipcard.adapter.CommentShowAdapter;
import com.bjypt.vipcard.base.VolleyCallBack;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.common.Wethod;
import com.bjypt.vipcard.model.CommentBean;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshBase;
import com.bjypt.vipcard.pulltorefresh.PullToRefreshListView;
import com.bjypt.vipcard.umeng.UmengCountContext;
import com.bjypt.vipcard.utils.ObjectMapperFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 涂有泽 .
 * Date by 2016/4/21
 * Use by 其他评论
 */
public class CommentOtherFra extends Fragment implements VolleyCallBack{


    private PullToRefreshListView mCommentAllList;
    private int begin = 0;
    private int pageLength = 10;
    private CommentShowAdapter commentShowAdapter;
    private View view;
    private ImageView iv_default_comment_other_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fra_comment_other, container, false);

        initView(view);
        return view;
    }

    public void initView(View view){
        iv_default_comment_other_pic = (ImageView) view.findViewById(R.id.iv_default_comment_other_pic);
        mCommentAllList = (PullToRefreshListView) view.findViewById(R.id.comment_other);
        mCommentAllList.setMode(PullToRefreshBase.Mode.BOTH);
        Map<String,String> params = new HashMap<>();
        params.put("pkmuser", CommentActivity.commentPkmuser);//商户主键
        params.put("begin",begin+"");//起始位置
        params.put("pageLength",pageLength+"");//查询长度
        params.put("type","1");//查询全部的时候传空
        Wethod.httpPost(getActivity(),1, Config.web.get_comment_list, params, this);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == 1){
            ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
            try {
                CommentBean commentBean = objectMapper.readValue(result.toString(),CommentBean.class);
                if (commentBean.getResultData().size()>0){
                    iv_default_comment_other_pic.setVisibility(View.GONE);
                    AssessShowAdapter assessShowAdapter = new AssessShowAdapter(getActivity(),commentBean.getResultData());
                    mCommentAllList.setAdapter(assessShowAdapter);
                }

            } catch (IOException e) {
                Log.e("eeee", "eeeeee:" + e.toString());
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == 1){
            Wethod.ToFailMsg(getActivity(),result);
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        mCommentAllList.onRefreshComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengCountContext.onPageStart("CommentOtherFra");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengCountContext.onPageEnd("CommentOtherFra");
    }
}

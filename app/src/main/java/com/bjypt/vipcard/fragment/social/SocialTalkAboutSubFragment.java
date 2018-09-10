package com.bjypt.vipcard.fragment.social;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjypt.vipcard.R;
import com.bjypt.vipcard.base.BaseFragment;
import com.wordplat.easydivider.RecyclerViewLinearDivider;

public class SocialTalkAboutSubFragment extends BaseFragment {

    private View view;

    private RecyclerView recyclerView = null;

    private HomeSubAdapter1 adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social_talkabout_sub, container, false);
        return view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);


    }

    @Override
    public void afterInitView() {
        initUI();
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void onClickEvent(View v) {

    }



    private void initUI() {
        adapter = new HomeSubAdapter1(mActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adapter);

        RecyclerViewLinearDivider recyclerViewLinearDivider = new RecyclerViewLinearDivider(mActivity, LinearLayoutManager.VERTICAL);
        recyclerViewLinearDivider.setDividerSize(15);
        recyclerViewLinearDivider.setDividerColor(0xffcccccc);
        recyclerViewLinearDivider.setShowHeaderDivider(false);
        recyclerViewLinearDivider.setShowFooterDivider(false);

        recyclerView.addItemDecoration(recyclerViewLinearDivider);
    }

    public static SocialTalkAboutSubFragment newInstance() {

        Bundle args = new Bundle();

        SocialTalkAboutSubFragment fragment = new SocialTalkAboutSubFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

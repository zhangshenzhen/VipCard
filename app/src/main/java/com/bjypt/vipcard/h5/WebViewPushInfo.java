package com.bjypt.vipcard.h5;

import android.view.View;

import java.io.Serializable;

/**
 * Created by WANG427 on 2016/3/29.
 */
public class WebViewPushInfo implements Serializable{
    public static final int [] DISPLAY = new int[]{0,1};//0 不显示 1显示
    private String url;
    private String title;
    private String action;
    private int btndisplay;
    private String btntext;
    private int btnbg;
    private String btnevent;

    public String getBtnevent() {
        return btnevent;
    }

    public void setBtnevent(String btnevent) {
        this.btnevent = btnevent;
    }

    public int getBtndisplay() {
        return btndisplay;
    }

    public void setBtndisplay(int btndisplay) {
        this.btndisplay = btndisplay;
    }

    public String getBtntext() {
        return btntext;
    }

    public void setBtntext(String btntext) {
        this.btntext = btntext;
    }

    public int getBtnbg() {
        return btnbg;
    }

    public void setBtnbg(int btnbg) {
        this.btnbg = btnbg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

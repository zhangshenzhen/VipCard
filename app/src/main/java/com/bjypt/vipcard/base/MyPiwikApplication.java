package com.bjypt.vipcard.base;

import android.support.multidex.MultiDexApplication;

import com.bjypt.vipcard.activity.shangfeng.util.StringUtils;
import com.bjypt.vipcard.common.Config;
import com.bjypt.vipcard.utils.SharedPreferenceUtils;

import org.piwik.sdk.Piwik;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.TrackerConfig;

public abstract class MyPiwikApplication extends MultiDexApplication {
    private Tracker mPiwikTracker;

    public MyPiwikApplication() {
    }

    public Piwik getPiwik() {
        return Piwik.getInstance(this);
    }

    public synchronized Tracker getTracker() {
        if (this.mPiwikTracker == null) {
            this.mPiwikTracker = this.getPiwik().newTracker(this.onCreateTrackerConfig());
        }
        String phoneno = SharedPreferenceUtils.getFromSharedPreference(getApplicationContext(), Config.userConfig.phoneno);
        if(StringUtils.isNotEmpty(phoneno)){
            mPiwikTracker.setUserId(phoneno);
        }
        return this.mPiwikTracker;
    }

    public abstract TrackerConfig onCreateTrackerConfig();

    public void onLowMemory() {
        if (this.mPiwikTracker != null) {
            this.mPiwikTracker.dispatch();
        }

        super.onLowMemory();
    }

    public void onTrimMemory(int level) {
        if ((level == 20 || level == 80) && this.mPiwikTracker != null) {
            this.mPiwikTracker.dispatch();
        }

        super.onTrimMemory(level);
    }
}

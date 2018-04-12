package com.bjypt.vipcard.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.navi.NaviParaOption;

import java.net.URISyntaxException;

/**
 *  百度地图导航
 *  若手机存在百度app 则会用app导航
 *  若无 则会用浏览器打开百度地图进行导航
 *
 */
public class BaiduUtil {
    /**
     *
     * @param context 上下文对象
     * @param mLat 当前所在的纬度
     * @param mLon 当前所在的经度
     * @param merchantLat 目的地的纬度
     * @param merchantLon 目的地的经度
     * @param adress 当前的地方名称的名称  例如：江苏省南京市雨花台...
     * @param mudi 目的地的名称 例如 江苏省南京市栖霞区...
     */
    public static void Daozheli( Context context,Double mLat,Double mLon,Double merchantLat,Double merchantLon,String adress,String mudi) {

        Intent intent = null;
        try {// 如果有安装百度地图 就启动百度地图
            StringBuffer sbs = new StringBuffer();
            sbs.append("intent://map/direction?origin=latlng:")
                    // 我的位置
                    .append(mLat)
                    .append(",")
                    .append(mLon)
                    .append("|name:")
                    .append("")
                            // 去的位置
                    .append("&destination=latlng:")
                    .append(merchantLat) // 经度
                    .append(",")
                    .append(merchantLon)// 纬度
                    .append("|name:")
                    .append("")
                            // 城市
                    .append("&mode=driving")
                    .append("&coord_type=gcj02")
                    .append("&referer=com.bjypt.vipcard|vipcard#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            try {
                intent = Intent.getIntent(sbs.toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
           context. startActivity(intent);
        } catch (Exception e) {// 没有百度地图则弹出网页端

           String rul = "http://api.map.baidu.com/direction?origin=latlng:"+mLat+","+mLon+"|name:我家&destination=latlng:"+merchantLat+","+merchantLon+"|name:目的&mode=driving&origin_region="+adress+"&destination_region="+mudi+"&output=html&coord_type=gcj02&src=yourCompanyName|yourAppName";
            Uri uri = Uri.parse(rul.toString());
            intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

        }


    }

}

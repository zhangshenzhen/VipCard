package com.bjypt.vipcard.mvp.modle;

import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.bjypt.vipcard.listener.IDataRequestListener;
import com.bjypt.vipcard.model.CommonBeanResult;
import com.bjypt.vipcard.utils.ObjectMapperFactory;
import com.bjypt.vipcard.utils.SsX509TrustManager;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.bjypt.vipcard.base.MyApplication.requestQueue;


/**
 * @author yiw
 * @ClassName: CircleModel
 * @Description: 因为逻辑简单，这里我就不写model的接口了
 * @date 2015-12-28 下午3:54:55
 */
public class CircleModel {


    public CircleModel() {
        //
    }

    public void loadUserInfo(final IDataRequestListener listener, String url, Map<String, String> params){
        requestServer(listener, url, params);
    }

    public void loadData(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }

    public void deleteCircle(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }

    public void addFavort(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }

    public void deleteFavort(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }

    public void addComment(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }

    public void deleteComment(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }
    public void requestRefreshOneItem(final IDataRequestListener listener, String url, Map<String, String> params) {
        requestServer(listener, url, params);
    }

    /**
     * @param listener 设定文件
     * @return void    返回类型
     * @throws
     * @Title: requestServer
     * @Description: 与后台交互, 因为demo是本地数据，不做处理
     */
    private void requestServer(final IDataRequestListener listener, final String url, final Map<String, String> params1) {
        new AsyncTask<Object, Integer, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                RequestFuture<String> future = RequestFuture.newFuture();
                SsX509TrustManager.allowAllSSL();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, future, future) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return params1;
                    }
                };
                requestQueue.add(stringRequest);
                try {
                    String request = future.get();
                    future.get(30000, TimeUnit.SECONDS);//添加请求超时
//					Log.e("------------>","同步"+ request);
                    return request;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object result) {
//				Log.e("------------>", "network  " + url +" " + result);
                if (result != null) {
                    try {
//                        Log.e("------------>", result+"");
                        ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();
                        JavaType type = objectMapper.getTypeFactory().constructParametricType(CommonBeanResult.class, Object.class);
                        CommonBeanResult<Object> commonBeanResult = objectMapper.readValue(result.toString(), type);
                        if(commonBeanResult.getResultStatus().equals("0")){
                            listener.loadSuccess(result);
                        }else{
                            //处理异常情况
                            listener.netWorkError(result);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
//                listener.loadSuccess(result);

            }

        }.execute();
    }
}

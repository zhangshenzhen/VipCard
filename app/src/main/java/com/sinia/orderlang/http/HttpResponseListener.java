package com.sinia.orderlang.http;

import com.sinia.orderlang.bean.AddOrderBean;
import com.sinia.orderlang.bean.AddRedBaoBean;
import com.sinia.orderlang.bean.AddRedBaoExBean;
import com.sinia.orderlang.bean.AddressListList;
import com.sinia.orderlang.bean.CarListBean;
import com.sinia.orderlang.bean.CollectionListList;
import com.sinia.orderlang.bean.OrderDetailBean;
import com.sinia.orderlang.bean.OrderGoodList;
import com.sinia.orderlang.bean.OrderManagerList;
import com.sinia.orderlang.bean.PersonalCenterBean;
import com.sinia.orderlang.bean.RedBaoDetailBean;
import com.sinia.orderlang.bean.RedBaoManagerDetailBean;
import com.sinia.orderlang.bean.RedBaoManagerList;
import com.sinia.orderlang.bean.TeJiaDetailBean;
import com.sinia.orderlang.bean.TeJiaListList;


public interface HttpResponseListener {

	void requestSuccess();

	void requestFailed();

	void requestException();
	/** onFailure----请求失败 */
	void httpRequestFailed();
	
	void getPersonalCenterSuccess(PersonalCenterBean bean);
	
	void getRedBaoDetailSuccess(RedBaoDetailBean bean);
	
	void addCollectionSuccess();
	
	void addCollectionFailed();
	
	void addRedBaoSuccess(AddRedBaoBean bean);

	void addRedBaoFailed(AddRedBaoExBean addRedBaoExBean);
	
	void cancelCollectionSuccess();
	
	void cancelCollectionFailed();
	
	void getRedBaoManagerListSuccess(RedBaoManagerList list);
	
	void getRedBaoManagerDetailSuccess(RedBaoManagerDetailBean bean);
	
	void delRedBaoSuccess();
	
	void delRedBaoFailed();
	
	void getCollectionListSuccess(CollectionListList list);
	
	void getTeJiaListSuccess(TeJiaListList list);
	
	void getTeJiaDetailSuccess(TeJiaDetailBean bean);
	
	void addShopCarSuccess();
	
	void addShopCarFailed();
	
	void getShopCarListSuccess(CarListBean bean);
	
	void getAddressListSuccess(AddressListList list);
	
	void delAddressSuccess();
	
	void delAddressFailed();
	
	void setDefaltAddressSuccess();
	
	void setDefaltAddressFailed();
	
	void addOrderSuccess(AddOrderBean bean);
	
	void getOrderManagerListSuccess(OrderManagerList list);
	
	void getOrderDetailSuccess(OrderDetailBean bean);
	
	void delCollectionSuccess();
	
	void delCollectionFailed();
	
	void getGoodsListSuccess(OrderGoodList list);
}

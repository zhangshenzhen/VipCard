package com.sinia.orderlang.request;

public class DelCollectionRequest extends BaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1018302177260151836L;
	private String collectionId;
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString()+"&collectionId="+collectionId;
	}
}

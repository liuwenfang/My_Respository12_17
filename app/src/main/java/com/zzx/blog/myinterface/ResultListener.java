package com.zzx.blog.myinterface;

/**
 * 网络请求回调接口
 * 
 **/
public interface ResultListener {
	/**
	 * 请求成功
	 * 
	 * @param json
	 */
	public abstract void onSuccess(String json, String msg);

	/**
	 * 请求失败
	 */
	public abstract void onFailure(String msg);

}

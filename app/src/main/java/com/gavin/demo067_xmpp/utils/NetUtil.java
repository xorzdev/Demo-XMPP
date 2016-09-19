package com.gavin.demo067_xmpp.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	private List<OnNetStatusChangeListener> mListeners = new ArrayList<OnNetStatusChangeListener>();

	private static NetUtil mInstance;
	private NetBroadcastReceiver receiver;

	public static NetUtil getInstance() {
		if (null == mInstance) {
			// 只有第一次才彻底执行这里的代码
			synchronized (NetUtil.class) {
				// 再检查一次
				if (null == mInstance)
					mInstance = new NetUtil();
			}
		}
		return mInstance;
	}

	/**
	 * 添加网络状态监听
	 * 
	 * @param listener
	 */
	public void addNetStatusListener(Context context, OnNetStatusChangeListener listener) {
		if (0 == mListeners.size()) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			receiver = new NetBroadcastReceiver();
			context.registerReceiver(receiver, filter);
		}
		mListeners.add(listener);
	}

	/**
	 * 删除网络状态监听
	 */
	public void removeNetStatusListener(Context context, OnNetStatusChangeListener listener) {
		mListeners.remove(listener);
		if (0 == mListeners.size()) {
			context.unregisterReceiver(receiver);
		}
	}

	/**
	 * 获取当前网络信息
	 */
	public static NetworkInfo getNetworkInfo(Context context) {
		if (null != context) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			return mConnectivityManager.getActiveNetworkInfo();
		}
		return null;
	}

	/**
	 * 判断网络是否有效
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo mNetworkInfo = getNetworkInfo(context);
		return null == mNetworkInfo ? false : mNetworkInfo.isAvailable();
	}

	/**
	 * 判断网络是否已连接
	 */
	public static boolean isNetworkConnected(Context context) {
		NetworkInfo mNetworkInfo = getNetworkInfo(context);
		return null == mNetworkInfo ? false : mNetworkInfo.isConnected();
	}

	/**
	 * 获取网络类型
	 * 
	 * @return ConnectivityManager.TYPE_XXX
	 */
	public static int getNetworkType(Context context) {
		NetworkInfo mNetworkInfo = getNetworkInfo(context);
		return null == mNetworkInfo ? -1 : mNetworkInfo.getType(); // ConnectivityManager.TYPE_XXX
	}

	/**
	 * 网络状态广播接收器
	 * 
	 * @author gavin.xiong
	 *
	 */
	private class NetBroadcastReceiver extends BroadcastReceiver {

		private ConnectivityManager mConnectivityManager;
		private NetworkInfo netInfo;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

				mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				netInfo = mConnectivityManager.getActiveNetworkInfo();

				if (null != netInfo && netInfo.isAvailable()) {

					if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
						// TODO WiFi网络
					} else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
						// TODO 有线网络
					} else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
						// TODO 3g网络
					}

				} else {
					// TODO 网络断开
				}

				for (OnNetStatusChangeListener listener : mListeners) {
					listener.onNetChange(netInfo);
				}
			}
		}

	}

	/**
	 * 网格更改监听器
	 * 
	 * @author gavin.xiong
	 *
	 */
	public abstract interface OnNetStatusChangeListener {

		public void onNetChange(NetworkInfo netInfo);
	}
}

package com.gavin.demo067_xmpp.xmpp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gavin.demo067_xmpp.Constants;
import com.gavin.demo067_xmpp.model.IMMessage;
import com.gavin.demo067_xmpp.model.UserInfo;
import com.gavin.demo067_xmpp.utils.L;
import com.gavin.demo067_xmpp.utils.ThreadPoolUtil;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 请求工具类
 * 
 * @ClassName: NetUtil
 * @author yeliangliang
 * @date 2015-7-22 下午4:22:14
 */
public class XMPPManager {

	/**
	 * 登录请求
	 * 
	 * @param context
	 * @param name
	 *            账号
	 * @param pass
	 *            密码
	 * @return
	 * @author yeliangliang
	 * @date 2015-7-22 下午5:28:15
	 * @version V1.0
	 * @return boolean
	 */
	public static void requestLogin(final Context context, final String name,
			final String pass, final NetCallBackListener callBackListener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					// 设置账号、密码、IP、服务器名称、端口
					// 关闭安全验证
					XMPPConnectManager xmppConnectManager = XMPPConnectManager.getInstance();
					XMPPTCPConnectionConfiguration config = xmppConnectManager.getConfiguration(name, pass);
					AbstractXMPPConnection conn2 = xmppConnectManager.setConnection(config);
					conn2.connect();
					L.e("连接服务器成功");
					conn2.login();
					L.e("登录成功");

					// 更改用户状态为 上线 // 要在获取离线消息之后上线
//					Presence presence = new Presence(Presence.Type.available);
//					XMPPConnectManager.getInstance().getConnection().sendStanza(presence);

					// 创建用户对象,保存用户信息
					UserInfo userInfo = new UserInfo(name + "@" + Constants.XMPP.SERVICE_NAME,
							pass, name);
					xmppConnectManager.setUserInfo(userInfo);
					SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
					sp.edit().putString("passWord", pass).putString("passName", name).commit();
					callBackListener.responseSuccess(Constants.XMPP.REQUEST_SUCCESS, null);
					return;
				} catch (SASLErrorException e) {
					e.printStackTrace();
					Log.e("NetUtil.java", "用户名密码错误");
					callBackListener.responseFailed(Constants.XMPP.LOGIN_PASSWORD_ERROR);
				} catch (SmackException e) {
					e.printStackTrace();
					Log.e("NetUtil.java", "登录失败");
					callBackListener.responseFailed(Constants.XMPP.REQUEST_FAILD);
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("NetUtil.java", "登录失败");
					callBackListener.responseFailed(Constants.XMPP.REQUEST_FAILD);
				} catch (XMPPException e) {
					e.printStackTrace();
					Log.e("NetUtil.java", "登录失败");
					callBackListener.responseFailed(Constants.XMPP.REQUEST_FAILD);
				}

			}
		};
		// 将任务添加进线程池
		ThreadPoolUtil.insertTaskToSinglePool(runnable);
	}

	/**
	 * 请求好友列表
	 * 
	 * @param callBackListener
	 * @author yeliangliang
	 * @date 2015-7-27 下午2:10:50
	 * @version V1.0
	 * @return void
	 */
	public static void requestRosterList(
			final NetCallBackListener callBackListener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				XMPPConnectManager connectManager = XMPPConnectManager.getInstance();
				AbstractXMPPConnection connection = connectManager.getConnection();
				if (connection == null) {
					callBackListener.responseFailed(Constants.XMPP.REQUEST_FAILD);
					L.e("连接器为空");
					return;
				}
				Roster roster = Roster.getInstanceFor(connection);
				Collection<RosterEntry> entries = roster.getEntries();
				if (entries == null) {
					callBackListener.responseFailed(Constants.XMPP.REQUEST_FAILD);
					L.e("好友列表返回为空，请求失败");
					return;
				}
				List<RosterEntry> list = new ArrayList<>();
				for (RosterEntry entry : entries) {
					//只有双方都是好友才显示
//					if (entry.getType()==ItemType.both) {
						list.add(entry);
//					}
				}
				
				callBackListener.responseSuccess(Constants.XMPP.REQUEST_SUCCESS, list);
			}
		};
		ThreadPoolUtil.insertTaskToCatchPool(runnable);
	}

	/**
	 * 发送消息
	 * 
	 * @param context
	 *            对方的用户名
	 * @param chatMessage
	 * @param callBackListener
	 * @author yeliangliang
	 * @date 2015-8-3 下午3:11:17
	 * @version V1.0
	 * @return void
	 */
	public static void postMessage(final Context context, final IMMessage chatMessage,
								   final Chat chat, final NetCallBackListener callBackListener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					chat.sendMessage(chatMessage.getContent());
					callBackListener.responseSuccess(Constants.XMPP.REQUEST_SUCCESS, null);
				} catch (NotConnectedException e) {
					callBackListener.responseFailed(Constants.XMPP.REQUEST_FAILD);
					e.printStackTrace();
				}
			}
		};
		ThreadPoolUtil.insertTaskToCatchPool(runnable);
	}

	/**
	 * 搜索好友
	 * 
	 * @author yeliangliang
	 * @date 2015-8-26 下午11:03:42
	 * @version V1.0
	 * @return void
	 */
	public static void searchFriends(final Context context, final String keyStr,
			final NetCallBackListener listener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				UserSearchManager search = new UserSearchManager(XMPPConnectManager.getInstance().getConnection());
				ReportedData data = null;
				try {
					Form searchForm = search.getSearchForm("search."
							+ XMPPConnectManager.getInstance().getConnection().getServiceName());

					Form answerForm = searchForm.createAnswerForm();
					answerForm.setAnswer("Username", true);
					answerForm.setAnswer("Name", true);
					answerForm.setAnswer("search", keyStr);
					data = search.getSearchResults(answerForm, "search."
							+ XMPPConnectManager.getInstance().getConnection().getServiceName());
				} catch (Exception e) {
					listener.responseError(Constants.XMPP.REQUEST_ERROR);
					e.printStackTrace();
				}
				if (data == null) {
					listener.responseFailed(Constants.XMPP.REQUEST_FAILD);
					return;
				}
				listener.responseSuccess(Constants.XMPP.REQUEST_SUCCESS, data.getRows());
			}
		};
		ThreadPoolUtil.insertTaskToCatchPool(runnable);
	}

	/**
	 * 添加好友
	 * 
	 * @author yeliangliang
	 * @date 2015-8-26 下午11:13:34
	 * @version V1.0
	 * @return void
	 */
	public static void addFriends(final Context context, final String name,
			final NetCallBackListener listener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					// 添加好友
					Roster roster = Roster.getInstanceFor(XMPPConnectManager.getInstance().getConnection());
					roster.createEntry(name + "@" + Constants.XMPP.SERVICE_NAME, name,
							new String[] { "Friends" });
					listener.responseSuccess(Constants.XMPP.REQUEST_SUCCESS, null);
				} catch (Exception e) {
					listener.responseFailed(Constants.XMPP.REQUEST_FAILD);
					e.printStackTrace();
				}
			}
		};
		ThreadPoolUtil.insertTaskToCatchPool(runnable);
	}

	/**
	 * 删除好友
	 * 
	 * @author yeliangliang
	 * @date 2015-8-26 下午11:24:17
	 * @version V1.0
	 * @return void
	 */
	public static void deleteFriend(final Context context, final String passName,
			final NetCallBackListener listener) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				Roster roster = Roster.getInstanceFor(XMPPConnectManager.getInstance().getConnection());
				try {
					roster.removeEntry(roster.getEntry(passName));
				} catch (Exception e) {
					listener.responseFailed(Constants.XMPP.REQUEST_FAILD);
					e.printStackTrace();
					return;
				}
				listener.responseSuccess(Constants.XMPP.REQUEST_SUCCESS, null);
			}
		};
		ThreadPoolUtil.insertTaskToCatchPool(runnable);
	}
}

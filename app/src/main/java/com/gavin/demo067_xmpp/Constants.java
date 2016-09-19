package com.gavin.demo067_xmpp;

import android.os.Environment;

import java.io.File;

/**
 * 常量类
 */
public class Constants {

    public static final class IntentExtra {

        /** 用户ID */
        public static final String USER_USER = "userId";
        /** 用户昵称 */
        public static final String USER_NAME = "remarkName";
    }

    public static final class RequestCode {

        /** 拍照 */
        public static final int REQUESTCODE_TAKE_PHOTO = 0X101;
        /** 圖庫 */
        public static final int REQUESTCODE_GALLERY = 0x102;
        /** 裁剪 */
        public static final int REQUESTCODE_ZOOM = 0x103;
        /** 请求必要的权限 */
        public static final int REQUEST_PERMISSION_NEEDFUL = 0x104;
        /** 去设置页更改权限 */
        public static final int REQUEST_PERMISSION_SETTING = 0x105;
    }

    public static final class XMPP {

        /** XMPP服务器地址 */
        public final static String SERVICE_HOST = "192.168.0.201";
        /** XMPP服务器端口 */
        public final static int SERVICE_PORT = 5222;
        /** XMPP服务器别名 */
        public final static String SERVICE_NAME = "xb-201603291439";
        /** 接收消息的权限 */
        public final static String PERMISSION_MESSAGE = "com.gavin.xmpp.permission.RECEIVE_MESSAGE";
        /** 接收到新消息 */
        public final static String RECEIVE_MESSAGE ="com.gavin.xmpp.message";
        /** 接收到通讯录消息 */
        public final static String RECEIVE_FRIENDS ="com.gavin.xmpp.friends";
        /** 消息体 */
        public final static String IM_MESSAGE = "com.gavin.xmpp.imessage";
        /** 聊天记录每页查询条数 */
        public final static int CHAT_LIMIT = 15;
        /** 请求成功 */
        public final static int REQUEST_SUCCESS = -100;
        /** 登录密码错误 */
        public final static int LOGIN_PASSWORD_ERROR = -101;
        /** 请求失败 */
        public final static int REQUEST_FAILD = -102;
        /** 请求失败 */
        public final static int REQUEST_ERROR = -103;
    }

    public static final class General {

        /** 默认图片文件后缀 */
        public static final String IMAGE_END = ".jpg";
        /** 默认编码 */
        public static final String UTF8 = "UTF-8";
        /** 默认时间格式1 */
        public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
        /** 默认时间格式2 */
        public static final String DATEFORMAT2 = "yyyyMMddHHmmss";
        /** 默认时间格式3 */
        public static final String DATEFORMAT3 = "yyyy年MM月dd日";
        /** 数据库名 */
        public static final String DB_NAME = "omg.db";
        /** 默认反页大小 */
        public static final int PAGE_SIZE = 15;
        /** 手机匹配规则 */
        public static final String PHONE_REGEX ="[1][0-9]{10}";
    }

    public static class SDCard {

        /** SDCARD根目录 */
        public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();
        /** 项目文件夹 */
        private static final String STORAGE_DIR = SDCARD + File.separatorChar + "omg" + File.separatorChar;
        /** 图片缓存文件夹 */
        public final static String CACHE = "cache" + File.separatorChar;
        /** 获取缓存目录 */
        public static String getCacheDir() {
            File file = Environment.getExternalStorageDirectory();
            if (file.canWrite()) {
                file = new File(STORAGE_DIR + CACHE);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return STORAGE_DIR + CACHE;
        }
    }


}

package com.example.deti.util;

/**
 * Created by Administrator on 2015/1/16.
 */
public class Global {
    /**
     * web URL prefix
     */
    public static String JPUSH_TITLE = "http://api.jpush.cn:8800/v2/push?";

    public static final String SERVICE_URL = "http://121.41.106.40/Isonoe";
    public static final String LOGIN_URL = SERVICE_URL +"/client/customer/login";
    public static final String GET_USER_IMFORMATION = SERVICE_URL +"/client/customer/getCustomer";
    public static final String GET_FAVORITE_DESIGN = SERVICE_URL + "/client/design/getFavoriteDesign";
    public static final String SHAREDPREFERENCE_URL = "/data/data/com.example.userSetting/shared_prefs/test.xml";
    public static final String GET_FAVORITE_DESIGNER_URL = SERVICE_URL+"/client/user/getFavoriteUser";
    public static final String FOLLOW_DESIGNER_URL = SERVICE_URL +"/client/user/collectUser";
    public static final String CANCEL_FOLLOW_DESIGNER_URL = SERVICE_URL +"/client/user/removeFavorite";
    public static final String GET_DESIGN_LIST_URL = SERVICE_URL + "/client/design/getDesignList";
    public static final String GET_SINGLE_DESIGN_INGO_URL = SERVICE_URL +"/client/design/getDesignInfo";
    //极光推送参数(必填)
    public static String SENDNO = "sendno";
    public static String APP_KEY = "app_key";
    public static String RECEIVER_TYPE = "receiver_type";
    public static String VERIFICATION = "verification_code";
    public static String MSG_TYPE = "msg_type";
    public static String PLATFORM = "platform";
    //内容 String
    public static String MSG_CONTENT = "msg_content";
    public static String KEY_VALUE = "e58e17e58f8b95a746dc73c5";
    public static String APP_MAIN_PASSWORD = "eea9b56f69296d556296f14a";


    //注册
    public final static int MSG_REGISTER = 1;
    //登陆
    public final static int MSG_LOGIN = 2;

    //获取用户列表
    public final static int MSG_GET_USER_IMFORMATION  = 3;
    //请求不成功
    public final static int MSG_REQUEST_WRONG = 4;
    //请求收藏列表
    public final static int MSG_FAVORITE = 5;
    //喜欢的设计师列表
    public final static int MSG_MY_LIKE_DESIGNER = 6;
//取消关注
    public final static int MSG_CANCEL_FOLLOW = 7;
    //关注
    public final static int MSG_FOLLOW = 8;
    //得到某设计师的产品列表
    public final static int MSG_GET_DESIGNER_LIST = 9;
    public final static int MSG_GET_DESIGN_INFO = 10;
    //得到未完成的任务列表
    public final static int MSG_GET_TASK_LIST = 1113;

    //返回当前用户任务

    public final static int MSG_TASK_DETAIL  =1116;
    //接受任务成功
    public final static int MSG_ACCEPT_SUCCESS = 1117;





}

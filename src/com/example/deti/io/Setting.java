package com.example.deti.io;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * tx
 *
 * Created by Administrator on 2015/3/25.
 */
public class Setting {
    private static final String TAG = Setting.class.getSimpleName();

    //用户名，密码
    private String userName ;
    private String password;
    private static Setting instance;
    private int  money ;
    private int achievement;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("avatarUrl", avatarUrl);
        editor.commit();
        this.avatarUrl = avatarUrl;
    }

    private String avatarUrl;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("avatar", avatar);
        editor.commit();
        this.avatar = avatar;
    }

    private String avatar;

    public boolean isChangedAvatar() {
        return isChangedAvatar;
    }

    public void setIsChangedAvatar(boolean isGetAvatar) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isChangedAvatar", isGetAvatar);
        editor.commit();
        this.isChangedAvatar = isGetAvatar;
    }

    private boolean isChangedAvatar;
    //登录返回userId；
    private int userId;

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("fragmentid", fragmentId);
        editor.commit();
        this.fragmentId = fragmentId;
    }

    private int fragmentId =0;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userphone", userPhone);
        editor.commit();
        this.userPhone = userPhone;
    }

    private String userPhone;


    private static SharedPreferences sharedPreferences;

    //初始化在程序一开始启动
    public static void init(Context context){


        if (instance ==null){
            instance = new Setting(context);
        }
    }

//不标准的单例类，由于初始化就生成Instance，后面的context不能获取到。当instance为null，就没办法了吗？

    public static  Setting getInstance(){
        if (instance ==null){
            throw new NullPointerException();

        }
        return instance;
    }
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password",password);
        editor.commit();
        this.password = password;
    }

    public  void setUserMoney( int money){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("money",money);
        editor.commit();
        this.money = money;
    }

    public void setUserId(int userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId",userId);
        editor.commit();
        this.userId = userId;
    }

    public  void setUserAchievement( int achievement){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("achievement",achievement);
        editor.commit();
        this.achievement = achievement;
    }

    public String getUserName() {
        return userName;
    }
    public  int getMoney(){
        return  money;
    }
    public  int getAchievement(){
        return achievement;
    }
    public  int getUserId(){return  userId; }


    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",userName);
        editor.commit();

       this. userName = userName;
    }

//用户名退出后，将用户名默认设置为xxx,密码默认设置为yyy
    public void logOutUser (){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",null);
        editor.putString("password",null);
        editor.commit();
    }

    private void read(){
        userName = sharedPreferences.getString("name",null);
        password = sharedPreferences.getString("password",null);
        money = sharedPreferences.getInt("money", 0);
        achievement = sharedPreferences.getInt("achievement",0);
        userId = sharedPreferences.getInt("userId",0);
        userPhone = sharedPreferences.getString("userphone", null);
        fragmentId = sharedPreferences.getInt("fragmentid", 0);
        isChangedAvatar = sharedPreferences.getBoolean("isChangedAvatar", false);
        avatar = sharedPreferences.getString("avatar", null);
        avatarUrl = sharedPreferences.getString("avatarUrl",null);
    }

    private Setting(Context context){
        sharedPreferences = context.getSharedPreferences("test",
                Context.MODE_PRIVATE);

        read();
    }
}

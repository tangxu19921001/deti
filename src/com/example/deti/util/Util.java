package com.example.deti.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.example.deti.R;

/**
 * create time 2015/1/17
 * Created by Administrator on 2015/1/17.
 */
public class Util {
    /**
     * @param id  the id of the String that you want to show
     * @param con the context show
     */
    public static void showToast(int id, Context con) {
        String msg = con.getResources().getString(id);
        Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
    }

    public static int getWindowWidth(Context con) {
        DisplayMetrics dm = con.getResources().getDisplayMetrics();
        return dm.widthPixels;//宽度height = dm.heightPixels ;//高度
    }

    public static int getWindowHeight(Context con) {
        DisplayMetrics dm = con.getResources().getDisplayMetrics();
        return dm.heightPixels;//宽度height = dm.heightPixels ;//高度
    }
    //不传参数的话就用这个有动画的activity转换
    public static void startActivityAnim(Activity activity, Class cla) {
        Intent intent = new Intent();
        intent.setClass(activity, cla);
        activity.startActivity(intent);
        // 设置切换动画，从右边进入，左边退出
        activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}

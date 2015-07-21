package com.example.deti.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.entity.Person;
import com.example.deti.io.Setting;
import com.example.deti.my.CollectionActivity;
import com.example.deti.my.DesignerLikeActivity;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;
import com.example.deti.widge.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/16.
 */
public class SettingFragment extends Fragment implements IMsgBack {
    private static final String Tag = UserFragment.class.getSimpleName();
    private String msgKey;
    private String userPhone;
    private CircleImageView avatar;
    private TaskThread taskThread = new TaskThread();
    private HashMap<String, String> hashMap;
    private Context context;
    private Handler handler;
    private LinearLayout  styleLikeLayout;
    private LinearLayout designerLikeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.user_detail_layout, null);
        msgKey = DumpMessage.getInstance().RegistryCallback(SettingFragment.this);
        context = this.getActivity();
        init(inflateView);
        userPhone = Setting.getInstance().getUserPhone();
        initHandler();
        return inflateView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hashMap == null) {
            hashMap = new HashMap<String, String>();
        }
        hashMap.put("cellphone", Setting.getInstance().getUserPhone());
        if (Setting.getInstance().isChangedAvatar()||Setting.getInstance().getAvatar()==null){

            taskThread.addTask(new HttpTask(Global.GET_USER_IMFORMATION, Global.MSG_GET_USER_IMFORMATION, context, hashMap));
        }else {
            Message successMsg = handler.obtainMessage();
            successMsg.what = Global.MSG_GET_USER_IMFORMATION;
            successMsg.obj = Setting.getInstance().getAvatar();
            handler.sendMessage(successMsg);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgKey != null) {
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (taskThread!=null){
            taskThread.threadDestroy();
        }
    }

    private void init(View inflateView) {

        avatar = (CircleImageView) inflateView.findViewById(R.id.avatar);
        styleLikeLayout = (LinearLayout)inflateView.findViewById(R.id.style_like_layout);
        styleLikeLayout.setOnClickListener(menuClick);
        designerLikeLayout = (LinearLayout)inflateView.findViewById(R.id.designer_like_layout);
        designerLikeLayout.setOnClickListener(designerClick);
    }
    View.OnClickListener menuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Util.startActivityAnim(getActivity(),CollectionActivity.class);
        }
    };

    View.OnClickListener designerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.startActivityAnim(getActivity(),DesignerLikeActivity.class);
        }
    };
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case Global.MSG_GET_USER_IMFORMATION:

                        ImageSize imageSize = new ImageSize(50, 50);

                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();
                        if (msg.obj == null) {
                            break;

                        }
                        ImageLoader.getInstance().loadImage(msg.obj.toString(), imageSize, options, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                avatar.setImageBitmap(loadedImage);
                            }
                        });


                        break;
                    case Global.MSG_REQUEST_WRONG:

                        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        //start 注册
        if (msgType == Global.MSG_GET_USER_IMFORMATION) {
            JsonParse jsonParse = new JsonParse();
            Person person1 = jsonParse.getPerson(appMessage.getMsg(), Person.class);
            if (person1.getResult() == true) {
                Setting.getInstance().setAvatar(Global.SERVICE_URL+person1.getAvatar());
                successMsg.what = Global.MSG_GET_USER_IMFORMATION;
                successMsg.obj =Global.SERVICE_URL+ person1.getAvatar();
                handler.sendMessage(successMsg);
            } else {
                successMsg.what = Global.MSG_REQUEST_WRONG;
                successMsg.obj = person1.getMessage();
                handler.sendMessage(successMsg);
            }
        }
    }
}
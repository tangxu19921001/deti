package com.example.deti.my;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.deti.R;
import com.example.deti.entity.Custom;
import com.example.deti.entity.Design;
import com.example.deti.entity.Designer;
import com.example.deti.entity.DesignerList;
import com.example.deti.io.Setting;
import com.example.deti.main.DumpMessage;
import com.example.deti.main.MainTabActivity;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;
import com.example.deti.widge.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/7/20.
 */
public class DesignerDetailActivity extends Activity implements IMsgBack{
    private static final String Tag = DesignerDetailActivity.class.getSimpleName();
    private String msgKey;
    private CircleImageView circleImageView;
    private Button getDesignerButton;
    private Designer defaultDesigner;
    private DisplayImageOptions displayImageOptions;
    private ImageLoadingListenerImpl imageLoadingListener;
    private ImageLoader imageLoader;
    private TextView realName;
    private TextView detailCountry;
    private TextView detailCity;
    private Button followButton;
    private TextView favoriteText;
    private TextView designerDescribtion;
    private GridView gridView;
    //存放所有款式的信息
    private List<Design>designList;
    private ImageView imageView;
    private ImageView shoppingCar;
    private boolean isFollowed =true;
    private TaskThread taskThread;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designer_detail_layout);
        msgKey = DumpMessage.getInstance().RegistryCallback(DesignerDetailActivity.this);
        Intent intent = this.getIntent();
        defaultDesigner = (Designer)intent.getSerializableExtra("designer");
        init();
        initHandler();

    }
     private void init(){
         circleImageView = (CircleImageView)findViewById(R.id.detail_designer_avatar);
         getDesignerButton = (Button)findViewById(R.id.get_designer);
         followButton = (Button)findViewById(R.id.follow);
         realName = (TextView)findViewById(R.id.detail_realName);
         detailCountry = (TextView)findViewById(R.id.detail_country);
         favoriteText = (TextView)findViewById(R.id.detail_favorite_number);
         detailCity = (TextView)findViewById(R.id.detail_city);
         gridView = (GridView)findViewById(R.id.grid_list_style);
         designerDescribtion = (TextView)findViewById(R.id.detail_designer_description);
         imageView = (ImageView)findViewById(R.id.dark_title_image_back);
         shoppingCar  = (ImageView)findViewById(R.id.shopping_car);
         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent = new Intent(DesignerDetailActivity.this,GoodsDetailActivity.class);
                 Design design = designList.get(position);
                 if (design!=null){
                     Bundle bundle = new Bundle();
                     bundle.putSerializable("design", design);
                     intent.putExtras(bundle);
                     startActivity(intent);
                 }

             }
         });
         shoppingCar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(DesignerDetailActivity.this, MainTabActivity.class);
                 Setting.getInstance().setFragmentId(3);
                 startActivity(intent);
             }
         });
         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
         int defaultImage = R.drawable.default_image_bg;

         displayImageOptions = new DisplayImageOptions.Builder()
                 .showStubImage(defaultImage)
                 .showImageForEmptyUri(defaultImage)
                 .showImageOnFail(defaultImage)
                 .cacheInMemory(true)
                 .cacheOnDisc(true)
                 .resetViewBeforeLoading()
                 .build();
         imageLoadingListener = new ImageLoadingListenerImpl();
         //如果传进来的默认设计师不为null，就初始化title
    if (this.defaultDesigner!=null){

       if(imageLoader==null){
           imageLoader = ImageLoader.getInstance();
       }
        try{
            imageLoader.displayImage(Global.SERVICE_URL+defaultDesigner.getAvatar(),circleImageView,displayImageOptions,imageLoadingListener);
        }catch (Exception e){
                e.printStackTrace();
            }
        realName.setText(defaultDesigner.getRealname());
        detailCountry.setText(defaultDesigner.getCountry());
        detailCity.setText(defaultDesigner.getCity());
        designerDescribtion.setText(defaultDesigner.getDescription());
        getDesignerButton.setOnClickListener(getDesignerListener);
        followButton.setOnClickListener(followListener);
        favoriteText.setText(defaultDesigner.getFavoriteCount()+"");
        }else {

    }

         if (taskThread==null){
             taskThread = new TaskThread();
         }
         HashMap <String,String >hashMap = new HashMap<>();
         hashMap.put("cellphone",Setting.getInstance().getUserPhone());
         hashMap.put("addUser",defaultDesigner.getUsername());
         hashMap.put("pageSize",10+"");
         taskThread.addTask(new HttpTask(Global.GET_DESIGN_LIST_URL, Global.MSG_GET_DESIGNER_LIST, DesignerDetailActivity.this, hashMap));
     }

    View.OnClickListener getDesignerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + defaultDesigner.getTelephone()));
            startActivity(intent);
        }
    };
    View.OnClickListener followListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if (isFollowed){
               if (taskThread == null) {
                   taskThread = new TaskThread();
               }
               HashMap<String,String> hashMap = new HashMap<>();
               hashMap.put("username",defaultDesigner.getUsername());
               hashMap.put("cellphone", Setting.getInstance().getUserPhone());
               taskThread.addTask(new HttpTask(Global.CANCEL_FOLLOW_DESIGNER_URL,Global.MSG_CANCEL_FOLLOW,DesignerDetailActivity.this,hashMap));
           }else{
               if (taskThread == null) {
                   taskThread = new TaskThread();
               }
               HashMap<String,String> hashMap = new HashMap<>();
               hashMap.put("username",defaultDesigner.getUsername());
               hashMap.put("cellphone", Setting.getInstance().getUserPhone());
               taskThread.addTask(new HttpTask(Global.FOLLOW_DESIGNER_URL,Global.MSG_FOLLOW,DesignerDetailActivity.this,hashMap ));
           }
        }
    };
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case Global.MSG_CANCEL_FOLLOW:
                        followButton.setText(R.string.follow);
                        Toast.makeText(DesignerDetailActivity.this,R.string.cancel_follow_success,Toast.LENGTH_LONG).show();
                        isFollowed = false;
                        break;
                    case Global.MSG_FOLLOW:
                        followButton.setText(R.string.cancel_follow);
                        Toast.makeText(DesignerDetailActivity.this,R.string.follow_success,Toast.LENGTH_LONG).show();
                        isFollowed = true;
                        break;
                    case Global.MSG_GET_DESIGNER_LIST:
                        if (designList!=null){
                            StyleGridAdapter styleAdapter = new StyleGridAdapter(designList,DesignerDetailActivity.this,imageLoader);
                            gridView.setAdapter(styleAdapter);
                        }
                                break;
                    case Global.MSG_REQUEST_WRONG:
                        Toast.makeText(DesignerDetailActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(msgKey!=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (taskThread!=null){
            taskThread.threadDestroy();
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        JsonParse jsonParse = new JsonParse();
        //start ???
        switch (msgType){
            case Global.MSG_CANCEL_FOLLOW:

                Log.i(Tag,appMessage.getMsg());
               DesignerList designerList = jsonParse.getPerson(appMessage.getMsg(), DesignerList.class);

                if (designerList.isResult() == true) {
                    successMsg.what = Global.MSG_CANCEL_FOLLOW;
                    handler.sendMessage(successMsg);
                } else {
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = designerList.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
            case Global.MSG_FOLLOW:

                Log.i(Tag, appMessage.getMsg());
                DesignerList followList = jsonParse.getPerson(appMessage.getMsg(), DesignerList.class);

                if (followList.isResult() == true) {
                    successMsg.what = Global.MSG_FOLLOW;
                    handler.sendMessage(successMsg);
                } else {
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = followList.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
            case Global.MSG_GET_DESIGNER_LIST:
                Log.i(Tag, appMessage.getMsg());
                Custom custom = jsonParse.getPerson(appMessage.getMsg(), Custom.class);

                if (custom.isResult() == true) {
                    successMsg.what = Global.MSG_GET_DESIGNER_LIST;
                    designList = custom.getDesignList();
                    handler.sendMessage(successMsg);
                } else {
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = custom.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
        }


    }
    }


package com.example.deti.main;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.entity.Person;
import com.example.deti.io.Setting;
import com.example.deti.my.AddressManageActivity;
import com.example.deti.my.SelectPicPopWindow;
import com.example.deti.parse.JsonParse;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.*;
import com.example.deti.widge.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2015/7/21.
 */
public class MySettingActivity extends Activity implements IMsgBack {
    private static final String Tag = MySettingActivity.class.getSimpleName();
    private String msgKey;
    //自定义的弹出框类
    SelectPicPopWindow menuWindow;
    private String saveDir = Environment.getExternalStorageDirectory()
            .getPath() + "/temp_image";
    private CircleImageView circleImageView;
    private Bitmap SavedBitmap;
    private File imageFile;
    private final int PHOTO_REQUEST_CUT = 1;
    private final int PHOTO_SELECT=4;
    private TaskThread taskThread;
    private Handler handler;
    private String  picPath;
    StringBuffer resultBuffer = new StringBuffer();
    String resultString;
    File localPicFile;
    private RelativeLayout myAddressRel;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.my_setting_layout);
        msgKey = DumpMessage.getInstance().RegistryCallback(MySettingActivity.this);
        initView();
       initHandler();
    }

    private void initView(){
        circleImageView =(CircleImageView)findViewById(R.id.my_setting_avatar_image);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(R.string.my_setting);
        ImageView backImage = (ImageView)findViewById(R.id.title_image_back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout settingAvatarPop = (RelativeLayout)findViewById(R.id.avatar_re_layout);
        myAddressRel = (RelativeLayout)findViewById(R.id.my_address_layout);
        myAddressRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.startActivityAnim(MySettingActivity.this,AddressManageActivity.class);
            }
        });
        settingAvatarPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopWindow(MySettingActivity.this, itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(MySettingActivity.this.findViewById(R.id.root_setting), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
        if(Setting.getInstance().getAvatar()!=null){
            ImageSize imageSize = new ImageSize(50, 50);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            ImageLoader.getInstance().loadImage(Setting.getInstance().getAvatar(), imageSize, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    circleImageView.setImageBitmap(loadedImage);
                }
            });
        }

    }
    private void initHandler(){
        handler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case Global.MSG_ADD_AVATAR_USER:
                        Util.showToast(R.string.keep_success,MySettingActivity.this);
                        break;
                    case   Global.MSG_REQUEST_WRONG:
                        Toast.makeText(MySettingActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    photo();
                    break;
                // 当选择从本地获取图片时
                case R.id.btn_pick_photo:
                    pickPhoto();
                    break;
                default:
                    break;
            }


        }

    };
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_SELECT);
    }

    private void photo(){
//调用系统拍照功能
        destroyImage();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            imageFile = new File(saveDir, "temp.jpg");
            imageFile.delete();
            if (!imageFile.exists()) {
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MySettingActivity.this, "照片创建失败!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imageFile));
            startActivityForResult(cameraintent,R.id.btn_take_photo);

        } else {
            Toast.makeText(MySettingActivity.this, "sdcard无效或没有插入!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //选择拍照时调用
            case R.id.btn_take_photo:
                startPhotoZoom(Uri.fromFile(imageFile));
                break;
            case PHOTO_REQUEST_CUT:
                    //设置显示在图像中
                    //  sentPicToNext(getPathFromUri(data));
                    if (imageFile != null && imageFile.exists()) {
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inSampleSize = 2;
                        SavedBitmap = BitmapFactory.decodeFile(imageFile.getPath(), option);
                        circleImageView.setImageBitmap(SavedBitmap);
                        Thread  myRun = new Thread(new MyRun());
                        myRun.start();
                    }
                break;
            case PHOTO_SELECT:
                if (data==null){
                    return;
                }
                if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        ContentResolver cr = this.getContentResolver();
                        try {
                          Bitmap  bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                            circleImageView.setImageBitmap(bitmap);
                            saveBitmap2file(bitmap);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    localPicFile = new File(saveDir + "tempPic.jpg");
                    if(localPicFile ==null){
                        return;
                    }
                    Thread  myPicRun = new Thread(new MyPicRun());
                    myPicRun.start();
                    }
        }
    }
    public class MyRun implements Runnable {
        @Override
        public void run() {
            if (uploadFile(imageFile,Global.AVATAR_POST_URL+"?cellphone="+Setting.getInstance().getUserPhone())){
                JsonParse jsonParse = new JsonParse();
                Person person1 = null;
                Message successMsg = handler.obtainMessage();
                try{
                     person1 = jsonParse.getPerson(resultString, Person.class);
                }catch (Exception e){
                    Util.showToast(R.string.accept,MySettingActivity.this);

            }
               if (person1!=null){
                   if (person1.getResult() == true) {
                       Setting.getInstance().setAvatar(Global.SERVICE_URL + person1.getPath());
                   //    Setting.getInstance().setIsChangedAvatar(true);
                       //TODO
                       successMsg.what = Global.MSG_ADD_AVATAR_USER;
                       handler.sendMessage(successMsg);
                       Message message = handler.obtainMessage();
                       message.what = Global.MSG_ADD_AVATAR_USER;
                       handler.sendMessage(message);
                   }
               }
            }

        }
    }

    public class MyPicRun implements Runnable {
        @Override
        public void run() {
            if (uploadFile(localPicFile,Global.AVATAR_POST_URL+"?cellphone="+Setting.getInstance().getUserPhone())){
                JsonParse jsonParse = new JsonParse();
                Person person1 = null;
                Message successMsg = handler.obtainMessage();
                try{
                    person1 = jsonParse.getPerson(resultString, Person.class);
                }catch (Exception e){
                    Util.showToast(R.string.accept,MySettingActivity.this);

                }
                if (person1!=null){
                    if (person1.getResult() == true) {
                        Setting.getInstance().setAvatar(Global.SERVICE_URL + person1.getPath());
                     //   Setting.getInstance().setIsChangedAvatar(true);
                        //TODO
                        successMsg.what = Global.MSG_ADD_AVATAR_USER;
                        handler.sendMessage(successMsg);
                        Message message = handler.obtainMessage();
                        message.what = Global.MSG_ADD_AVATAR_USER;
                        handler.sendMessage(message);
                    }
                }
            }

        }
    }
    public  boolean uploadFile(File file, String RequestURL) {
        try {
            // 创建一个URL对象
            URL url = new URL(RequestURL);

            HashMap<String, File> fileparams = new HashMap<String, File>();
            // 要上传的图片文件

            fileparams.put("file", file);
            // 利用HttpURLConnection对象从网络中获取网页数据
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
            conn.setConnectTimeout(5000);
            // 设置允许输出（发送POST请求必须设置允许输出）
            conn.setDoOutput(true);
            // 设置使用POST的方式发送
            conn.setRequestMethod("POST");
            // 设置不使用缓存（容易出现问题）
            conn.setUseCaches(false);
            conn.setRequestProperty("Charset", "UTF-8");//设置编码
            // 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType
           conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
            OutputStream os = conn.getOutputStream();
            DataOutputStream ds = new DataOutputStream(os);
          //TODO NetUtil.writeStringParams(textParams, ds);
            NetUtil.writeFileParams(fileparams, ds);
            NetUtil.paramsEnd(ds);
            // 对文件流操作完,要记得及时关闭
            os.close();
            // 服务器返回的响应吗
            int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
            // 对响应码进行判断
            if (code == 200) {// 返回的响应码200,是成功
                // 得到网络返回的输入流
                InputStream is = conn.getInputStream();
              resultString = NetUtil.readString(is);
                return true;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void startPhotoZoom(Uri uri){
        Intent intent = new Intent(
                "com.android.camera.action.CROP");

        intent.setDataAndType(uri,"image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    private void destroyImage() {
        if (SavedBitmap != null) {
            SavedBitmap.recycle();
            SavedBitmap = null;
        }
    }
private  boolean saveBitmap2file(Bitmap bmp){
    Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
    int quality = 100;
    OutputStream stream = null;
    try {
        stream = new FileOutputStream(saveDir + "tempPic.jpg");
    } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
        e.printStackTrace();
    }

    return bmp.compress(format, quality, stream);
}
    public static String getPath(Uri uri, Context context) {
        String[] proj = { MediaStore.Images.Media.DATA };
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try{   cursor = cr.query(uri, proj, null, null, null);

    cursor.moveToFirst();
}catch (Exception e){

}


        int actual_image_column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        return cursor.getString(actual_image_column_index);

    }
    private File getFileFromUri(Intent data){

        Uri uri = data.getData();
        picPath= getPath(uri,MySettingActivity.this);
//
//        String[] pojo = { MediaStore.MediaColumns.DATA };
//        File file;
//
//        Cursor cursor = MySettingActivity.this.getContentResolver().query(uri, pojo, null, null, null);
//        if(cursor!=null){
//            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
//            cursor.moveToFirst();
//             picPath = cursor.getString(columnIndex);
//        }
//        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
//            cursor.close();
//        }
        if (picPath!=null&&(picPath.endsWith(".png") ||
                picPath.endsWith(".PNG") ||
        picPath.endsWith(".jpg") ||
        picPath.endsWith(".JPG"))){

        File   file =new File(picPath);
            return file;
        }
        return null;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if (msgKey !=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
        if (taskThread!=null){
            taskThread.threadDestroy();
        }
    }
    @Override
    public void onMsg(AppMessage appMessage) {
        Log.i(Tag, appMessage.toString());
        int msgType = appMessage.getMsgType();
        Message successMsg = handler.obtainMessage();
        //start 注册
        switch (msgType){
            case Global.MSG_ADD_AVATAR_USER:
                JsonParse jsonParse = new JsonParse();
                Person person1 = jsonParse.getPerson(appMessage.getMsg(), Person.class);
                if (person1.getResult() == true) {
                    Setting.getInstance().setAvatar(Global.SERVICE_URL + person1.getPath());
                //    Setting.getInstance().setIsChangedAvatar(true);
                    //TODO
                    successMsg.what = Global.MSG_ADD_AVATAR_USER;

                    handler.sendMessage(successMsg);
                } else {
                    successMsg.what = Global.MSG_REQUEST_WRONG;
                    successMsg.obj = person1.getMessage();
                    handler.sendMessage(successMsg);
                }
                break;
        }

    }
}

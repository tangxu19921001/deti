package com.example.deti.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.deti.R;
import com.example.deti.main.DumpMessage;
import com.example.deti.main.MySettingActivity;
import com.example.deti.widge.CircleImageView;

/**
 * Created by Administrator on 2015/7/22.
 */
public class AddressManageActivity extends Activity   {
    private static final String Tag = MySettingActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.my_setting_layout);

        initView();
        initHandler();
    }
    private void initView(){

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(R.string.address_manage);
        ImageView backImage = (ImageView)findViewById(R.id.title_image_back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

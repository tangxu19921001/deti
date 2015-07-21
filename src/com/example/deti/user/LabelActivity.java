package com.example.deti.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deti.R;
import com.example.deti.main.MainTabActivity;
import com.example.deti.util.SysApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2015/1/20.
 */
public class LabelActivity extends Activity {

    //我打算从配置文件中取
    private ArrayList<Integer> labelItemList;
    LabelLayout labelLayout;
    TextView startTaskText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label_layout);
        SysApplication.getInstance().addActivity(this);
        labelLayout = (LabelLayout) findViewById(R.id.grid_view_pager);
        startTaskText = (TextView) findViewById(R.id.startTask);
        startTaskText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showChoice();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(LabelActivity.this, MainTabActivity.class);
                int backFragment = getIntent().getIntExtra("fragment", 3);
                intent.putExtra("fragment", backFragment);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void showChoice() throws Exception {
        labelItemList = labelLayout.getCheckedLabelList();

        Iterator iterator = labelItemList.iterator();
        String[] checkedLabel = getResources().getStringArray(R.array.label_array);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("你已经选择了：");
        while (iterator.hasNext()) {
            Integer labelIndexInArray = (Integer) iterator.next();
            String labelItem = checkedLabel[labelIndexInArray];
            stringBuffer.append(labelItem).append(",");
        }
        String a = stringBuffer.toString().trim();
        Toast.makeText(LabelActivity.this, a, Toast.LENGTH_LONG).show();
    }


}

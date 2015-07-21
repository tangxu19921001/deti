package com.example.deti.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.deti.R;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;

/**
 * Created by Administrator on 2015/7/16.
 */
public class WomanFragment extends Fragment implements IMsgBack {
    private final static String Tag = WomanFragment.class.getSimpleName();
    private String msgKey;
    private String userPhone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.woman_layout, null);
        msgKey = DumpMessage.getInstance().RegistryCallback(WomanFragment.this);

        return inflateView;
    }

    @Override
    public void onDestroy(){
        if (msgKey!=null){
            DumpMessage.getInstance().UnRegistryCallback(msgKey);
        }
    }
    @Override
    public void onMsg(AppMessage msg) {

    }
}

package com.example.deti.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.deti.R;
import com.example.deti.io.Setting;
import com.example.deti.user.IMsgBack;
import com.example.deti.util.AppMessage;

/**
 * Created by Administrator on 2015/7/14.
 */
public class ShoppingCarFragment extends Fragment implements IMsgBack {
    private static final String Tag = UserFragment.class.getSimpleName();
    private String msgKey;
    private String userPhone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.shopping_car_layout, null);
        msgKey = DumpMessage.getInstance().RegistryCallback(ShoppingCarFragment.this);
        userPhone = Setting.getInstance().getUserPhone();
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

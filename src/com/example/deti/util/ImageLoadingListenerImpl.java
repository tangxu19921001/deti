package com.example.deti.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/20.
 */
public  class ImageLoadingListenerImpl extends SimpleImageLoadingListener {
    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
    @Override
    public void onLoadingComplete(String imageUri,View view,Bitmap bitmap){
        if (bitmap!=null){
            ImageView imageView = (ImageView)view;
            boolean isFirstDisplay = !displayedImages.contains(imageUri);
            if (isFirstDisplay){
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
        }

    }
}
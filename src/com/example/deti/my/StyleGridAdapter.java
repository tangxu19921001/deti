package com.example.deti.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.deti.R;
import com.example.deti.entity.Design;
import com.example.deti.entity.Designer;
import com.example.deti.util.Global;
import com.example.deti.util.ImageLoadingListenerImpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2015/7/20.
 */
public class StyleGridAdapter extends BaseAdapter {
    private List<Design>designList;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ImageLoadingListenerImpl imageLoadingListener;
    public StyleGridAdapter(List<Design>designList,Context context,ImageLoader imageLoader){
        this.designList = designList;
        this.context = context;
        this.imageLoader = imageLoader;
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
    }
    @Override
    public int getCount() {
        if (designList==null){
            return 0;
        }else{
            return designList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.design_image, null, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.design_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (this.designList != null) {
            Design design = this.designList.get(position);
            if (viewHolder.imageView != null) {
                try {
                    imageLoader.displayImage(Global.SERVICE_URL + design.getImage(), viewHolder.imageView, displayImageOptions, imageLoadingListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return convertView;
    }
    private class ViewHolder{
        ImageView imageView;
    }
}

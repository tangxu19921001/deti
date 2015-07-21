package com.example.deti.my;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.deti.R;
import com.example.deti.entity.Designer;
import com.example.deti.util.Global;
import com.example.deti.util.ImageLoadingListenerImpl;
import com.example.deti.widge.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/18.
 */
public class DesignerLikeAdapter extends BaseAdapter{
    private List<Designer>designerList;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ImageLoadingListenerImpl imageLoadingListener;

    public DesignerLikeAdapter(List<Designer> designerList,Context context,ImageLoader imageLoader){
        super();
        this.designerList = designerList;
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
        if (designerList ==null){
            return 0;
        }
        return designerList.size();
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
        ViewHolder holder;

        if (convertView ==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.designer_like_item,null,false);
            holder.imageView = (CircleImageView)convertView.findViewById(R.id.designer_avatar);
            holder.favoriteNumber = (TextView)convertView.findViewById(R.id.favorite_number);
            holder.realName  = (TextView)convertView.findViewById(R.id.realName);
            holder.country = (TextView)convertView.findViewById(R.id.country);
            holder.city = (TextView)convertView.findViewById(R.id.city);
            holder.description = (TextView)convertView.findViewById(R.id.designer_description);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        if (this.designerList!=null){
            Designer designer = this.designerList.get(position);
            if (holder.imageView!=null){
                try{
                    imageLoader.displayImage(Global.SERVICE_URL+designer.getAvatar(),holder.imageView,displayImageOptions,imageLoadingListener);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            holder.favoriteNumber.setText(designer.getFavoriteCount()+"");
            holder.realName.setText(designer.getRealname());
            holder.country.setText(designer.getCountry());
            holder.city.setText(designer.getCity());
            holder.description.setText(designer.getDescription());

        }
        return convertView;
    }

    private class ViewHolder{
        CircleImageView imageView;
        TextView favoriteNumber;
        TextView realName;
        TextView country;
        TextView city;
        TextView description;
    }
}

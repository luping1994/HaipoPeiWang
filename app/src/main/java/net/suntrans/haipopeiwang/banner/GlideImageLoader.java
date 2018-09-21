package net.suntrans.haipopeiwang.banner;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import java.net.HttpCookie;

/**
 * Created by Looney on 2018/9/7.
 * Des:
 */
public class GlideImageLoader extends ImageLoader {
    int width=0;
    int height=0;
    public GlideImageLoader(int width,int height){
        this.width = width;
        this.height = height;
    }
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        //Glide 加载图片简单用法
        Glide.with(context)
                .load(path)
                .override(width,height)
                .into(imageView);

    }
}

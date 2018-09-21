package net.suntrans.haipopeiwang

import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

import android.content.Context
import android.graphics.Bitmap

/**
 * Created by Looney on 2018/9/21.
 * Des:
 */
class KotilnTest {
    fun getData() {
        val context: Context? = null
        Glide.with(context)
                .load("")
                .asBitmap()
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {

                    }
                })
    }
}

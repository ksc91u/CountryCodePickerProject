package ext

import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ViewTarget
import com.caverock.androidsvg.SVG
import svg.SvgDecoder
import svg.SvgDrawableTranscoder
import java.io.InputStream

fun RequestManager.loadWithOption(uri: Uri, view: ImageView): ViewTarget<ImageView, Drawable> {
    var requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .timeout(5 * 1000)
            .skipMemoryCache(false)
            .priority(Priority.HIGH)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        requestOptions = requestOptions.disallowHardwareConfig()
    }
    return this.applyDefaultRequestOptions(requestOptions)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(view)
}

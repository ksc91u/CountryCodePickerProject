package com.hbb20

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.bumptech.glide.*
import com.bumptech.glide.BuildConfig
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient

@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        val memoryCacheSizeBytes = 1024 * 1024 * 20 // 20mb
        val diskCacheSizeBytes = 1024 * 1024 * 100 // 100mb
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
                .setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))
                .setDefaultRequestOptions(defaultRequestOptions())
                .setDefaultTransitionOptions(
                        Bitmap::class.java, BitmapTransitionOptions.withCrossFade(200)
                )
                .setDefaultTransitionOptions(
                        Drawable::class.java, DrawableTransitionOptions.withCrossFade(200)
                )
                .setLogLevel(if (BuildConfig.DEBUG) Log.VERBOSE else Log.ERROR)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        val builder = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                            .newBuilder()
                            .addHeader("Accept-Encoding", "identity")
                            .build()
                    chain.proceed(request)
                }

    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    private fun defaultRequestOptions(): RequestOptions {
        val requestOptions = RequestOptions().format(DecodeFormat.DEFAULT)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .timeout(5 * 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return requestOptions.disallowHardwareConfig()
        }
        return requestOptions
    }
}
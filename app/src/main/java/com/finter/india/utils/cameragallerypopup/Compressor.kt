package com.finter.india.utils.cameragallerypopup

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File


/**
 * Created by Vijay Kumar on 01-05-2017.
 */
class Compressor private constructor(private val context: Context) {
    //max width and height values of the compressed image is taken as 612x816
    private var maxWidth = 612.0f
    private var maxHeight = 816.0f
    private var compressFormat = Bitmap.CompressFormat.JPEG
    private var bitmapConfig = Bitmap.Config.ARGB_8888
    private var quality = 80
    private var destinationDirectoryPath: String
    fun compressToFile(file: File?): File {
        return ImageUtil.compressImage(
            context,
            Uri.fromFile(file),
            maxWidth,
            maxHeight,
            compressFormat,
            bitmapConfig,
            quality,
            destinationDirectoryPath
        )
    }

    fun compressToBitmap(file: File?): Bitmap {
        return ImageUtil.getScaledBitmap(
            context,
            Uri.fromFile(file),
            maxWidth,
            maxHeight,
            bitmapConfig
        )!!
    }

    class Builder(context: Context) {
        private val compressor: Compressor
        fun setMaxWidth(maxWidth: Float): Builder {
            compressor.maxWidth = maxWidth
            return this
        }

        fun setMaxHeight(maxHeight: Float): Builder {
            compressor.maxHeight = maxHeight
            return this
        }

        fun setCompressFormat(compressFormat: Bitmap.CompressFormat): Builder {
            compressor.compressFormat = compressFormat
            return this
        }

        fun setBitmapConfig(bitmapConfig: Bitmap.Config): Builder {
            compressor.bitmapConfig = bitmapConfig
            return this
        }

        fun setQuality(quality: Int): Builder {
            compressor.quality = quality
            return this
        }

        fun setDestinationDirectoryPath(destinationDirectoryPath: String): Builder {
            compressor.destinationDirectoryPath = destinationDirectoryPath
            return this
        }

        fun build(): Compressor {
            return compressor
        }

        init {
            compressor = Compressor(context)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Compressor? = null
        fun getDefault(context: Context): Compressor? {
            if (INSTANCE == null) {
                synchronized(Compressor::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Compressor(context)
                    }
                }
            }
            return INSTANCE
        }
    }

    init {
        destinationDirectoryPath = context.cacheDir.path + File.pathSeparator + FileUtil.FILES_PATH
    }
}
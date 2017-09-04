package com.rxkotlindaggerdemo.commons.extentions

import android.widget.ImageView
import com.squareup.picasso.Picasso
import timber.log.Timber

/**
 * Picasso image loading
 */
fun ImageView.loadImg(imgUrl: String?, placeholder: Int) {
    if (imgUrl.isNullOrEmpty()) {
        Picasso.with(context).load(placeholder).into(this)
    } else {
        Picasso.with(context).load(imgUrl).into(this)
    }
}
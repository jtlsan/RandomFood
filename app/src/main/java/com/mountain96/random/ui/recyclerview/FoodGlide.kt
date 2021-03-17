package com.mountain96.random.ui.recyclerview

import android.app.Activity
import android.view.PixelCopy.request
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_food.view.*

class FoodGlide (val activity: Activity){
    //private val glide : Glide()

    public fun loadImageTo(url : String, view : View) {
        Glide.with(activity).load(url).apply(RequestOptions().circleCrop()).into(view.imageview_food)
    }
}
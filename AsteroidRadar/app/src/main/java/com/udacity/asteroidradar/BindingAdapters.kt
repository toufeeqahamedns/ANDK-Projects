package com.udacity.asteroidradar

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun bindDataToRecyclerView(recyclerView: RecyclerView, asteroid: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(asteroid)
}

@BindingAdapter("pictureOfDay")
fun bindImage(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUrl = imgUrl.toUri().buildUpon().scheme("https").build()

        Picasso.get().load(imgUrl).placeholder(R.drawable.placeholder_picture_of_day)
            .into(imageView)
    }
}

@BindingAdapter("apiStatus")
fun bindApiStatus(progressView: ProgressBar, apiStatus: ApiStatus?) {
    when (apiStatus) {
        ApiStatus.LOADING -> progressView.visibility = VISIBLE
        ApiStatus.ERROR -> progressView.visibility = GONE
        ApiStatus.DONE -> progressView.visibility = GONE
    }
}

@BindingAdapter("apiStatus")
fun bindApiStatus(recyclerView: RecyclerView, apiStatus: ApiStatus?) {
    when (apiStatus) {
        ApiStatus.LOADING -> recyclerView.visibility = GONE
        ApiStatus.ERROR -> recyclerView.visibility = VISIBLE
        ApiStatus.DONE -> recyclerView.visibility = VISIBLE
    }
}
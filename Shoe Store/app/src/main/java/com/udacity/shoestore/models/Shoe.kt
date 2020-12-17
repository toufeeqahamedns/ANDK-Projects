package com.udacity.shoestore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shoe(
    val name: String = "",
    val size: Double? = 0.0,
    val company: String = "",
    val description: String = "",
    val image: String = ""
) : Parcelable
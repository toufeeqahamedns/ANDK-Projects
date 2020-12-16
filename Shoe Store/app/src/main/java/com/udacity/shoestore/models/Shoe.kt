package com.udacity.shoestore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shoe(
    val name: String = "Shoe",
    val size: Double? = 0.0,
    val company: String = "Company",
    val description: String = "Shoe Description",
    val image: String = ""
) : Parcelable
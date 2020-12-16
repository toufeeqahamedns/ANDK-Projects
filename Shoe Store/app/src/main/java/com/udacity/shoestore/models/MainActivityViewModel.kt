package com.udacity.shoestore.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    private val _shoes = MutableLiveData<List<Shoe>>()
    val shoes: LiveData<List<Shoe>>
        get() = _shoes

    init {
        _shoes.value = mutableListOf(Shoe("ABC", 0.0, "XYZ", "LMKOPQ" ))
    }

    fun saveShoe(name: String, size: String, company: String, description: String) {
        val shoeList:List<Shoe> = _shoes.value!!
        _shoes.value = shoeList.plus(Shoe(name, size.toDoubleOrNull(), company, description))
    }
}
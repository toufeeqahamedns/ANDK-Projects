package com.udacity.shoestore.models

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class MainActivityViewModel : ViewModel() {

    private val _shoes = MutableLiveData<List<Shoe>>()
    val shoes: LiveData<List<Shoe>>
        get() = _shoes

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    private val _shoeName = ObservableField<String>("")
    val shoeName: ObservableField<String>
        get() = _shoeName

    private val _shoeSize = ObservableField<String>("")
    val shoeSize: ObservableField<String>
        get() = _shoeSize

    private val _shoeCompany = ObservableField<String>("")
    val shoeCompany: ObservableField<String>
        get() = _shoeCompany

    private val _shoeDesc = ObservableField<String>("")
    val shoeDesc: ObservableField<String>
        get() = _shoeDesc

    init {
        _navigate.value = false
        _shoes.value = mutableListOf(Shoe("ABC", 0.0, "XYZ", "LMKOPQ"))
    }

    fun saveShoe() {
        val shoeList: List<Shoe> = _shoes.value!!
        _shoes.value = shoeList.plus(
            Shoe(
                _shoeName.get().toString(),
                _shoeSize.get()?.toDoubleOrNull(),
                _shoeCompany.get().toString(),
                _shoeDesc.get().toString()
            )
        )
        clearValues()
        _navigate.value = true
    }

    private fun clearValues() {
        _shoeName.set("")
        _shoeSize.set("")
        _shoeCompany.set("")
        _shoeDesc.set("")
    }

    fun navigationDone() {
        _navigate.value = false
    }

    fun navigate() {
        _navigate.value = true
    }
}
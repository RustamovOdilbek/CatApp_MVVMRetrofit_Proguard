package com.exsample.catapp.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exsample.catapp.models.CatResponse
import com.exsample.catapp.repository.MainRepository
import com.exsample.catapp.util.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatFragmentViewModel
@Inject constructor(private val repsitory: MainRepository): ViewModel() {

    private val _catList = MutableStateFlow<UiStateObject<CatResponse>>(UiStateObject.EMPTY)

    val catList = _catList

    fun getCatList() = viewModelScope.launch {
        _catList.value = UiStateObject.LOADING

        try {
            val response = repsitory.getCatList()
            _catList.value = UiStateObject.SUCCESS(response)
        }catch (e: Exception){
            _catList.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }
}
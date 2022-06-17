package com.exsample.catapp.repository

import com.exsample.catapp.api.ApiService
import javax.inject.Inject

class MainRepository
@Inject constructor(private val apiService: ApiService)
{

    suspend fun getCatList() = apiService.getCatList()

}
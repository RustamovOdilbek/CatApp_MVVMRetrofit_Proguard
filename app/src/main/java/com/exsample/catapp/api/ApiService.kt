package com.exsample.catapp.api

import com.exsample.catapp.models.CatResponse
import com.exsample.catapp.util.Constans.BREED_LIST
import retrofit2.http.GET

interface ApiService {

    @GET(BREED_LIST)
    suspend fun getCatList(): CatResponse

}
package com.example.m16.data

import javax.inject.Inject

class UsefulActivitiesRepository @Inject constructor() {

    private var usefulActivity = UsefulActivityDto(
        activity = ""
    )


    suspend fun getUsefulActivity(): UsefulActivityDto {
        usefulActivity = getSearchUsefulApi()
        return usefulActivity
    }


    private suspend fun getSearchUsefulApi(): UsefulActivityDto {

        val response = RetrofitServices.searchUsefulApi.getResultsOfUseful()
        usefulActivity = UsefulActivityDto(
            activity = response.body()!!.activity
        )
        return usefulActivity
    }

}
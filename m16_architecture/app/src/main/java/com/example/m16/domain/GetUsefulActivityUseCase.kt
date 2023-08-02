package com.example.m16.domain

import com.example.m16.data.UsefulActivitiesRepository
import com.example.m16.entity.UsefulActivity
import javax.inject.Inject

class GetUsefulActivityUseCase @Inject constructor(private val usefulActivitiesRepository: UsefulActivitiesRepository) {

    suspend fun execute(): UsefulActivity {
        return usefulActivitiesRepository.getUsefulActivity()
    }

}
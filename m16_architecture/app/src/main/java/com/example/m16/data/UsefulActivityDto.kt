package com.example.m16.data

import com.example.m16.domain.entity.UsefulActivity

data class UsefulActivityDto(
    override val activity: String,
) : UsefulActivity
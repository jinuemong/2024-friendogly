package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.FootprintSaveDto
import com.happy.friendogly.remote.model.response.FootprintSaveResponse

fun FootprintSaveResponse.toData(): FootprintSaveDto {
    return FootprintSaveDto(
        footprintId = id,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
    )
}

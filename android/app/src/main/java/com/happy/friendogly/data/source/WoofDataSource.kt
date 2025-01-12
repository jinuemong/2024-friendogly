package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.FootprintDto
import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.data.model.FootprintMarkBtnInfoDto
import com.happy.friendogly.data.model.FootprintRecentWalkStatusDto
import com.happy.friendogly.data.model.MyFootprintDto
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.FootprintRequest

interface WoofDataSource {
    suspend fun postFootprint(request: FootprintRequest): Result<MyFootprintDto>

    suspend fun patchFootprintRecentWalkStatusAuto(request: FootprintRecentWalkStatusAutoRequest): Result<FootprintRecentWalkStatusDto>

    suspend fun patchFootprintRecentWalkStatusManual(request: FootprintRecentWalkStatusManualRequest): Result<FootprintRecentWalkStatusDto>

    suspend fun getFootprintMarkBtnInfo(): Result<FootprintMarkBtnInfoDto>

    suspend fun getNearFootprints(
        latitude: Double,
        longitude: Double,
    ): Result<List<FootprintDto>>

    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto>

    suspend fun deleteFootprint(footprintId: Long): Result<Unit>
}

package com.happy.friendogly.remote.source

import com.happy.friendogly.data.source.AlarmTokenDataSource
import com.happy.friendogly.remote.api.AlarmTokenService
import com.happy.friendogly.remote.model.request.DeviceTokenRequest

class AlamTokenDataSourceImpl(private val service: AlarmTokenService) : AlarmTokenDataSource {
    override suspend fun saveToken(token: String): Result<Unit> =
        runCatching {
            service.patchDeviceTokens(DeviceTokenRequest(token))
        }
}

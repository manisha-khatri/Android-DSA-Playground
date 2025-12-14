package com.example.systemdesign.interview.sixt.mock
/*

import android.content.Context
import com.example.systemdesign.interview.sixt.ApiService
import com.example.systemdesign.interview.sixt.RecommendationDto
import com.example.systemdesign.interview.sixt.VehicleListDto
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class JsonAssetReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun readJson(fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}

class MockVehicleApiService @Inject constructor(
    private val jsonAssetReader: JsonAssetReader
) : ApiService {

    private val gson = Gson()

    override suspend fun getVehicles(): VehicleListDto {
        val json = jsonAssetReader.readJson("vehicles.json")
        return gson.fromJson(json, VehicleListDto::class.java)
    }

    override suspend fun getRecommendation(): Response<RecommendationDto> {
        val json = jsonAssetReader.readJson("recommendation.json")
        val dto = gson.fromJson(json, RecommendationDto::class.java)
        return Response.success(dto)
    }

}

*/

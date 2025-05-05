package jp.co.zaico.codingtest.api

import jp.co.zaico.codingtest.model.InventoryRequest
import jp.co.zaico.codingtest.model.InventoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ZaicoApi {

    @GET(ApiConfig.ENDPOINT)
    suspend fun getInventories(): List<InventoryResponse>

    @POST(ApiConfig.ENDPOINT)
    suspend fun createInventory(@Body inventoryRequest: InventoryRequest)
}
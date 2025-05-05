package jp.co.zaico.codingtest.repository

import jp.co.zaico.codingtest.api.ZaicoApi
import jp.co.zaico.codingtest.model.ApiResult
import jp.co.zaico.codingtest.model.InventoryRequest
import jp.co.zaico.codingtest.model.InventoryResponse
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val zaicoApi: ZaicoApi
) {

    /**
     * 在庫データ一覧取得
     */
    suspend fun getInventories(): ApiResult<List<InventoryResponse>> {
        return try {
            val inventories = zaicoApi.getInventories()
            ApiResult.Success(inventories)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    /**
     * 在庫データ作成
     */
    suspend fun createInventory(inventoryRequest: InventoryRequest): ApiResult<Unit> {
        return try {
            zaicoApi.createInventory(inventoryRequest)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
}
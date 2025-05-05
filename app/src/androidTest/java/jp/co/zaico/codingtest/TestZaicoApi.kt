package jp.co.zaico.codingtest
import jp.co.zaico.codingtest.api.ZaicoApi
import jp.co.zaico.codingtest.model.InventoryRequest
import jp.co.zaico.codingtest.model.InventoryResponse

class TestZaicoApi : ZaicoApi {
    override suspend fun getInventories(): List<InventoryResponse> {
        return listOf(
            InventoryResponse(id = 1, title = "データ1", quantity = "10"),
            InventoryResponse(id = 2, title = "データ2", quantity = "20")
        )
    }

    override suspend fun createInventory(inventoryRequest: InventoryRequest) {
    }
}

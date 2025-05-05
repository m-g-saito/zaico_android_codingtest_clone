package jp.co.zaico.codingtest.ui.createinventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.zaico.codingtest.model.ApiResult
import jp.co.zaico.codingtest.model.InventoryRequest
import jp.co.zaico.codingtest.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 在庫データ作成画面のViewModel
 */
@HiltViewModel
class CreateInventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    /**
     * 成功メッセージを送信するためのSharedFlow
     */
    private val _successMessage = MutableSharedFlow<String>()
    val successMessage: SharedFlow<String> = _successMessage

    /**
     * エラー発生時のメッセージを通知するためのSharedFlow
     */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage

    /**
     * ローディング状態を管理するStateFlow
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * 在庫データを作成する処理
     * @param inventoryRequest 作成する在庫データのリクエスト情報
     */
    fun createInventoryData(inventoryRequest: InventoryRequest) {
        viewModelScope.launch {
            _isLoading.emit(true)

            when (val result = inventoryRepository.createInventory(inventoryRequest)) {
                is ApiResult.Success -> _successMessage.emit("作成しました")
                is ApiResult.Error -> _errorMessage.emit(
                    result.exception.message ?: "作成に失敗しました"
                )
            }

            _isLoading.emit(false)
        }
    }
}
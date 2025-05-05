package jp.co.zaico.codingtest.ui.inventorylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.zaico.codingtest.model.ApiResult
import jp.co.zaico.codingtest.model.InventoryResponse
import jp.co.zaico.codingtest.repository.InventoryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 在庫一覧画面のViewModel
 * 在庫データの取得やフィルタリング処理を担当
 */
@HiltViewModel
class InventoryListViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    /**
     * UIに公開される在庫一覧の読み取り専用LiveData
     */
    private val _inventories = MutableLiveData<List<InventoryResponse>>()
    val inventories: LiveData<List<InventoryResponse>> = _inventories

    /**
     * エラー発生時のメッセージを通知するためのSharedFlow
     */
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage

    /**
     * APIから取得した全在庫データを一時的に保持するリスト
     * フィルタ処理時に使用
     */
    private val allInventories = mutableListOf<InventoryResponse>()

    /**
     * APIを呼び出して在庫一覧を取得し結果をLiveDataとキャッシュに反映する
     * エラーが発生した場合はエラーメッセージをSharedFlowに送出する
     */
    fun fetchInventories() {
        viewModelScope.launch {
            when (val result = inventoryRepository.getInventories()) {
                is ApiResult.Success -> {
                    allInventories.clear()
                    allInventories.addAll(result.data)
                    _inventories.postValue(result.data)
                }

                is ApiResult.Error -> {
                    val message = result.exception.message ?: "在庫一覧の取得に失敗しました"
                    _errorMessage.emit(message)
                }
            }
        }
    }

    /**
     * クエリ文字列に基づいて在庫一覧をフィルタし結果をLiveDataに設定する
     * 空文字の場合はすべての在庫を再表示する
     *
     * @param query タイトルまたはIDに一致させる検索文字列
     */
    fun filterInventories(query: String) {
        if (query.isBlank()) {
            _inventories.postValue(allInventories)
        } else {
            val filtered = allInventories.filter {
                it.title.contains(query, ignoreCase = true) || it.id.toString().contains(query)
            }
            _inventories.postValue(filtered)
        }
    }
}
package jp.co.zaico.codingtest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryResponse(
    val id: Int,
    val title: String,
    val quantity: String
) : Parcelable
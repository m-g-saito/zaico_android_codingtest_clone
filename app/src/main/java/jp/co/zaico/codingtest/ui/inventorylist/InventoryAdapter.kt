package jp.co.zaico.codingtest.ui.inventorylist

import androidx.recyclerview.widget.ListAdapter
import jp.co.zaico.codingtest.model.InventoryResponse
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import jp.co.zaico.codingtest.databinding.ItemInventoryBinding

/**
 * 在庫一覧を表示するRecyclerView用アダプタ
 * アイテムクリックを外部に通知するリスナーを受け取り各在庫データをバインドする
 *
 * @property listener アイテムタップ時のコールバックを提供するリスナー
 */
class InventoryAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<InventoryResponse, InventoryAdapter.ViewHolder>(InventoryDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(item: InventoryResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInventoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * 在庫アイテムの表示内容を保持するViewHolder
     *
     * @property binding アイテムビューのバインディング
     */
    inner class ViewHolder(
        private val binding: ItemInventoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InventoryResponse) {
            binding.textViewId.text = item.id.toString()
            binding.textViewTitle.text = item.title
            binding.root.setOnClickListener { listener.onItemClick(item) }
        }
    }
}

/**
 * RecyclerViewの差分更新に使用されるDiffUtilコールバック
 * InventoryResponseのIDと内容を比較して変更を検出
 */
class InventoryDiffCallback : DiffUtil.ItemCallback<InventoryResponse>() {

    override fun areItemsTheSame(oldItem: InventoryResponse, newItem: InventoryResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: InventoryResponse,
        newItem: InventoryResponse
    ): Boolean {
        return oldItem == newItem
    }
}
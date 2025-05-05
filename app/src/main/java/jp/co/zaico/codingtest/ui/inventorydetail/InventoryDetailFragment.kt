package jp.co.zaico.codingtest.ui.inventorydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.databinding.FragmentInventoryDetailBinding
import jp.co.zaico.codingtest.model.InventoryResponse

/**
 * 在庫詳細画面
 * 編集・削除機能は割愛
 */
@AndroidEntryPoint
class InventoryDetailFragment : Fragment() {

    private var _binding: FragmentInventoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inventory = InventoryDetailFragmentArgs.fromBundle(requireArguments()).inventory
        bindInventory(inventory)
    }

    private fun bindInventory(inventory: InventoryResponse) {
        binding.textViewId.text = inventory.id.toString()
        binding.textViewTitle.text = inventory.title
        binding.textViewQuantity.text = inventory.quantity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
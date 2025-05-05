package jp.co.zaico.codingtest.ui.inventorylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.databinding.FragmentInventoryListBinding
import jp.co.zaico.codingtest.model.InventoryResponse
import kotlinx.coroutines.launch

/**
 * 在庫一覧画面
 * 検索入力やリスト表示、ナビゲーション処理などを担当
 */
@AndroidEntryPoint
class InventoryListFragment : Fragment() {

    private var _binding: FragmentInventoryListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InventoryListViewModel by viewModels()

    /**
     * 在庫一覧を表示するRecyclerViewのアダプタ
     * アイテムタップ時に詳細画面へ遷移
     */
    private val adapter by lazy {
        InventoryAdapter(object : InventoryAdapter.OnItemClickListener {
            override fun onItemClick(item: InventoryResponse) {
                val action =
                    InventoryListFragmentDirections.actionInventoryListFragmentToInventoryDetailFragment(
                        item
                    )
                findNavController().navigate(action)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 検索文字列の変更に応じて在庫一覧をフィルタリング
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterInventories(newText.orEmpty())
                return true
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = this@InventoryListFragment.adapter
        }

        // 在庫一覧の更新を監視
        viewModel.inventories.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.emptyView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        // エラーメッセージを監視してSnackbarで表示
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect { message ->
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchInventories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
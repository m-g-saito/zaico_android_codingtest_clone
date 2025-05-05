package jp.co.zaico.codingtest.ui.createinventory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jp.co.zaico.codingtest.databinding.ActivityCreateInventoryBinding
import jp.co.zaico.codingtest.model.InventoryRequest
import kotlinx.coroutines.launch

/**
 * 在庫データ作成画面
 * タイトル、数量を必須入力とする
 */
@AndroidEntryPoint
class CreateInventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateInventoryBinding
    private val viewModel: CreateInventoryViewModel by viewModels()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, CreateInventoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            validateInput()?.let { viewModel.createInventoryData(it) }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // ローディング状態に応じてボタンの有効/無効とローディングオーバーレイを更新
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.buttonSubmit.isEnabled = !isLoading
                        binding.loadingOverlay.visibility =
                            if (isLoading) View.VISIBLE else View.GONE
                    }
                }

                // 成功メッセージを受け取った場合にToastを表示しActivityを終了
                launch {
                    viewModel.successMessage.collect {
                        Toast.makeText(this@CreateInventoryActivity, it, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                // エラーメッセージを受け取った場合にSnackbarで表示
                launch {
                    viewModel.errorMessage.collect {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateInput(): InventoryRequest? {
        val title = binding.editTextTitle.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(this, "タイトルを入力してください", Toast.LENGTH_SHORT).show()
            return null
        }

        val quantity = binding.editTextQuantity.text.toString().trim()
        if (quantity.isEmpty()) {
            Toast.makeText(this, "数量を入力してください", Toast.LENGTH_SHORT).show()
            return null
        }

        return InventoryRequest(title = title, quantity = quantity)
    }
}
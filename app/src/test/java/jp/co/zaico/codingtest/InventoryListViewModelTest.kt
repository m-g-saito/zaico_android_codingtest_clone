package jp.co.zaico.codingtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.cash.turbine.test
import jp.co.zaico.codingtest.model.ApiResult
import jp.co.zaico.codingtest.model.InventoryResponse
import jp.co.zaico.codingtest.repository.InventoryRepository
import jp.co.zaico.codingtest.ui.inventorylist.InventoryListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*

import org.mockito.Mockito
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class InventoryListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var mockRepository: InventoryRepository
    private lateinit var viewModel: InventoryListViewModel

    private val mockData = listOf(
        InventoryResponse(1, "データ1", "10"),
        InventoryResponse(2, "データ2", "20")
    )

    private val observedResults = mutableListOf<List<InventoryResponse>?>()
    private lateinit var inventoryObserver: Observer<List<InventoryResponse>>

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = Mockito.mock(InventoryRepository::class.java)
        viewModel = InventoryListViewModel(mockRepository)

        inventoryObserver = Observer { observedResults.add(it) }
        viewModel.inventories.observeForever(inventoryObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.inventories.removeObserver(inventoryObserver)
        observedResults.clear()
    }

    private fun InventoryListViewModel.fetch() {
        fetchInventories()
        dispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun fetchInventoriesSuccess_postsInventoryListToLiveData() = runTest {
        whenever(mockRepository.getInventories()).thenReturn(ApiResult.Success(mockData))
        viewModel.fetch()
        Assert.assertEquals(listOf(mockData), observedResults)
    }

    @Test
    fun fetchInventoriesFailure_emitsErrorMessage() = runTest {
        val errorMsg = "通信エラー"
        whenever(mockRepository.getInventories()).thenReturn(ApiResult.Error(Exception(errorMsg)))

        viewModel.errorMessage.test {
            viewModel.fetch()
            dispatcher.scheduler.advanceUntilIdle()
            Assert.assertEquals(errorMsg, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun filterInventories_queryMatchesOnlyOneItem_postsFilteredList() = runTest {
        whenever(mockRepository.getInventories()).thenReturn(ApiResult.Success(mockData))

        viewModel.fetch()
        viewModel.filterInventories("データ1")
        dispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(listOf(mockData, listOf(mockData[0])), observedResults)
    }

    @Test
    fun filterInventories_emptyQuery_postsFullListAgain() = runTest {
        whenever(mockRepository.getInventories()).thenReturn(ApiResult.Success(mockData))

        viewModel.fetch()
        viewModel.filterInventories("")
        dispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(2, observedResults.size)
        Assert.assertEquals(mockData, observedResults[1])
    }
}
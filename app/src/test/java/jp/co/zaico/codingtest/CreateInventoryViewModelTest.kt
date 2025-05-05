package jp.co.zaico.codingtest

import app.cash.turbine.test
import jp.co.zaico.codingtest.model.ApiResult
import jp.co.zaico.codingtest.model.InventoryRequest
import jp.co.zaico.codingtest.repository.InventoryRepository
import jp.co.zaico.codingtest.ui.createinventory.CreateInventoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class CreateInventoryViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var mockRepository: InventoryRepository
    private lateinit var viewModel: CreateInventoryViewModel

    private val request = InventoryRequest("データ1", "10")

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = Mockito.mock(InventoryRepository::class.java)
        viewModel = CreateInventoryViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun emitsSuccessMessage_whenApiCallSucceeds() = runTest {
        whenever(mockRepository.createInventory(request)).thenReturn(ApiResult.Success(Unit))

        viewModel.successMessage.test {
            viewModel.createInventoryData(request)
            dispatcher.scheduler.advanceUntilIdle()
            Assert.assertEquals("作成しました", awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun emitsErrorMessage_whenApiCallFails() = runTest {
        val errorMsg = "Network error"
        whenever(mockRepository.createInventory(request)).thenReturn(
            ApiResult.Error(Exception(errorMsg))
        )

        viewModel.errorMessage.test {
            viewModel.createInventoryData(request)
            dispatcher.scheduler.advanceUntilIdle()
            Assert.assertEquals(errorMsg, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun loadingStateChangesProperly_duringApiCall() = runTest {
        val request = InventoryRequest("sample title", "10")
        whenever(mockRepository.createInventory(request)).thenReturn(ApiResult.Success(Unit))

        viewModel.isLoading.test {
            Assert.assertEquals(false, awaitItem())
            viewModel.createInventoryData(request)
            Assert.assertEquals(true, awaitItem())
            dispatcher.scheduler.advanceUntilIdle()
            Assert.assertEquals(false, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}
package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.Resource

class FakeShoppingRepositoryAndroidTest : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()
    private val observableAllShoppingItems = MutableLiveData<List<ShoppingItem>>()
    private val observableTotalPrice = MutableLiveData<Float>()
    private var shouldReturnNetworkError = false

    fun setshouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItem(): LiveData<List<ShoppingItem>> {
        return observableAllShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(searchQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError){
            Resource.error("Error", null)
        }else{
            Resource.success(ImageResponse(listOf(), 0, 0))
        }
    }

    private fun refreshLiveData(){
        observableAllShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float{
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }
}
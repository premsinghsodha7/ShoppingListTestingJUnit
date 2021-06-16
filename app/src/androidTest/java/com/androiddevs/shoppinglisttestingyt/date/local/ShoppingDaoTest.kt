package com.androiddevs.shoppinglisttestingyt.date.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDatabase
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.ui.ShoppingFragment
import com.androiddevs.shoppinglisttestingyt.ui.TestShoppingFragmentFactory
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao


    @Inject
    lateinit var testFragmentFactory: TestShoppingFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> (
            fragmentFactory = testFragmentFactory
        ) {

        }
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("Prem", 5, 5f, "url", 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItem).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("Prem", 5, 5f, "url", 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItem = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItem).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("Prem", 2, 10f, "url", 1)
        val shoppingItem2 = ShoppingItem("Zee", 4, 5.5f, "url", 2)
        val shoppingItem3 = ShoppingItem("Janzi", 0, 100f, "url", 3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(
            shoppingItem1.amount * shoppingItem1.price +
                    shoppingItem2.amount * shoppingItem2.price+
                    shoppingItem3.amount * shoppingItem3.price)
    }
}
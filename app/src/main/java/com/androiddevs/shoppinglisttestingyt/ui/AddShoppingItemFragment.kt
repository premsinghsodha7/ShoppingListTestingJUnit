package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import javax.inject.Inject

class AddShoppingItemFragment @Inject constructor(
    val glide: RequestManager,
    var viewModel: ShoppingViewModel? = null
) : Fragment(R.layout.fragment_add_shopping_item) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subcribeToObservers()

        btnAddShoppingItem.setOnClickListener{
            viewModel?.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel?.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subcribeToObservers(){
        viewModel?.curImageURL?.observe(viewLifecycleOwner, Observer {
            glide.load(it).into(ivShoppingImage)
        })

        viewModel?.insertShoppingItemStatus?.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {result ->
                when(result.status){
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireView().rootView,
                            result.message?: "An unknown error occured",
                            Snackbar.LENGTH_LONG
                        )
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView().rootView,
                            "Added shopping item",
                            Snackbar.LENGTH_LONG
                        )
                    }
                    Status.LOADING -> {
                        /* No-OP */
                    }
                }
            }
        })
    }
}
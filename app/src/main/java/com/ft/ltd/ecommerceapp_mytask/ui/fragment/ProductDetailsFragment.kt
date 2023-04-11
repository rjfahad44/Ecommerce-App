package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentProductDetailsBinding
import com.ft.ltd.ecommerceapp_mytask.ui.viewmodel.ApiViewModel
import com.ft.ltd.ecommerceapp_mytask.utils.Utils
import com.ft.ltd.ecommerceapp_mytask.utils.Utils.GlobalProductCartList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val apiViewModel by activityViewModels<ApiViewModel>()
    private var productCartList = ArrayList<ProductsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        initializes()
        return binding.root
    }

    private fun initializes() {
        binding.customToolbar.backButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.customToolbar.title.text = "Product Details"
        binding.customToolbar.addToCart.isVisible = true
        binding.customToolbar.counter.isVisible = true

        lifecycleScope.launch {
            apiViewModel.communicator.collectLatest {
                binding.customToolbar.counter.text = "${it.size}"
            }
        }

        val product = arguments?.getParcelable<ProductsItem>("Product")
        product?.let {
            binding.apply {
                Glide.with(requireContext()).load(it.image).placeholder(R.drawable.product_placeholder_img).centerCrop().into(binding.productImg)
                productName.text = it.title
                productPrice.text = "$ ${it.price}"
                category.text = it.category
                productDetails.text = it.description
                productStockQuantity.text = it.rating.count.toString()
                productRatingBar.rating = it.rating.rate.toFloat()
            }
        }

        binding.goToCartMBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productDetailsFragment_to_cartFragment)
        }

        binding.addToCartMBtn.setOnClickListener {
            product?.let { it1 -> productCartList.add(it1) }
            apiViewModel.setCommunicatorData(productCartList)
            GlobalProductCartList = productCartList
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalProductCartList.clear()
    }
}
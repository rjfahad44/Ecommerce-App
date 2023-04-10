package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentProductDetailsBinding


class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        initializes()
        return binding.root
    }

    private fun initializes() {
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
    }
}
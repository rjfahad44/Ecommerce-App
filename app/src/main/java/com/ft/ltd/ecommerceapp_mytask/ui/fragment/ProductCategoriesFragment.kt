package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.apis.Status
import com.ft.ltd.ecommerceapp_mytask.ui.viewmodel.ApiViewModel
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentProductCategoriesBinding
import com.ft.ltd.ecommerceapp_mytask.ui.adapter.ProductCategoriesAdapter
import com.ft.ltd.ecommerceapp_mytask.utils.hideLoader
import com.ft.ltd.ecommerceapp_mytask.utils.showLoader
import com.ft.ltd.ecommerceapp_mytask.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentProductCategoriesBinding

    private val apiViewModel by activityViewModels<ApiViewModel>()
    private lateinit var productCategoriesAdapter: ProductCategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductCategoriesBinding.inflate(inflater)
        initializes()
        return binding.root
    }

    private fun initializes() {
        binding.customToolbar.backButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.customToolbar.title.text = "Product Categories"

        apiViewModel.getProductCategories()

        productCategoriesAdapter = ProductCategoriesAdapter(::productItemClicked)
        binding.productCategoriesRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productCategoriesAdapter
            setHasFixedSize(true)
        }

        getProductsCategories()
    }

    private fun getProductsCategories() {
        lifecycleScope.launch {
            showLoader()
            apiViewModel.getProductCategories.collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { _data -> productCategoriesAdapter.submitData(items = _data) }
                        hideLoader()
                    }
                    Status.ERROR -> {
                        hideLoader()
                        "${it.error?.message}".toast(requireContext())
                    }
                    else -> {
                        hideLoader()
                    }
                }
            }
        }
    }

    private fun productItemClicked(category: String){
        val bundle = Bundle()
        bundle.putString("product_category", category)
        findNavController().navigate(R.id.action_homeFragment_to_productListFragment, bundle)
    }

}
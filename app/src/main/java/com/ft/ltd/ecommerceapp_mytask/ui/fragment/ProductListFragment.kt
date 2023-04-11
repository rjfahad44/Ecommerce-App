package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.apis.Status
import com.ft.ltd.ecommerceapp_mytask.data.listeners.ProductOnClickListeners
import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentCartBinding
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentProductListBinding
import com.ft.ltd.ecommerceapp_mytask.ui.adapter.ProductListAdapter
import com.ft.ltd.ecommerceapp_mytask.ui.viewmodel.ApiViewModel
import com.ft.ltd.ecommerceapp_mytask.utils.Utils.GlobalProductCartList
import com.ft.ltd.ecommerceapp_mytask.utils.hideLoader
import com.ft.ltd.ecommerceapp_mytask.utils.showLoader
import com.ft.ltd.ecommerceapp_mytask.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment : Fragment(), ProductOnClickListeners {

    private lateinit var binding: FragmentProductListBinding
    private val apiViewModel by activityViewModels<ApiViewModel>()
    private lateinit var productListAdapter: ProductListAdapter
    private var productCartList = ArrayList<ProductsItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(inflater)
        initialize()
        return binding.root
    }

    private fun initialize() {
        binding.customToolbar.backButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.customToolbar.title.text = "Product List"

        binding.customToolbar.addToCart.isVisible = true
        binding.customToolbar.counter.isVisible = true


        apiViewModel.getProducts()

        lifecycleScope.launch {
            apiViewModel.communicator.collectLatest {
                binding.customToolbar.counter.isVisible = true
                binding.customToolbar.counter.text = "${it.size}"
            }
        }

        binding.customToolbar.addToCart.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }

        productListAdapter = ProductListAdapter(this)
        binding.productListRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productListAdapter
            setHasFixedSize(true)
        }

        getProductList()
    }

    private fun getProductList() {
        val productCategory = arguments?.getString("product_category")
        lifecycleScope.launch {
            showLoader()
            apiViewModel.getProducts.collectLatest {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { _data ->
                            val products = _data.filter { it.category == productCategory }
                            productListAdapter.submitData(items = ArrayList(products))
                        }
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

    override fun productDetailsOnClickListener(productsItem: ProductsItem) {
        val bundle = Bundle()
        bundle.putParcelable("Product", productsItem)
        findNavController().navigate(R.id.action_productListFragment_to_productDetailsFragment, bundle)
    }


    override fun addToCartOnClickListener(productsItem: ProductsItem) {
        productCartList.add(productsItem)
        apiViewModel.setCommunicatorData(productCartList)
        GlobalProductCartList = productCartList
    }

    override fun onResume() {
        super.onResume()
        GlobalProductCartList.clear()
    }
}
package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.listeners.CartOnClickListeners
import com.ft.ltd.ecommerceapp_mytask.data.model.ProductsItem
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentCartBinding
import com.ft.ltd.ecommerceapp_mytask.ui.adapter.ProductCartAdapter
import com.ft.ltd.ecommerceapp_mytask.ui.adapter.ProductListAdapter
import com.ft.ltd.ecommerceapp_mytask.ui.viewmodel.ApiViewModel
import com.ft.ltd.ecommerceapp_mytask.utils.Utils
import com.ft.ltd.ecommerceapp_mytask.utils.Utils.GlobalProductCartList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() , CartOnClickListeners {

    private lateinit var binding: FragmentCartBinding
    private val apiViewModel by activityViewModels<ApiViewModel>()
    private var productCartList = ArrayList<ProductsItem>()
    private lateinit var productCartAdapter: ProductCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        initialize()
        return binding.root
    }

    private fun initialize() {
        binding.customToolbar.backButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.customToolbar.title.text = "Product Cart"
        binding.customToolbar.addToCart.isVisible = true
        binding.customToolbar.counter.isVisible = true

        binding.customToolbar.counter.text = "${GlobalProductCartList.size}"

        validationCheck(GlobalProductCartList.isEmpty())

        val price = GlobalProductCartList.sumOf { it.price }


        lifecycleScope.launch {
            apiViewModel.communicator.collectLatest {
                validationCheck(it.isEmpty())
                binding.customToolbar.counter.text = "${it.size}"
            }
        }

        productCartAdapter = ProductCartAdapter(this)
        binding.productCartRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productCartAdapter
            setHasFixedSize(true)
        }

        productCartAdapter.submitData(GlobalProductCartList)
    }

    private fun validationCheck(isCheck: Boolean){
        if (isCheck){
            binding.emptyCart.isVisible = true
            binding.productCartRv.isVisible = false
            binding.checkOutBtn.isClickable = false
            binding.checkOutBtn.isEnabled = false
            binding.checkOutBtn.isFocusable = false
        }else{
            binding.emptyCart.isVisible = false
            binding.productCartRv.isVisible = true
            binding.checkOutBtn.isClickable = true
            binding.checkOutBtn.isEnabled = true
            binding.checkOutBtn.isFocusable = true
        }
    }

    override fun productQuantityMinusOnClickListener(position: Int, productsItem: ProductsItem) {
        productCartAdapter.notifyItemChanged(position)

    }

    override fun productQuantityPlusOnClickListener(position: Int, productsItem: ProductsItem) {
        productCartAdapter.notifyItemChanged(position)
    }

    override fun productDeleteOnClickListener(position: Int, productsItem: ProductsItem) {
        GlobalProductCartList.remove(productsItem)
        productCartAdapter.notifyItemRemoved(position)
        productCartAdapter.notifyItemChanged(position)
        apiViewModel.setCommunicatorData(GlobalProductCartList)
    }
}
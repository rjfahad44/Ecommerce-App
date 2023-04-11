package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
        checkoutCost()

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

        binding.checkOutBtn.setOnClickListener {
            showCheckoutAlertSuccessfully()
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
        GlobalProductCartList[position] = productsItem
        checkoutCost()
    }

    override fun productQuantityPlusOnClickListener(position: Int, productsItem: ProductsItem) {
        productCartAdapter.notifyItemChanged(position)
        GlobalProductCartList[position] = productsItem
        checkoutCost()
    }

    override fun productDeleteOnClickListener(position: Int, productsItem: ProductsItem) {
        GlobalProductCartList.remove(productsItem)
        productCartAdapter.notifyItemRemoved(position)
        productCartAdapter.notifyItemChanged(position)
        apiViewModel.setCommunicatorData(GlobalProductCartList)
        checkoutCost()
    }

    private fun checkoutCost(){
        val price = GlobalProductCartList.sumOf { it.price }
        var shippingCost: Double = 0.0
        if (price>0){
            shippingCost = 100.0
        }else{
            shippingCost = 0.0
        }
        binding.productPriceTv.text = "$ ${String. format("%.1f", price)}"
        binding.shippingCostTv.text = "$ $shippingCost"
        binding.totalPriceTv.text = "$ ${String. format("%.1f", price + shippingCost)}"
    }

    private fun showCheckoutAlertSuccessfully(){
        val bookingAlert = AlertDialog.Builder(requireContext())
        bookingAlert.setTitle("Checkout Successfully.")

        bookingAlert.setMessage("Your Checkout is completed. \nYour Servicing Date is 11–04–2023\n to 20-04-2023.")
        bookingAlert.setIcon(R.drawable.baseline_add_task_24)
        bookingAlert.setPositiveButton("Ok"){dialog, id ->
            //requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            findNavController().navigateUp()
        }

        val alertDialog = bookingAlert.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}
package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ft.ltd.ecommerceapp_mytask.R
import com.ft.ltd.ecommerceapp_mytask.data.apis.Status
import com.ft.ltd.ecommerceapp_mytask.databinding.FragmentProductCategoriesBinding
import com.ft.ltd.ecommerceapp_mytask.ui.adapter.ProductCategoriesAdapter
import com.ft.ltd.ecommerceapp_mytask.ui.viewmodel.ApiViewModel
import com.ft.ltd.ecommerceapp_mytask.utils.hideLoader
import com.ft.ltd.ecommerceapp_mytask.utils.log
import com.ft.ltd.ecommerceapp_mytask.utils.showLoader
import com.ft.ltd.ecommerceapp_mytask.utils.toast
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.security.Permission
import java.security.Permissions

@AndroidEntryPoint
class ProductCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentProductCategoriesBinding

    private val apiViewModel by activityViewModels<ApiViewModel>()
    private lateinit var productCategoriesAdapter: ProductCategoriesAdapter

    private val getDataFroResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedFileUri = result.data?.data
                binding.profileImage.setImageURI(selectedFileUri)
            }
        }


//    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
//        binding.profileImage.setImageURI(imageUri)
//    }
//
//    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
//
//    }


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

        binding.uploadDp.setOnClickListener {
            //galleryLauncher.launch("image/*")
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            getDataFroResult.launch(cameraIntent)
            showImageUploadOptions()
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

    private fun productItemClicked(category: String) {
        val bundle = Bundle()
        bundle.putString("product_category", category)
        findNavController().navigate(R.id.action_homeFragment_to_productListFragment, bundle)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getDataFroResult.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        getDataFroResult.launch(galleryIntent)
    }

    private fun showImageUploadOptions() {
        val mDialog = Dialog(requireContext())
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_image_chooser)
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val galleryLl = mDialog.findViewById<MaterialCardView>(R.id.galleryBtn)
        val cameraLl = mDialog.findViewById<MaterialCardView>(R.id.cameraBtn)

        galleryLl.setOnClickListener {
            openGallery()
            mDialog.dismiss()
        }

        cameraLl.setOnClickListener {
            registerPermission()
            mDialog.dismiss()
        }

        mDialog.setCancelable(true)
        mDialog.show()
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        mDialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun registerPermission() {
        val requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
                granted.map {
                    if (it.value) {
//                        openCamera()
                        it.key.log("PERMISSION")
                    } else {

                    }
                }
            }

        requestCameraPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

}
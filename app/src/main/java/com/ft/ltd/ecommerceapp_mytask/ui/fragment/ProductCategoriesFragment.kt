package com.ft.ltd.ecommerceapp_mytask.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.ft.ltd.ecommerceapp_mytask.BuildConfig
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProductCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentProductCategoriesBinding

    private val apiViewModel by activityViewModels<ApiViewModel>()
    private lateinit var productCategoriesAdapter: ProductCategoriesAdapter
    private val REQUEST_CODE = 101
    private var cameraFilePath: String? = null
    private var cameraFile: String? = null

    private val getDataFroResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedFileUri = result.data?.data
                if (selectedFileUri == null) {
                    cameraFile?.let {
                        "Camera ${it}".log("IMAGE_DATA")
                        binding.profileImage.setImageURI(it.toUri())
                    }
                } else {
                    binding.profileImage.setImageURI(selectedFileUri)
                }
                "${selectedFileUri}".log("IMAGE_DATA")
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            if (granted.all { it.value }) {
                openCamera()
            } else {
                "permission denied".toast(requireContext())
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        cameraIntent.putExtra("REQUEST_CODE", REQUEST_CODE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createImageFile()?.let {
            FileProvider.getUriForFile(
                requireContext(), BuildConfig.APPLICATION_ID + ".provider", it
            )
        })
        //startActivityForResult(cameraIntent, 1002)
        getDataFroResult.launch(cameraIntent)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera"
        )
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        cameraFilePath = "file://" + image.absolutePath
        cameraFile = image.absolutePath
        return image
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
            requestCameraPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            mDialog.dismiss()
        }

        mDialog.setCancelable(true)
        mDialog.show()
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        mDialog.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}
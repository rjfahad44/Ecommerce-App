package com.ft.ltd.ecommerceapp_mytask.utils

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ft.ltd.ecommerceapp_mytask.R


fun String.log(key: String = "LOG") {
    Log.e(key, this)
}

fun String.toast(context: Context, show: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, show).show()
}

private var loaderDialog: Dialog? = null
fun Fragment.showLoader() {
    loaderDialog = Dialog(requireActivity())
    loaderDialog?.requestWindowFeature(1)
    loaderDialog?.setContentView(R.layout.dialog_loader_view)
    loaderDialog?.setCanceledOnTouchOutside(false)
    loaderDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    loaderDialog?.show()

    if (loaderDialog?.isShowing == true) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (loaderDialog?.isShowing == true) {
                loaderDialog?.dismiss()
            }
        }
        loaderDialog?.setOnDismissListener {
            handler.removeCallbacks(runnable)
        }
        handler.postDelayed(runnable, 90000)
    }
}

fun Fragment.hideLoader() {
    loaderDialog?.dismiss()
}
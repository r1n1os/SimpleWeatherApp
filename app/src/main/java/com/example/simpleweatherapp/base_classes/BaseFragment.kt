package com.example.simpleweatherapp.base_classes

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.simpleweatherapp.R

abstract class BaseFragment<T: BaseViewModel>: Fragment() {

    protected lateinit var viewModel: T
    protected abstract fun initViewModel(): Class<T>

    private var progressDialog: AlertDialog? = null
    private var alertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(initViewModel())
    }

    protected fun showToast(message: String){
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected fun showProgressDialog() {
        val view = LayoutInflater.from(this.requireContext()).inflate(R.layout.layout_progress_dialog, null)
        hideProgressDialog()
        activity?.runOnUiThread {
            progressDialog = AlertDialog.Builder(this.requireContext())
                    .setTitle(null)
                    .setView(view)
                    .setCancelable(false)
                    .create()
            progressDialog!!.show()
        }
    }

    protected fun hideProgressDialog() {
        if (progressDialog != null)
            progressDialog!!.dismiss()
    }

    protected fun showAlertDialog(title: String?, message: String?, dialogInterface: DialogInterface.OnClickListener?) {
        hideAlertDialog()
        activity?.runOnUiThread {
            alertDialog = AlertDialog.Builder(this.requireContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.ok_text), dialogInterface)
                    .setCancelable(false)
                    .create()
            alertDialog!!.show()
        }
    }

    protected fun hideAlertDialog() {
        if (alertDialog != null)
            alertDialog!!.dismiss()
    }
}
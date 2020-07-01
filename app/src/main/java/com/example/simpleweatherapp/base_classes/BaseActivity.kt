package com.example.simpleweatherapp.base_classes

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.simpleweatherapp.R

abstract class BaseActivity<T: BaseViewModel>: AppCompatActivity() {

    protected lateinit var viewModel: T
    protected abstract fun initViewModel(): Class<T>
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(initViewModel())
    }

    protected fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    protected fun showProgressDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_progress_dialog, null)
        hideProgressDialog()
        runOnUiThread {
            progressDialog = AlertDialog.Builder(this)
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

}
package com.example.simpleweatherapp.base_classes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

abstract class BaseActivity<T: BaseViewModel>: AppCompatActivity() {

    protected lateinit var viewModel: T
    protected abstract fun initViewModel(): Class<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(initViewModel())
    }

    protected fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
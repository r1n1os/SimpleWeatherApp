package com.example.simpleweatherapp.base_classes

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

abstract class BaseFragment<T: BaseViewModel>: Fragment() {

    protected lateinit var viewModel: T
    protected abstract fun initViewModel(): Class<T>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(initViewModel())
    }

    protected fun showToast(message: String){
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
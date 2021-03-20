package com.goodfood.app.ui.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.goodfood.app.BR
import com.goodfood.app.R
import com.goodfood.app.interfaces.Navigable

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
        binding.setVariable(BR.viewModel, viewModel)
        setContentView(binding.root)
        addNavigationObserver()
        setObservers()
    }

    private fun addNavigationObserver() {
        viewModel.screenToNav.observe(this, Observer { navigable ->
            navigateTo(navigable)
        })
    }

    abstract fun navigateTo(navigable: Navigable)
    abstract fun setUp()
    abstract fun setObservers()

}
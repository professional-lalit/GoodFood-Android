package com.goodfood.app.ui.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewbinding.ViewBinding
import com.goodfood.app.BR
import com.goodfood.app.R
import com.goodfood.app.common.Constants
import com.goodfood.app.common.Prefs
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.Message
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.login.LoginActivity
import com.goodfood.app.utils.Extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            unAuthenticationReceiver,
            IntentFilter(Constants.UNAUTHENTICATED)
        )
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(unAuthenticationReceiver)
    }

    abstract fun navigateTo(navigable: Navigable)
    abstract fun setUp()
    abstract fun setObservers()

    open fun onUnAuthenticated() {
        if (this !is LoginActivity) {
            LoginActivity.openScreen(this)
            finishAffinity()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onClickEvent(event: ClickEventMessage) {
    }

    @Subscribe
    open fun onEvent(event: Message) {
    }

    private val unAuthenticationReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            onUnAuthenticated()
        }
    }


}
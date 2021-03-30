package com.goodfood.app.ui.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityForgotPasswordBinding
import com.goodfood.app.databinding.ActivitySignupBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.signup.SignupActivity
import com.goodfood.app.ui.signup.SignupViewModel

class ForgotPasswordActivity :
    BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel>() {

    companion object {
        fun <B : ViewDataBinding, VM : BaseViewModel, T : BaseActivity<B, VM>> openScreen(
            activity: T,
            bundle: Bundle? = null
        ) {
            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            bundle?.let { intent.putExtras(bundle) }
            activity.startActivity(intent)
        }
    }

    private val forgotPasswordViewModel by lazy {
        ViewModelProvider(this).get(
            ForgotPasswordViewModel::class.java
        )
    }

    override fun navigateTo(navigable: Navigable) {

    }

    override fun setUp() {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        viewModel = forgotPasswordViewModel
    }

    override fun setObservers() {

    }

    override fun onClickEvent(event: ClickEventMessage) {

    }
}
package com.goodfood.app.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityLoginBinding
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.forgot_password.ForgotPasswordActivity
import com.goodfood.app.ui.signup.SignupActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private val loginViewModel by lazy {
        ViewModelProvider(this)
            .get(LoginViewModel::class.java)
    }

    override fun setUp() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = loginViewModel
    }

    override fun navigateTo(navigable: Navigable) {
        when (navigable) {
            BaseViewModel.LoginNav.SIGNUP -> SignupActivity.openScreen(this)
            BaseViewModel.LoginNav.FORGOT_PWD -> ForgotPasswordActivity.openScreen(this)
        }
    }

    override fun setObservers() {

    }

}
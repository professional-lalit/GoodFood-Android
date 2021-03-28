package com.goodfood.app.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityHomeBinding
import com.goodfood.app.databinding.HomeDrawerHeaderBinding
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.common.dialogs.DialogManager
import com.goodfood.app.ui.login.LoginActivity
import com.goodfood.app.utils.Extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var drawerHeaderBinding: HomeDrawerHeaderBinding

    companion object {

        fun <B : ViewDataBinding, VM : BaseViewModel, T : BaseActivity<B, VM>> openScreen(
            activity: T,
            bundle: Bundle? = null
        ) {
            val intent = Intent(activity, HomeActivity::class.java)
            bundle?.let { intent.putExtras(bundle) }
            activity.startActivity(intent)
        }

    }

    @Inject
    lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMeDetails()
        setUpActionBar()
        setViews()
    }

    private fun setUpActionBar() {
        binding.toolbarHome.title = getString(R.string.explore)
        setSupportActionBar(binding.toolbarHome)
    }

    private fun setViews() {
        binding.navHome.setNavigationItemSelectedListener {
            if (it.itemId == R.id.action_logout) {
                showLogoutDialog()
            }
            binding.drawerHome.closeDrawer(GravityCompat.START, true)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun showLogoutDialog() {
        dialogManager.showLogoutDialog(supportFragmentManager) { isToBeLoggedOut ->
            if (isToBeLoggedOut) {
                viewModel.logout()
            }
        }
    }

    override fun navigateTo(navigable: Navigable) {
        when (navigable) {
            BaseViewModel.Navigables.LOGIN -> {
                LoginActivity.openScreen(this)
                finish()
            }
        }
    }

    override fun setUp() {
        viewModel = homeViewModel
        binding = ActivityHomeBinding.inflate(layoutInflater)
        drawerHeaderBinding = DataBindingUtil.bind(binding.navHome.getHeaderView(0))!!
    }

    override fun setObservers() {
        homeViewModel.errorData.observe(this, {
            showToast(it.message)
        })
        homeViewModel.user.observe(this, { user ->
            drawerHeaderBinding.user = user
        })
    }

}
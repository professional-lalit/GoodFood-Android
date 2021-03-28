package com.goodfood.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import com.goodfood.app.ui.home.fragments.ExploreFragment
import com.goodfood.app.ui.home.fragments.MyRecipesFragment
import com.goodfood.app.ui.home.fragments.PaymentHistoryFragment
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
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setViews() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.fragments.size > 2) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
            } else if (supportFragmentManager.fragments.size == 2) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
            }
        }
        binding.navHome.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_logout -> showLogoutDialog()
                R.id.action_explore -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ExploreFragment())
                        .commit()
                }
                R.id.action_my_recipes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MyRecipesFragment())
                        .commit()
                }
                R.id.action_payment_history -> {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, PaymentHistoryFragment())
                        .addToBackStack(PaymentHistoryFragment.TAG)
                        .commit()
                }
            }
            binding.drawerHome.closeDrawer(GravityCompat.START, true)
            return@setNavigationItemSelectedListener true
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.container, ExploreFragment())
            .addToBackStack(ExploreFragment.TAG)
            .commit()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                handleBackLogic()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        handleBackLogic()
    }

    private fun handleBackLogic() {
        if (supportFragmentManager.fragments.size == 2) {
            if (!binding.drawerHome.isDrawerOpen(GravityCompat.START)) {
                binding.drawerHome.openDrawer(GravityCompat.START)
            }
        } else if (supportFragmentManager.fragments.size > 2) {
            val fragment =
                supportFragmentManager.fragments[supportFragmentManager.fragments.size - 2]
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
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
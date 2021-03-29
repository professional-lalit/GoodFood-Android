package com.goodfood.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.goodfood.app.R
import com.goodfood.app.databinding.ActivityHomeBinding
import com.goodfood.app.databinding.HomeDrawerHeaderBinding
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.common.dialogs.DialogManager
import com.goodfood.app.ui.home.fragments.ExploreFragment
import com.goodfood.app.ui.home.fragments.MyRecipesFragment
import com.goodfood.app.ui.home.fragments.PurchasedRecipesFragment
import com.goodfood.app.ui.login.LoginActivity
import com.goodfood.app.utils.Extensions.showToast
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

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

    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var drawerHeaderBinding: HomeDrawerHeaderBinding

    @Inject
    lateinit var dialogManager: DialogManager

    private val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getMeDetails()
        setUpActionBar()
        setViews()
        setUpFragNav(savedInstanceState)
    }

    private fun setUpActionBar() {
        binding.toolbarHome.title = getString(R.string.explore)
        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setViews() {
        fragNavController.transactionListener = object : FragNavController.TransactionListener {
            override fun onFragmentTransaction(
                fragment: Fragment?,
                transactionType: FragNavController.TransactionType
            ) {
                when (fragNavController.currentFrag) {
                    is ExploreFragment -> supportActionBar?.title = getString(R.string.explore)
                    is MyRecipesFragment -> supportActionBar?.title = getString(R.string.my_recipes)
                    is PurchasedRecipesFragment -> supportActionBar?.title =
                        getString(R.string.purchased_recipes)
                }
                if (fragNavController.isRootFragment) {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
                } else {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
                }
            }

            override fun onTabTransaction(fragment: Fragment?, index: Int) {

            }
        }

        binding.navHome.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_logout -> showLogoutDialog()
                R.id.action_explore -> {
                    fragNavController.switchTab(0)
                    supportActionBar?.title = getString(R.string.explore)
                }
                R.id.action_my_recipes -> {
                    fragNavController.switchTab(1)
                    supportActionBar?.title = getString(R.string.my_recipes)
                }
                R.id.action_purchased_recipes -> {
                    fragNavController.pushFragment(PurchasedRecipesFragment())
                }
            }
            binding.drawerHome.closeDrawer(GravityCompat.START, true)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpFragNav(savedInstanceState: Bundle?) {
        val fragments = listOf(
            ExploreFragment(),
            MyRecipesFragment()
        )
        fragNavController.rootFragments = fragments
        fragNavController.initialize(0, savedInstanceState)
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
        if (fragNavController.isRootFragment) {
            if (!binding.drawerHome.isDrawerOpen(GravityCompat.START)) {
                binding.drawerHome.openDrawer(GravityCompat.START)
            }
        } else {
            fragNavController.popFragment()
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
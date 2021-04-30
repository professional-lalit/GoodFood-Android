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
import com.goodfood.app.common.CustomApplication
import com.goodfood.app.common.multimedia_managers.RecipeMultimediaManager
import com.goodfood.app.databinding.ActivityHomeBinding
import com.goodfood.app.databinding.HomeDrawerHeaderBinding
import com.goodfood.app.events.ClickEventMessage
import com.goodfood.app.events.EventConstants
import com.goodfood.app.events.Message
import com.goodfood.app.interfaces.Navigable
import com.goodfood.app.models.domain.Recipe
import com.goodfood.app.ui.common.BaseActivity
import com.goodfood.app.ui.common.BaseViewModel
import com.goodfood.app.ui.common.dialogs.DialogManager
import com.goodfood.app.ui.home.fragments.*
import com.goodfood.app.ui.home.fragments.create_recipe.CreateRecipeFragment
import com.goodfood.app.ui.home.fragments.explore.ExploreFragment
import com.goodfood.app.ui.home.fragments.my_recipes.MyRecipesFragment
import com.goodfood.app.ui.home.fragments.recipe_detail.RecipeDetailFragment
import com.goodfood.app.ui.login.LoginActivity
import com.goodfood.app.utils.Extensions.showToast
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    private var backPressTime: Long = 0L

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var recipeMultimediaManager: RecipeMultimediaManager

    private val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar()
        setViews()
        setUpFragNav(savedInstanceState)
        viewModel.getMeDetails()
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
                updateToolbarTitle()
                if (fragNavController.isRootFragment) {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
                } else {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)
                }
            }

            override fun onTabTransaction(fragment: Fragment?, index: Int) {
                updateToolbarTitle()
            }
        }

        binding.navHome.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_explore ->
                    fragNavController.switchTab(BaseViewModel.Navigables.EXPLORE.index!!)
                R.id.action_my_recipes ->
                    fragNavController.switchTab(BaseViewModel.Navigables.MY_RECIPES.index!!)
                R.id.action_saved_recipes ->
                    fragNavController.switchTab(BaseViewModel.Navigables.SAVED_RECIPES.index!!)
                R.id.action_purchased_recipes ->
                    fragNavController.switchTab(BaseViewModel.Navigables.PURCHASED_RECIPES.index!!)
                R.id.action_payment_history ->
                    fragNavController.switchTab(BaseViewModel.Navigables.PAYMENT_HISTORY.index!!)
                R.id.action_complaints ->
                    fragNavController.switchTab(BaseViewModel.Navigables.COMPLAINTS.index!!)
                R.id.action_logout -> showLogoutDialog()
            }
            binding.drawerHome.closeDrawer(GravityCompat.START, true)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setUpFragNav(savedInstanceState: Bundle?) {
        val fragments = listOf(
            ExploreFragment.newInstance(),
            MyRecipesFragment.newInstance(),
            SavedRecipesFragment.newInstance(),
            PurchasedRecipesFragment.newInstance(),
            PaymentHistoryFragment.newInstance(),
            ComplaintsFragment.newInstance()
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

    private fun updateToolbarTitle() {
        when (fragNavController.currentFrag) {
            is ExploreFragment ->
                supportActionBar?.title = getString(R.string.explore)
            is MyRecipesFragment ->
                supportActionBar?.title = getString(R.string.my_recipes)
            is SavedRecipesFragment ->
                supportActionBar?.title = getString(R.string.saved_recipes)
            is PurchasedRecipesFragment ->
                supportActionBar?.title = getString(R.string.purchased_recipes)
            is PaymentHistoryFragment ->
                supportActionBar?.title = getString(R.string.payment_history)
            is ComplaintsFragment ->
                supportActionBar?.title = getString(R.string.complaints)
            is RecipeDetailFragment ->
                supportActionBar?.title = getString(R.string.recipe_details)
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (fragNavController.isRootFragment) {
                    if (!binding.drawerHome.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerHome.openDrawer(GravityCompat.START)
                    }
                } else {
                    fragNavController.popFragment()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            showToast(getString(R.string.please_again_press_back_to_exit))
            if (!fragNavController.isRootFragment) {
                fragNavController.popFragment()
            }
        }
        backPressTime = System.currentTimeMillis()
    }

    override fun setObservers() {
        homeViewModel.errorData.observe(this, {
            showToast(it.message)
        })
        homeViewModel.user.observe(this, { user ->
            drawerHeaderBinding = DataBindingUtil.bind(binding.navHome.getHeaderView(0))!!
            drawerHeaderBinding.user = user
            CustomApplication.userId = user.userId
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onClickEvent(event: ClickEventMessage) {
        when (event.viewId) {
            R.id.cl_root -> {
                val model = event.payload as Recipe
                with(FragNavTransactionOptions.newBuilder()) {
                    enterAnimation = R.anim.enter_from_right
                    exitAnimation = R.anim.exit_to_left
                    popEnterAnimation = R.anim.enter_from_left
                    popExitAnimation = R.anim.exit_to_right
                    fragNavController.pushFragment(
                        RecipeDetailFragment.newInstance(recipeId = model.recipeId),
                        build()
                    )
                }
            }
        }
    }

    override fun onEvent(event: Message) {
        when (event.eventId) {
            EventConstants.Event.OPEN_CREATE_RECIPE_SCREEN.id -> {
                with(FragNavTransactionOptions.newBuilder()) {
                    enterAnimation = R.anim.enter_from_right
                    exitAnimation = R.anim.exit_to_left
                    popEnterAnimation = R.anim.enter_from_left
                    popExitAnimation = R.anim.exit_to_right
                    fragNavController.pushFragment(CreateRecipeFragment.newInstance(), build())
                }
            }
            EventConstants.Event.RECIPE_UPLOADED.id -> {
                showToast(getString(R.string.recipe_data_uploaded))
                with(FragNavTransactionOptions.newBuilder()) {
                    enterAnimation = R.anim.enter_from_right
                    exitAnimation = R.anim.exit_to_left
                    popEnterAnimation = R.anim.enter_from_left
                    popExitAnimation = R.anim.exit_to_right
                    fragNavController.popFragment()
                }
            }
        }
    }


}
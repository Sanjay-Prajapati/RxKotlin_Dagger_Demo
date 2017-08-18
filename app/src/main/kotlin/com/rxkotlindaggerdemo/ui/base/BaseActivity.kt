package com.rxkotlindaggerdemo.ui.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.rxkotlindaggerdemo.R
import com.rxkotlindaggerdemo.ui.auth.LoginActivity
import com.rxkotlindaggerdemo.ui.githubprofile.GitHubProfileActivity
import kotlinx.android.synthetic.main.activity_singlepane.*
import kotlinx.android.synthetic.main.app_bar.*
import timber.log.Timber

/**
 *  Base Activity
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val ITEM_INVALID = -1
    // delay to launch nav drawer item, to allow close animation to play
    private val NAVDRAWER_LAUNCH_DELAY = 250
    protected val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var tbToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("BaseActivity", "OnCreate Method Called")
        Timber.d("OnCreate Method called");
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        getActionbarToolbar()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Timber.d("OnPostCreate Method called");
        Log.d("BaseActivity", "OnPostCreate Method called")
        setupNavigation();
    }

    private fun getActionbarToolbar() {
        if (toolbar != null) {
            tbToolbar = findViewById(R.id.toolbar) as Toolbar;

            if (tbToolbar != null) {
                setSupportActionBar(tbToolbar);
                val actionBar = supportActionBar
                if (actionBar != null) {
                    actionBar.setDisplayShowTitleEnabled(false)
                    actionBar.setDisplayHomeAsUpEnabled(true)
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
                }
                var title = getActivityLabel()
                if (intent.hasExtra(Intent.EXTRA_TITLE)) {
                    title = intent.getStringExtra(Intent.EXTRA_TITLE)
                }
                if (tv_title != null) {
                    tv_title.setText(title)
                }
            }
        }
    }

    private fun getActivityLabel(): CharSequence {
        try {
            val activityInfo: ActivityInfo = packageManager.getActivityInfo(
                    componentName, PackageManager.GET_META_DATA);
            return activityInfo.loadLabel(packageManager).toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return getString(R.string.app_name)
    }


    private fun setupNavigation() {
        Log.d("BaseActivity", "Setup Navigation method called")
        Timber.d("Setup Navigation method called")
        val selfItem = getSelfNavDrawerItem();

        if (toolbar == null) {
            Timber.d("setupNavigation: toolbar is not present");
            return
        }
        if (drawer_layout == null) {
            return
        }
        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this,
                drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (nav_view == null) {
            return
        }
        nav_view.setNavigationItemSelectedListener(
                NavigationView.OnNavigationItemSelectedListener { item ->
                    val id = item.itemId
                    Timber.d("item id is " + id);
                    Log.d("BaseActivity", "item id is" + id)
                    closeDrawerAndNavigate(id)
                    true
                })
        val item = nav_view.getMenu().findItem(selfItem)
        if (item != null) {
            item.setChecked(true)
        }

        ll_logout.setOnClickListener({
            Log.d("BaseActivity", "Logout clicklistener")
            closeDrawerAndNavigate(it.id)
        })

    }

    private fun closeDrawerAndNavigate(id: Int) {
        Handler().postDelayed({
            goToNavDrawerItem(id)
            val item = nav_view.getMenu().findItem(getSelfNavDrawerItem())
            if (item != null) {
                item!!.setChecked(true)
            }
        }, NAVDRAWER_LAUNCH_DELAY.toLong())
        closeDrawer()
    }

    private fun closeDrawer(): Boolean {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    override fun onBackPressed() {
        if (!closeDrawer()) {
            super.onBackPressed()
        }
    }

    private fun goToNavDrawerItem(id: Int) {
        if (id == R.id.nav_drag_drop) {

        } else if (id == R.id.nav_github_profile) {
            if (this is GitHubProfileActivity) return
            startActivity(Intent(this, GitHubProfileActivity::class.java))
            finish()

        } else if (id == R.id.nav_user_profile) {

        } else if (id == R.id.ll_logout) {
            Timber.d("Logout called");
            Log.d("BaseActivity", "Logout called")
            logout()
        }
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(OnCompleteListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                })
    }

    protected open fun getSelfNavDrawerItem(): Int {
        return ITEM_INVALID;
    }

    public fun intentToFragmentArguments(intentData: Intent?): Bundle? {
        var arguments: Bundle = Bundle()
        if (intentData == null) {
            return arguments
        }
        val data = intentData.data
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }
        val extras = intentData.extras
        if (extras != null) {
            arguments.putAll(extras)
        }
        return arguments
    }
}
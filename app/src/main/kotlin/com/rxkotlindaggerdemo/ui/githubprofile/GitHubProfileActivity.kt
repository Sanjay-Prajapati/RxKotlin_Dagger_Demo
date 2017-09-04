package com.rxkotlindaggerdemo.ui.githubprofile

import android.support.v4.app.Fragment
import com.rxkotlindaggerdemo.R
import com.rxkotlindaggerdemo.ui.base.SinglePaneActivity

/**
 *
 */
class GitHubProfileActivity : SinglePaneActivity() {
    override fun onContentPane(): Fragment = GitHubProfileFragment.newInstance()

    override fun getLayoutResId(): Int = R.layout.activity_singlepane

    override fun getSelfNavDrawerItem(): Int {
        return R.id.nav_github_profile;
    }
}
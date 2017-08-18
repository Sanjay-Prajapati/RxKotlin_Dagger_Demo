package com.rxkotlindaggerdemo.injections.components

import com.rxkotlindaggerdemo.injections.modules.RetrofitModule
import com.rxkotlindaggerdemo.ui.githubprofile.GitHubProfileFragment
import dagger.Component
import javax.inject.Singleton

/**
 *  Network Component
 */

@Singleton
@Component(modules = arrayOf(RetrofitModule::class))
interface NetworkComponent {
    fun inject(fragment: GitHubProfileFragment)
}
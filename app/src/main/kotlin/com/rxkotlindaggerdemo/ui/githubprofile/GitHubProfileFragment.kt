package com.rxkotlindaggerdemo.ui.githubprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rxkotlindaggerdemo.AppApplication
import com.rxkotlindaggerdemo.R
import com.rxkotlindaggerdemo.commons.extentions.loadImg
import com.rxkotlindaggerdemo.remote.model.GithubUserProfile
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_github_profile.*
import timber.log.Timber
import javax.inject.Inject


/**
 * Github profile fragment
 */
public class GitHubProfileFragment : Fragment(), GitHubProfilePresenter.View {
    private lateinit var mUserUrl: String

    @Inject lateinit var presenter: GitHubProfilePresenter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_github_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.application as AppApplication).component.inject(this)
        presenter.initView(this)
        presenter.getGitHubProfileData()
        tv_see_profile.setOnClickListener({
            if (!mUserUrl.isEmpty()) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mUserUrl)))
            }
        })
    }

    override fun showLoadingIndicator(isShow: Boolean) {
        if (isShow)
            progress_bg?.visibility = View.VISIBLE
        else
            progress_bg?.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(cl_root_view, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun setUserProfileDetails(githubUserProfile: GithubUserProfile) {
        tv_full_name.setText(githubUserProfile.name)
        tv_username.setText(githubUserProfile.userName)
        tv_company.setText(githubUserProfile.company)
        tv_bio_value.setText(githubUserProfile.bio)
        tv_followers_value.setText(githubUserProfile.follower)
        tv_following_value.setText(githubUserProfile.following)
        tv_address.setText(githubUserProfile.location)
        iv_profile.loadImg(githubUserProfile.avatarUrl, R.drawable.placeholder)
        mUserUrl = githubUserProfile.profileUrl
    }

    override fun getNetworkError(): String {
        return activity?.getString(R.string.network_error).toString()
    }

    override fun getUnknownError(): String {
        return activity?.getString(R.string.unknown_error).toString()
    }

    override fun getUsernameObservable(): Observable<CharSequence> {
        val txt: Observable<CharSequence> = RxTextView.textChanges(et_username)
                .onErrorReturn({ "" })
        txt.subscribeBy(
                onNext = {
                    showLoadingIndicator(true)
                },
                onError = {
                    Timber.d("OnError called")
                }
        )
        return txt
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.safeDispose()
    }

    companion object {
        fun newInstance(): Fragment {
            return GitHubProfileFragment()
        }
    }
}
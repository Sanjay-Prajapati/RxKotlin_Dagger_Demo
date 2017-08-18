package com.rxkotlindaggerdemo.ui.githubprofile

import android.text.TextUtils
import com.rxkotlindaggerdemo.remote.AppEndPoint
import com.rxkotlindaggerdemo.remote.model.GithubUserProfile
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 *  GithubProfile Presenter
 */
class GitHubProfilePresenter @Inject constructor(private val api: AppEndPoint) {
    private val disposables = CompositeDisposable();
    lateinit var mView: View;

    fun initView(view: View) {
        mView = view
    }

    fun getGitHubProfileData() {
        mView.getUsernameObservable()
                .observeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap {
                    if (TextUtils.isEmpty(it)) {
                        Timber.d("Empty string in editText")
                        return@switchMap Observable.just(GithubUserProfile())
                    }
                    return@switchMap api.getGithubUserDetails(it.toString())
                            .onErrorReturn({
                                GithubUserProfile()
                            })
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Timber.d("profile pic is ${it}")
                                mView?.setUserProfileDetails(it)
                                mView?.showLoadingIndicator(false)
                            Timber.d("OnNext Called")
                        },
                        onError = {
                            Timber.d("onError Called")
                            it.printStackTrace()
                            if (it is IOException) {
                                mView?.showErrorMessage(mView?.getNetworkError())
                            } else {
                                mView?.showErrorMessage(mView?.getUnknownError())
                            }
                            Timber.d("Loading indicator hide")
                            mView?.showLoadingIndicator(false)
                        },
                        onComplete = {
                            Timber.d("onComplete Called")
                        }
                )?.let {
            disposables.add(it)
        }
    }

    fun safeDispose() {
        disposables?.clear()
        disposables?.dispose()
    }

    interface View {
        fun getUsernameObservable(): Observable<CharSequence>
        fun setUserProfileDetails(githubUserProfile: GithubUserProfile)
        fun showLoadingIndicator(isShow: Boolean)
        fun showErrorMessage(message: String)
        fun getNetworkError(): String
        fun getUnknownError(): String
    }
}
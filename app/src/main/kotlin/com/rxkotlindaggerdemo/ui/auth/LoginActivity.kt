package com.rxkotlindaggerdemo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.ResultCodes
import com.google.android.gms.tasks.OnCompleteListener
import com.rxkotlindaggerdemo.BuildConfig
import com.rxkotlindaggerdemo.R
import com.rxkotlindaggerdemo.ui.base.BaseActivity
import com.rxkotlindaggerdemo.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : BaseActivity() {
    private val RC_SIGN_IN = 123

    override fun getLayoutResId() = R.layout.activity_login
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        if (auth.currentUser != null) {
            startHomeActivity()
            // btn_logout.visibility = View.VISIBLE
        } else {
            btn_logout.visibility = View.GONE
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                            AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
                                    ))
                            .setTheme(R.style.SignInTheme)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                            .build(),
                    RC_SIGN_IN)
        }
        btn_logout.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(OnCompleteListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    })
        }
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response: IdpResponse? = IdpResponse.fromResultIntent(data)
            if (resultCode == ResultCodes.OK) {
                startHomeActivity()
                return
            } else {
                if (response == null) {
                    showSnackbar(R.string.auth_sign_in_cancelled)
                    finish()
                    return
                }
                if (response.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.network_error)
                    return
                }
                if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.auth_unknown_error)
                    return
                }
            }
            showSnackbar(R.string.auth_unknown_signin_response)
        }
    }

    private fun showSnackbar(message: Int) {
        Snackbar.make(cl_root_view, message, Snackbar.LENGTH_LONG).show()
    }
}

package com.rxkotlindaggerdemo.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 *  Base Activity
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val auth: FirebaseAuth = FirebaseAuth.getInstance()
    protected abstract fun getLayoutResId(): Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
    }
}
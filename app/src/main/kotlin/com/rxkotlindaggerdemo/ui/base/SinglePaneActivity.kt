package com.rxkotlindaggerdemo.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.rxkotlindaggerdemo.R

/**
 * A {@link BaseActivity} that simply contains a single fragment. The intent used to invoke this
 * activity is forwarded to the fragment as arguments during fragment instantiation. Derived
 * activities should only need to implement {@link SinglePaneActivity#onCreatePane()}.
 */
abstract class SinglePaneActivity : BaseActivity() {
    lateinit var mFragment: Fragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        if (getIntent().hasExtra(Intent.EXTRA_TITLE)) {
            setTitle(getIntent().getStringExtra(Intent.EXTRA_TITLE))
        } else {
            setTitle(getTitle())
        }

        if (savedInstanceState == null) {
            mFragment = onContentPane()
            mFragment.arguments = intentToFragmentArguments(intent)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, mFragment, "single_pane")
                    .commit();
        } else {
            mFragment = getSupportFragmentManager().findFragmentByTag("single_pane")
        }
    }

    protected open fun getLayoutResId(): Int = R.layout.activity_singlepane

    /**
     * Called in <code>onCreate</code> when the fragment constituting this activity is needed.
     * The returned fragment's arguments will be set to the intent used to invoke this activity.
     */
    protected abstract fun onContentPane(): Fragment

    fun getFragment(): Fragment = mFragment;
}
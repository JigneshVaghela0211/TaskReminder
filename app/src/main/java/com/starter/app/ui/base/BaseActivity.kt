package com.starter.app.ui.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.starter.app.R
import com.starter.app.core.Session
import com.starter.app.exception.ApplicationException
import com.starter.app.exception.AuthenticationException
import com.starter.app.exception.ServerException
import com.starter.app.ui.activity.AuthActivity
import com.starter.app.ui.manager.ActivityBuilder
import com.starter.app.ui.manager.ActivityStarter
import com.starter.app.ui.manager.FragmentActionPerformer
import com.starter.app.ui.manager.FragmentNavigationFactory
import com.starter.app.ui.manager.Navigator
import com.starter.app.utils.ProgressHelper
import dagger.hilt.android.AndroidEntryPoint
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), HasToolbar, Navigator {

    @Inject
    lateinit var navigationFactory: FragmentNavigationFactory

    @Inject
    lateinit var activityStarter: ActivityStarter

    @Inject
    lateinit var session: Session

    //protected var toolbar: Toolbar? = null
    //protected var toolbarTitle: AppCompatTextView? = null

    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createViewBinding())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        /*if (toolbar != null)
         setSupportActionBar(toolbar)*/

        setUpAlertDialog()

    }

    private fun setUpAlertDialog() {
        alertDialog =
            AlertDialog.Builder(this).setPositiveButton("Ok", null).setTitle(R.string.app_name)
                .create()
    }


    @Suppress("UNCHECKED_CAST")
    fun <F : BaseFragment<*>> getCurrentFragment(): F? {
        return if (findFragmentPlaceHolder() == 0) null else supportFragmentManager.findFragmentById(
            findFragmentPlaceHolder()
        ) as F?
    }

    abstract fun findFragmentPlaceHolder(): Int

    abstract fun createViewBinding(): View

    fun showMessage(message: String) {
        showErrorMessage(message)
    }

    fun showMessage(@StringRes stringId: Int) {
        showErrorMessage(getString(stringId))
    }

    fun showMessage(applicationException: ApplicationException) {
        showErrorMessage(applicationException.message)
    }

    private fun showErrorMessage(message: String, viewSet: View? = null) {
        Snackbar.make(
            viewSet ?: (this.findViewById<ViewGroup>(android.R.id.content)!!).getChildAt(0),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setActionTextColor(
                ResourcesCompat.getColor(
                    resources, android.R.color.white, null
                )
            )
            view.apply {
                findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
                    maxLines = 4
                    setTextColor(
                        ResourcesCompat.getColor(
                            resources, android.R.color.white, null
                        )
                    )
                }
                setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources, R.color.colorAccent, null
                    )
                )
            }
        }.show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun toggleLoader(show: Boolean) {
        if (show) {
            if (!ProgressHelper.isDialogVisible) ProgressHelper.showDialog(this, "Loading...")
        } else {
            if (ProgressHelper.isDialogVisible) ProgressHelper.dismissDialog()
        }
    }

    fun hideKeyboard() {
        // Check if no view has focus:

        val view = this.currentFocus
        if (view != null) {
            val inputManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun showToolbar(b: Boolean) {
        val supportActionBar = supportActionBar
        if (supportActionBar != null) {

            if (b) supportActionBar.show()
            else supportActionBar.hide()
        }
    }

    override fun setToolbarTitle(title: CharSequence) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

    override fun setToolbarTitle(@StringRes title: Int) {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(title)
            //appToolbarTitle.setText(name);
        }
    }

    override fun showBackButton(b: Boolean) {
        val supportActionBar = supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(b)
    }

    override fun setToolbarColor(@ColorRes color: Int) {
        TODO("Remove Comment")/*if (toolbar != null) {
            toolbar.setBackgroundResource(color)
        }*/
    }

    override fun setToolbarElevation(isVisible: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.elevation = if (isVisible) 8f else 0f
        }
    }

    fun showKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun <T : BaseFragment<*>> load(tClass: Class<T>): FragmentActionPerformer<T> {
        return navigationFactory.make(tClass)
    }

    override fun loadActivity(aClass: Class<out BaseActivity>): ActivityBuilder {
        return activityStarter.make(aClass)
    }

    override fun <T : BaseFragment<*>> loadActivity(
        aClass: Class<out BaseActivity>, pageTClass: Class<T>
    ): ActivityBuilder {
        return activityStarter.make(aClass).setPage(pageTClass)
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    fun onError(throwable: Throwable) {
        try {
            when (throwable) {
                is ServerException -> showMessage(throwable.message.toString())
                is ConnectException -> showMessage(R.string.connection_exception)
                is AuthenticationException -> {
                    logoutUser()
                }

                is ApplicationException -> {
                    showMessage(throwable.toString())
                }

                is SocketTimeoutException -> showMessage(R.string.socket_time_out_exception)
                else -> showMessage(getString(R.string.other_exception) + throwable.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun logoutUser() {
        session.clearSession()
        loadActivity(AuthActivity::class.java).byFinishingAll().start()
    }

}

package com.starter.app.ui.auth.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.starter.app.databinding.AuthFragmentSignupBinding
import com.starter.app.ui.base.BaseFragment
import com.starter.app.utils.Validator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Base Fragment has T type class, pass viewbinding name on this T type,
 */
@AndroidEntryPoint
class SignupFragment : BaseFragment<AuthFragmentSignupBinding>() {

    /**
     * Inject fragmentComponent for dagger
     */
    @Inject
    lateinit var validator: Validator

    /**
     * Create view binding object and return this object for set layout on fragment.
     */
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): AuthFragmentSignupBinding {
        return AuthFragmentSignupBinding.inflate(inflater, container, attachToRoot)
    }

    /**
     * This method is call when on onViewCreated call from life cycle
     * THis one is used for bind data to control
     */
    override fun bindData() {
        binding.buttonSignUp.setOnClickListener {
            navigator.goBack()
        }
    }

    override fun onBackActionPerform(): Boolean {
        return false
    }

}
package com.redvings.linkmanager.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.redvings.linkmanager.utils.AppPreferences
import com.redvings.linkmanager.utils.Utils.showToast

abstract class BaseFragment<T: ViewBinding> : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!
    val preferences by lazy { AppPreferences.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createViewBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    protected abstract fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): T

    protected abstract fun bindData()

    fun showToast(msg: String){
        context?.showToast(msg)
    }
    fun showToast(@StringRes id: Int){
        context?.showToast(id)
    }

    fun showTwoActionDialog(
        title: String?,
        description: String,
        posText: String,
        negText: String,
        positiveClick: () -> Unit
    ) {
        (activity as? BaseActivity)?.showTwoActionDialog(title, description, posText, negText, positiveClick)
    }
}
package com.redvings.linkmanager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.redvings.linkmanager.R
import com.redvings.linkmanager.databinding.DialogAddLinkBinding
import com.redvings.linkmanager.models.CollectionModel
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.utils.Utils.checkAllEmptyOrAllFilled
import com.redvings.linkmanager.utils.Utils.checkEmpty
import com.redvings.linkmanager.utils.Utils.checkValidUrl
import com.redvings.linkmanager.utils.Utils.inflateBinding
import com.redvings.linkmanager.utils.Utils.isNotBlank
import com.redvings.linkmanager.utils.Utils.isNotNullBlank
import com.redvings.linkmanager.utils.Utils.setEmptyCheck
import com.redvings.linkmanager.utils.Utils.stringText

class AddCollectionDialog(private val callback: (CollectionModel) -> Unit) :
    DialogFragment() {
    var binding: DialogAddLinkBinding? = null
    private val isLinkDetailsAdded:Boolean get() {
        binding!!.apply {
            return layoutTitle.isNotNullBlank && layoutLink.isNotNullBlank && layoutDescription.isNotNullBlank
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = requireContext().inflateBinding()
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.apply {
            setStyle(STYLE_NORMAL, R.style.dialogTheme)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.DialogAnimation
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)

        setUp()
    }

    private fun setUp() {
        binding?.apply {
            btnSave.isEnabled = false
            tvTitle.setText(R.string.text_add_collection)
            layoutCollectionName.isVisible = true
            layoutCollectionName.setEmptyCheck(R.string.msg_please_enter_collection_name)
            layoutTitle.setEmptyCheck(R.string.msg_please_enter_title)
            layoutDescription.setEmptyCheck(R.string.msg_please_enter_description)
            layoutLink.setEmptyCheck(R.string.msg_please_enter_link)
            layoutLink.checkValidUrl(R.string.msg_it_doesn_t_look_correct)

            checkEmpty(layoutCollectionName) {
                btnSave.isEnabled = it
            }

            checkAllEmptyOrAllFilled(layoutTitle, layoutLink, layoutDescription) {
                btnSave.isEnabled = it && edtCollectionName.isNotBlank
            }

            btnSave.setOnClickListener {
                callback(
                    CollectionModel(
                        id = System.currentTimeMillis().toString(),
                        name = edtCollectionName.stringText(),
                        links = arrayListOf<LinkModel>().apply {
                            if (isLinkDetailsAdded) {
                                add(
                                    LinkModel(
                                        System.currentTimeMillis().toString(),
                                        edtTitle.stringText(),
                                        edtDescription.stringText(),
                                        edtLink.stringText()
                                    )
                                )
                            }
                        }
                    )
                )
                this@AddCollectionDialog.dismiss()
            }
            btnCancel.setOnClickListener {
                this@AddCollectionDialog.dismiss()
            }
        }
    }
}



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
import androidx.fragment.app.DialogFragment
import com.redvings.linkmanager.R
import com.redvings.linkmanager.databinding.DialogAddLinkBinding
import com.redvings.linkmanager.models.LinkModel
import com.redvings.linkmanager.utils.Utils.checkEmpty
import com.redvings.linkmanager.utils.Utils.eLog
import com.redvings.linkmanager.utils.Utils.inflateBinding
import com.redvings.linkmanager.utils.Utils.safely
import com.redvings.linkmanager.utils.Utils.setEmptyCheck
import com.redvings.linkmanager.utils.Utils.setTextFormatted
import com.redvings.linkmanager.utils.Utils.stringText
import com.redvings.linkmanager.utils.Utils.validate

class AddLinkDialog(private val title: String, private val callback: (LinkModel) -> Unit) :
    DialogFragment() {
    var binding: DialogAddLinkBinding? = null

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
            tvTitle.setTextFormatted(R.string.text_add_to_holder, title)
            layoutTitle.setEmptyCheck(R.string.msg_please_enter_title)
            layoutDescription.setEmptyCheck(R.string.msg_please_enter_description)
            layoutLink.setEmptyCheck(R.string.msg_please_enter_link)

            checkEmpty(layoutTitle, layoutLink, layoutDescription) {
                btnSave.isEnabled = it
            }

            btnSave.setOnClickListener {
                /*safely {
                    layoutTitle.validate()
                    layoutLink.validate()
                    layoutDescription.validate()*/

                    callback(
                        LinkModel(
                            System.currentTimeMillis().toString(),
                            edtTitle.stringText(),
                            edtDescription.stringText(),
                            edtLink.stringText()
                        )
                    )
                    this@AddLinkDialog.dismiss()
//                }

            }
            btnCancel.setOnClickListener {
                this@AddLinkDialog.dismiss()
            }
        }
    }
}



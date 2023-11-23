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
import com.redvings.linkmanager.utils.Utils.checkValidUrl
import com.redvings.linkmanager.utils.Utils.inflateBinding
import com.redvings.linkmanager.utils.Utils.setEmptyCheck
import com.redvings.linkmanager.utils.Utils.setTextFormatted
import com.redvings.linkmanager.utils.Utils.stringText

class AddLinkDialog(
    private val title: String,
    private var linkModel: LinkModel? = null,
    private val callback: (LinkModel) -> Unit
) :
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
            setData(linkModel)
            tvTitle.text = title
            layoutTitle.setEmptyCheck(R.string.msg_please_enter_title)
            layoutDescription.setEmptyCheck(R.string.msg_please_enter_description)
            layoutLink.setEmptyCheck(R.string.msg_please_enter_link)
            layoutLink.checkValidUrl(R.string.msg_it_doesn_t_look_correct)


            checkEmpty(layoutTitle, layoutLink, layoutDescription) {
                btnSave.isEnabled = it
            }

            btnSave.setOnClickListener {
                /*safely {
                    layoutTitle.validate()
                    layoutLink.validate()
                    layoutDescription.validate()*/

               val newLinkModel = linkModel?.apply {
                    name = edtTitle.stringText()
                    description = edtDescription.stringText()
                    link = edtLink.stringText()
                } ?: LinkModel(
                    System.currentTimeMillis().toString(),
                    edtTitle.stringText(),
                    edtDescription.stringText(),
                    edtLink.stringText()
                )

                callback(newLinkModel)
                this@AddLinkDialog.dismiss()
//                }

            }
            btnCancel.setOnClickListener {
                this@AddLinkDialog.dismiss()
            }
        }
    }

    private fun setData(it: LinkModel?) {
        if (it == null) return

        binding?.apply{
            edtTitle.setText(it.name)
            edtLink.setText(it.link)
            edtDescription.setText(it.description)
        }
    }
}



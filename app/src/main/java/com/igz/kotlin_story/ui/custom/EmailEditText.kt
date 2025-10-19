package com.igz.kotlin_story.ui.custom

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import android.util.Patterns

class EmailEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyleAttr) {

    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validate()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun isValid(): Boolean {
        val textStr = text?.toString()?.trim() ?: ""
        return textStr.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(textStr).matches()
    }

    private fun validate() {
        val ok = isValid()
        error = if (!ok && !text.isNullOrEmpty()) context.getString(com.igz.kotlin_story.R.string.error_email_invalid) else null
    }
}

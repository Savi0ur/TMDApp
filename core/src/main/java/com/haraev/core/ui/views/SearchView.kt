package com.haraev.core.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.haraev.core.R
import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.view_search.view.*

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var hint: String = ""
        set(value) {
            field = value
            search_edit_text.hint = field
        }

    var text: String = ""
        get() = search_edit_text.text.toString()
        private set(value) {
            field = value
            search_edit_text.setText(field)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_search, this, true)

        lateinit var attrsArray: TypedArray

        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView)
            hint = attrsArray.getString(R.styleable.SearchView_android_hint) ?: ""
        } finally {
            attrsArray.recycle()
        }
    }

    fun textChanges(): InitialValueObservable<CharSequence> = search_edit_text.textChanges()

    fun setOnEditorActionListener(action: () -> Unit) {
        search_edit_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                action()
            }
            false
        }
    }

    fun clear() {
        text = ""
    }

    fun setOnClearClickListener(listener: () -> Unit) {
        search_clear_button.setOnClickListener { listener() }
    }

    fun setOnSearchClickListener(listener: () -> Unit) {
        search_button.setOnClickListener { listener() }
    }
}
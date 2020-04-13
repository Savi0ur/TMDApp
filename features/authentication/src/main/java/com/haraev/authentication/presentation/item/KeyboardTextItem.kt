package com.haraev.authentication.presentation.item

import com.haraev.authentication.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_pin_keyboard_text.*

data class KeyboardTextItem(
    val text: String
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.keyboard_exit_text.text = text
    }

    override fun getLayout(): Int = R.layout.item_pin_keyboard_text
}
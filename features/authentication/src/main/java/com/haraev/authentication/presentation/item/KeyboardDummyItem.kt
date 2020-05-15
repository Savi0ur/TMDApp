package com.haraev.authentication.presentation.item

import com.haraev.authentication.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class KeyboardDummyItem : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int = R.layout.item_pin_keyboard_dummy
}
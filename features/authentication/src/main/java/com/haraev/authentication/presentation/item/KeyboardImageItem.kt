package com.haraev.authentication.presentation.item

import androidx.annotation.DrawableRes
import com.haraev.authentication.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_pin_keyboard_image.*

data class KeyboardImageItem(
    @DrawableRes
    val drawableId : Int
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.keyboard_item_image.setImageResource(drawableId)
    }


    override fun getLayout(): Int = R.layout.item_pin_keyboard_image
}
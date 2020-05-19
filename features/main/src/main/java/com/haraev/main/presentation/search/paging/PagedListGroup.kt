package com.haraev.main.presentation.search.paging

import android.annotation.SuppressLint
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

class PagedListGroup<T : Item<*>> : Group, GroupDataObserver {

    var parentObserver: GroupDataObserver? = null

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            parentObserver?.onItemRangeChanged(this@PagedListGroup, position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            parentObserver?.onItemMoved(this@PagedListGroup, fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            parentObserver?.onItemRangeInserted(this@PagedListGroup, position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            parentObserver?.onItemRangeRemoved(this@PagedListGroup, position, count)
        }

    }

    private val differ = AsyncPagedListDiffer(
        listUpdateCallback,
        AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return newItem.isSameAs(oldItem)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return newItem == oldItem
            }
        }).build()
    )

    override fun getItemCount(): Int = differ.itemCount

    override fun getItem(position: Int): Item<*> =
        differ.getItem(position) ?: throw NoSuchElementException("no element at position $position")

    override fun getPosition(item: Item<*>): Int {
        val currentList = differ.currentList ?: return -1

        return currentList.indexOf(item)
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentObserver = null
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentObserver = groupDataObserver
    }

    override fun onChanged(group: Group) {
        parentObserver?.onChanged(this)
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemRangeChanged(this, index, itemCount)
        }
    }

    override fun onItemInserted(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemRangeInserted(this, index, itemCount)
        }
    }

    override fun onItemRemoved(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemRemoved(this, index)
        }
    }

    override fun onItemChanged(group: Group, position: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemChanged(this, index)
        }
    }

    override fun onItemChanged(group: Group, position: Int, payload: Any?) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemChanged(this, index, payload)
        }
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemRangeInserted(this, positionStart, itemCount)
        }
    }

    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemMoved(this, fromPosition, toPosition)
        }
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemRangeChanged(this, positionStart, itemCount)
        }
    }

    override fun onItemRangeChanged(
        group: Group,
        positionStart: Int,
        itemCount: Int,
        payload: Any?
    ) {
        val index = getItemPosition(group)
        if (index >= 0) {
            parentObserver?.onItemRangeChanged(this, positionStart, itemCount, payload)
        }
    }

    fun submitList(newPagedList: PagedList<T>) = differ.submitList(newPagedList)

    private fun getItemPosition(group: Group): Int {
        val currentList = differ.currentList

        return currentList?.indexOf(group) ?: -1
    }
}
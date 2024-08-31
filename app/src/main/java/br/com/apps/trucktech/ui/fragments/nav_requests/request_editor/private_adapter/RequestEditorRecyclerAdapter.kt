package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.private_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.request.Item
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRequestSubItemBinding
import br.com.apps.trucktech.expressions.loadGif
import br.com.apps.trucktech.expressions.loadImageThroughUrl

class RequestEditorRecyclerAdapter(
    val context: Context,
    receivedData: List<Item>,
    val itemClickListener: (itemId: String) -> Unit = {},
    val deleteClickListener: (itemId: String) -> Unit = {}
) : RecyclerView.Adapter<RequestEditorRecyclerAdapter.ViewHolder>() {

    private val dataSet = receivedData.toMutableList()

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemRequestSubItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {
        val image = binding.itemRequestItemImage
        val description = binding.itemRequestItemDescription
        val value = binding.itemRequestItemValue
        val card = binding.itemRequestItemCard
        val gif = binding.itemReqGif

        lateinit var item: Item

        init {
                card.setOnLongClickListener {
                    if (!item.isUpdating) {
                        PopupMenu(context, itemView).apply {
                            menuInflater.inflate(R.menu.menu_delete, menu)
                            setOnMenuItemClickListener(this@ViewHolder)
                        }.show()
                    }
                    true
                }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_delete_delete -> {
                        deleteClickListener(this.item.id)
                    }

                    else -> {}
                }
            }
            return true
        }

    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRequestSubItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.item = item
        bind(holder, item)
        initClickListener(holder, item)
    }

    fun bind(holder: ViewHolder, item: Item) {
        holder.apply {
            try {

                when (item.isUpdating) {
                    true -> {
                        image.visibility = GONE
                        gif.visibility = VISIBLE
                        gif.loadGif(R.drawable.gif_sending, context)
                    }

                    false -> {
                        gif.visibility = GONE
                        image.visibility = VISIBLE
                        image.loadImageThroughUrl(item.urlImage)
                    }
                }

                description.text = item.description
                value.text = item.value.toCurrencyPtBr()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initClickListener(holder: ViewHolder, item: Item) {
        holder.card.setOnClickListener {
            when (item.isUpdating) {
                true -> itemClickListener("")
                false -> itemClickListener(item.id)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initialize(dataSet: List<Item>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataSet.size

    fun insert(items: List<Item>) {
        items.forEach {
            dataSet.add(0, it)
            notifyItemInserted(0)
        }
    }

    fun remove(items: List<Item>) {
        items.forEach { item ->
            val itemToRemove = dataSet.first { it.id == item.id }
            val pos = dataSet.indexOf(itemToRemove)
            dataSet.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    fun update(items: List<Item>) {
        items.forEach { item ->
            val itemToUpdate = dataSet.first { it.id == item.id }
            val pos = dataSet.indexOf(itemToUpdate)
            dataSet[pos] = item
            notifyItemChanged(pos)
        }
    }

}
package br.com.apps.trucktech.ui.fragments.nav_requests.request_editor.private_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.request.travel_requests.RequestItem
import br.com.apps.model.model.request.travel_requests.RequestItemType
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRequestSubItemBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl

class RequestEditorRecyclerAdapter(
    val context: Context,
    dataSet: List<RequestItem>,
    val itemClickListener: (itemCLickData: RequestItemClickData) -> Unit = {},
    val deleteClickListener: (itemDto: RequestItem) -> Unit = {}
) : RecyclerView.Adapter<RequestEditorRecyclerAdapter.ViewHolder>() {

    private val dataSet = dataSet.toMutableList()

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

        lateinit var requestItem: RequestItem

        init {
            card.setOnLongClickListener {
                PopupMenu(context, itemView).apply {
                    menuInflater.inflate(R.menu.menu_delete, menu)
                    setOnMenuItemClickListener(this@ViewHolder)
                }.show()
                true
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_delete_delete -> {
                      deleteClickListener(requestItem)
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
        holder.requestItem = item
        bind(holder, item)
        initClickListener(holder, item)
    }

    fun bind(holder: ViewHolder, item: RequestItem) {
        holder.apply {
            try {
                image.loadImageThroughUrl(
                    url = item.getImage()
                )
                description.text = item.getDescription()
                value.text = item.value?.toCurrencyPtBr()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initClickListener(holder: ViewHolder, item: RequestItem) {
        holder.card.setOnClickListener {
            if (item.id != null && item.type != null) {
                itemClickListener(
                    RequestItemClickData(
                        itemId = item.id!!,
                        type = item.type!!
                    )
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<RequestItem>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataSet.size

}

class RequestItemClickData(
    val itemId: String,
    val type: RequestItemType
)
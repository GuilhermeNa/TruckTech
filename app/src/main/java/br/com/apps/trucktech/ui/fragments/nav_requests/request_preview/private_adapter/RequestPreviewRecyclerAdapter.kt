package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.private_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.model.model.request.request.RequestItemType
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRequestPreviewBinding
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import java.security.InvalidParameterException

class RequestPreviewRecyclerAdapter(
    private val context: Context,
    dataSet: List<RequestItem>
): RecyclerView.Adapter<RequestPreviewRecyclerAdapter.ViewHolder>() {

    private val dataSet = dataSet.toMutableList()

    //---------------------------------------------------------------------------------------------//
    // VIEW HOLDER
    //---------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemRequestPreviewBinding):
        RecyclerView.ViewHolder(binding.root) {
        val description = binding.itemRequestPreviewDescription
        val value = binding.itemRequestPreviewValue
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE
    //---------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRequestPreviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //---------------------------------------------------------------------------------------------//
    // ON BIND
    //---------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val requestItem = dataSet[position]
        bind(holder, requestItem)
    }

    override fun getItemCount(): Int = dataSet.size

    fun bind(holder: ViewHolder, requestItem: RequestItem) {
        holder.apply {

            description.text = requestItem.getDescription()

            value.text = requestItem.value?.toCurrencyPtBr()

            val drawable = when(requestItem.type) {
                RequestItemType.COST -> ContextCompat.getDrawable(context, R.drawable.icon_cost)
                RequestItemType.REFUEL -> ContextCompat.getDrawable(context, R.drawable.icon_refuel)
                RequestItemType.WALLET -> ContextCompat.getDrawable(context, R.drawable.icon_wallet)
                else -> {throw InvalidParameterException("RequestPreviewRecyclerAdapter, bind: invalid item type: ${requestItem.type}")}
            }
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            description.setCompoundDrawables(drawable, null, null, null)

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<RequestItem>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
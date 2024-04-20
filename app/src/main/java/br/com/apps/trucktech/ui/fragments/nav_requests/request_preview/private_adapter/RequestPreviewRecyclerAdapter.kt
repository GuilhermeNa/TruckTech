package br.com.apps.trucktech.ui.fragments.nav_requests.request_preview.private_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.request.request.RequestItem
import br.com.apps.trucktech.databinding.ItemRequestPreviewBinding
import br.com.apps.trucktech.expressions.toCurrencyPtBr

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
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<RequestItem>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
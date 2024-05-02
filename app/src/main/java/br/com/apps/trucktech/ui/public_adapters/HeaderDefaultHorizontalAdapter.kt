package br.com.apps.trucktech.ui.public_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.ItemHeaderHorizontalBinding
import br.com.apps.trucktech.ui.PositionHolder

class HeaderDefaultHorizontalAdapter(
    private val context: Context,
    dataSet: Map<Int, String>,
    private val adapterPos: Int?,
    var itemClickListener: (title: String) -> Unit = {}
) : RecyclerView.Adapter<HeaderDefaultHorizontalAdapter.ViewHolder>() {

    private val dataSet = dataSet.map { it.value }.toList()
    private var isInitialized = false

    private val positionHolder = PositionHolder(adapterPos!!)
    private var lastPos: Int = 0

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemHeaderHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.itemHeaderHorizontalTitle
        lateinit var item: String

        init {
            itemView.setOnClickListener {
                if (::item.isInitialized) {
                    itemClickListener(item)
                    if (!itemView.isSelected) {
                        positionHolder.newPositionHaveBeenSelected(bindingAdapterPosition) { lastPos, _ ->
                            itemView.isSelected = true
                            lastPos?.let {
                                notifyItemChanged(it)
                                this@HeaderDefaultHorizontalAdapter.lastPos = it
                            }
                        }
                    }
                }
            }
        }

    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHeaderHorizontalBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val headerText = dataSet[position]
        bind(holder, headerText)
        holder.item = headerText
        selectorStateManager(holder, position)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, headerText: String) {
        holder.title.text = headerText
    }

    private fun selectorStateManager(holder: ViewHolder, position: Int) {
        when (isInitialized) {
            false -> {
                if (position == adapterPos) {
                    holder.itemView.isSelected = true
                    isInitialized = true
                    positionHolder.updateLastPos(adapterPos)
                }
            }

            true -> {
                if (position == lastPos) {
                    holder.itemView.isSelected = false
                }

            }
        }
    }

}
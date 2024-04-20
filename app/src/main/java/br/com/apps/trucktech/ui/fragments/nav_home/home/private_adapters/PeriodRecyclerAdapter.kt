package br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.databinding.ItemPeriodBinding
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated
import br.com.apps.trucktech.ui.PositionHolder

class PeriodRecyclerAdapter(

    private val context: Context,
    private val dataSet: List<Travel>,
    private val adapterPos: Int?,
    var itemClickListener: (title: Int) -> Unit = {}

) : RecyclerView.Adapter<PeriodRecyclerAdapter.ViewHolder>() {

    private var isInitialized = false
    private val positionHolder = PositionHolder(adapterPos!!)
    private var lastPos: Int = 0

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemPeriodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val firstText = binding.itemPeriodRecyclerFirst
        val secondText = binding.itemPeriodRecyclerSecond
        lateinit var item: Travel

        init {
            itemView.setOnClickListener {
                if (::item.isInitialized) {
                    itemClickListener(bindingAdapterPosition)
                    if (!itemView.isSelected) {
                        positionHolder.newPositionHaveBeenSelected(bindingAdapterPosition) { lastPos, _ ->
                            itemView.isSelected = true
                            lastPos?.let{
                                notifyItemChanged(it)
                                this@PeriodRecyclerAdapter.lastPos = it
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemPeriodBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val travel = dataSet[position]
        bind(holder, travel)
        holder.item = travel
        selectedStateManager(holder, position)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, travel: Travel) {
        holder.apply {
            firstText.text = travel.initialDate?.getMonthInPtBrAbbreviated()
            secondText.text = travel.initialDate?.getDayFormatted()
        }
    }

    private fun selectedStateManager(holder: ViewHolder, position: Int) {
        when(isInitialized) {

            false -> {
                if(position == adapterPos){
                    holder.itemView.isSelected = true
                    isInitialized = true
                    positionHolder.updateLastPos(adapterPos)
                }
            }

            true -> {
                if (position ==lastPos) {
                    holder.itemView.isSelected = false
                }
            }

        }
    }

}
package br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.ItemPeriodBinding
import br.com.apps.trucktech.ui.PositionHolderTeste

class PeriodRecyclerAdapter(
    private val context: Context,
    adapterPos: Int?,
    var itemClickListener: (title: Int) -> Unit = {}
) : RecyclerView.Adapter<PeriodRecyclerAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<String>()

    private val posHolder = PositionHolderTeste(adapterPos!!)
    val selectedPos get() = posHolder.actualPos
    private lateinit var firstVh: ViewHolder

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemPeriodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val firstText = binding.itemPeriodRecyclerFirst
        val secondText = binding.itemPeriodRecyclerSecond
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
        val item = dataSet[position]
        bind(holder, item)
        initItemCLickListener(holder, position)
        selectorStateManager(holder, position)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, item: String) {
        holder.apply {
            val first = item.substringBefore(" ")
            val second = item.substringAfter(" ")
            firstText.text = first

            if (first != second) {
                secondText.visibility = VISIBLE
                secondText.text = second
            } else {
                secondText.visibility = GONE
            }

        }
    }

    private fun initItemCLickListener(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            setOnClickListener {
                if (position != posHolder.actualPos) {

                    itemClickListener(position)

                    posHolder.newPosSelected(position) { actualPos ->
                        actualPos?.let { notifyItemChanged(actualPos) }
                        isSelected = true
                    }

                }
            }
        }
    }

    private fun selectorStateManager(holder: ViewHolder, position: Int) {
        if (position == posHolder.actualPos) {
            holder.itemView.isSelected = true
            firstVh = holder
        } else {
            holder.itemView.isSelected = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<String>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

    fun resetSelector() {
        if (posHolder.actualPos != 0) {
            posHolder.newPosSelected(0) { actualPos ->
                actualPos?.let { notifyItemChanged(actualPos) }
                firstVh.itemView.isSelected = true
            }
        }
    }

}
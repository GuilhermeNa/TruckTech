package br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.payroll.TravelAid
import br.com.apps.trucktech.databinding.ItemRequestSubItemBinding
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.toCurrencyPtBr

class CostHelpRecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<CostHelpRecyclerAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<TravelAid>()

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemRequestSubItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.itemRequestItemImage
        val description = binding.itemRequestItemDescription
        val value = binding.itemRequestItemValue
        val card = binding.itemRequestItemCard
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CostHelpRecyclerAdapter.ViewHolder {
        val binding = ItemRequestSubItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: CostHelpRecyclerAdapter.ViewHolder, position: Int) {
        val item = dataSet[position]
        bind(holder, item)
    }

    fun bind(holder: ViewHolder, item: TravelAid) {
        holder.apply {
            try {
                image.loadImageThroughUrl(
                    url = "https://wtdistribuidora.com.br/uploads/blog/97/83a2ff783cb23fb1db944ddd279411ff.jpg",
                    context = context
                )
                val day = item.date.getDayFormatted()
                val month = item.date.getMonthInPtBrAbbreviated()

                description.text = "$day de $month"
                value.text = item.value.toCurrencyPtBr()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(data: List<TravelAid>) {
        this.dataSet.clear()
        this.dataSet.addAll(data)
        notifyDataSetChanged()
    }

}
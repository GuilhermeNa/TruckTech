package br.com.apps.trucktech.ui.public_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.getDayFormatted
import br.com.apps.model.expressions.getMonthInPtBrAbbreviated
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Outlay
import br.com.apps.model.model.travel.Refuel
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRecordsBinding
import br.com.apps.trucktech.expressions.isLightTheme

class RecordsItemRecyclerAdapter<T>(
    private val context: Context,
    private val dataSet: List<T>,
    private val itemCLickListener: (id: String) -> Unit = {}
) : RecyclerView.Adapter<RecordsItemRecyclerAdapter<T>.ViewHolder>() {

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemRecordsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val month = binding.itemRecyclerMonthTxt
        val day = binding.itemRecyclerDayOfMonthTxt
        val image = binding.itemRecyclerImage
        val title = binding.itemRecyclerTitleTxt
        val description = binding.itemRecyclerDescriptionTxt
        val value = binding.itemRecyclerValueTxt
        val isValidImg = binding.isValidImage
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordsItemRecyclerAdapter<T>.ViewHolder {
        val binding = ItemRecordsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = dataSet[position]
        bind(holder, t)
        holder.itemView.setOnClickListener {
            itemCLickListener(getItemId(t))
        }
    }

    override fun getItemCount(): Int = dataSet.size

    private fun getItemId(item: T): String {
        return when (item) {
            is Freight -> item.id!!
            is Refuel -> item.id!!
            is Outlay -> item.id!!
            else -> throw IllegalArgumentException("Object not supported")
        }
    }

    private fun bind(holder: ViewHolder, t: T) {
        when (t) {
            is Freight -> bindFreight(holder, t as Freight)
            is Refuel -> bindFuel(holder, t as Refuel)
            is Outlay -> bindCost(holder, t as Outlay)
        }
    }

    private fun bindFreight(holder: ViewHolder, freight: Freight) {
        holder.apply {
            month.text = freight.loadingDate?.getMonthInPtBrAbbreviated()
            day.text = freight.loadingDate?.getDayFormatted()
            title.text = freight.getCustomerName()
            description.text = "Voce carregou para ${freight.destiny}"
            value.text = freight.value?.toCurrencyPtBr()
            isValidImg.visibility = if(freight.isValid) VISIBLE else GONE
            image.run {
                setImageResource(R.drawable.image_icon_freight)
                if (image.isLightTheme()) {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
                    value.setTextColor(ContextCompat.getColor(context, R.color.dark_green))
                } else {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.dark_green))
                    value.setTextColor(ContextCompat.getColor(context, R.color.light_green))
                }
            }
        }
    }

    private fun bindFuel(holder: ViewHolder, refuel: Refuel) {
        holder.apply {
            month.text = refuel.date.getMonthInPtBrAbbreviated()
            day.text = refuel.date.getDayFormatted()
            title.text = refuel.station
            description.text = getRefuelDescription(refuel.isCompleteRefuel)
            value.text = refuel.totalValue.toCurrencyPtBr()
            isValidImg.visibility = if(refuel.isValid) VISIBLE else GONE
            image.run {
                setImageResource(R.drawable.image_icon_fuel_pump)
                if (image.isLightTheme()) {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.light_cyan))
                    value.setTextColor(ContextCompat.getColor(context, R.color.dark_red))
                } else {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.dark_cyan))
                    value.setTextColor(ContextCompat.getColor(context, R.color.light_red))
                }
            }
        }
    }

    private fun bindCost(holder: ViewHolder, travelCost: Outlay) {
        holder.apply {
            month.text = travelCost.date.getMonthInPtBrAbbreviated()
            day.text = travelCost.date.getDayFormatted()
            title.text = travelCost.company
            description.text = travelCost.description
            value.text = travelCost.value.toCurrencyPtBr()
            isValidImg.visibility = if(travelCost.isValid) VISIBLE else GONE
            image.run {
                setImageResource(R.drawable.image_icon_maintenance)

                if (image.isLightTheme()) {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
                    value.setTextColor(ContextCompat.getColor(context, R.color.dark_red))
                } else {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.dark_red))
                    value.setTextColor(ContextCompat.getColor(context, R.color.light_red))
                }
            }
        }
    }

    private fun getRefuelDescription(isCompleteRefuelling: Boolean): String {
        return when (isCompleteRefuelling) {
            true -> "Abastecimento completo."
            false -> "Abastecimento parcial."
        }
    }

}
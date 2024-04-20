package br.com.apps.trucktech.ui.public_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRecordsBinding
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated
import br.com.apps.trucktech.expressions.isLightTheme
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.model.costs.DefaultCost
import br.com.apps.trucktech.model.freight.Freight
import br.com.apps.trucktech.model.freight.FreightType
import br.com.apps.trucktech.model.refuel.ReFuel
import br.com.apps.trucktech.model.refuel.RefuelType

class RecordsItemRecyclerAdapter<T>(

    private val context: Context,
    private val dataSet: List<T>,
    var itemCLickListener: (id: String) -> Unit = {}

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
            is Freight -> item.id
            is ReFuel -> item.id
            is DefaultCost -> item.id
            else -> throw IllegalArgumentException("Object not supported")
        }
    }

    private fun bind(holder: ViewHolder, t: T) {
        when (t) {
            is Freight -> bindFreight(holder, t as Freight)
            is ReFuel -> bindFuel(holder, t as ReFuel)
            is DefaultCost -> bindCost(holder, t as DefaultCost)
        }
    }

    private fun bindFreight(holder: ViewHolder, freight: Freight) {
        holder.apply {
            month.text = freight.date.getMonthInPtBrAbbreviated()
            day.text = freight.date.getDayFormatted()
            title.text = freight.company
            description.text = "Voce carregou para Barcarena"
            value.text = freight.value.toCurrencyPtBr()
            image.run {
                if (freight.type == FreightType.DEFAULT) {
                    setImageResource(R.drawable.image_icon_freight)
                } else {
                    setImageResource(R.drawable.image_icon_complement)
                }
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

    private fun bindFuel(holder: ViewHolder, reFuel: ReFuel) {
        holder.apply {
            month.text = reFuel.date.getMonthInPtBrAbbreviated()
            day.text = reFuel.date.getDayFormatted()
            title.text = reFuel.fuelStationName
            description.text = getDescription(reFuel)
            value.text = reFuel.getTotalValue()?.toCurrencyPtBr()
            image.run {
                when (reFuel.type) {
                    RefuelType.COMPLETE -> {
                        setImageResource(R.drawable.image_icon_fuel_pump)
                    }

                    RefuelType.DIESEL_ONLY -> {
                        setImageResource(R.drawable.image_icon_fuel_pump)
                    }

                    RefuelType.ARLA_ONLY -> {
                        setImageResource(R.drawable.image_icon_arla)
                    }
                }
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

    private fun bindCost(holder: ViewHolder, travelCost: DefaultCost) {
        holder.apply {
            month.text = travelCost.date.getMonthInPtBrAbbreviated()
            day.text = travelCost.date.getDayFormatted()
            title.text = travelCost.company
            description.text = travelCost.description
            value.text = travelCost.value.toCurrencyPtBr()
            image.run {
                when (travelCost.label) {
                    1L -> {
                        setImageResource(R.drawable.image_icon_maintenance)
                    }

                    2L -> {
                        setImageResource(R.drawable.image_icon_tire)
                    }

                    3L -> {
                        setImageResource(R.drawable.image_icon_unloader)
                    }

                    4L -> {
                        setImageResource(R.drawable.image_icon_passage)
                    }

                }
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

    private fun getDescription(reFuel: ReFuel): String {
        return "Você abastreceu"
    }


}
package br.com.apps.trucktech.ui.public_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemToReceiveBinding
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.model.costs.DefaultCost
import br.com.apps.trucktech.model.freight.Freight

class ToReceiveRecyclerAdapter<T>(

    private val context: Context,
    private val dataSet: List<T>

) : RecyclerView.Adapter<ToReceiveRecyclerAdapter<T>.ViewHolder>() {

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemToReceiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val month = binding.itemToReceiveMonth
        val day = binding.itemToReceiveDay
        val description = binding.itemToReceiveDescription
        val valueText = binding.itemToReceiveValueText
        val value = binding.itemToReceiveValue
        val dataLayout = binding.itemToReceiveLayoutData
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToReceiveRecyclerAdapter<T>.ViewHolder {
        val binding =
            ItemToReceiveBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = dataSet[position]
        bind(holder, t)
    }

    override fun getItemCount(): Int = dataSet.size

    fun bind(holder: ViewHolder, t: T) {
        when(t) {
            is Freight -> {
                bindFreight(holder, t)
            }

            is DefaultCost -> {
                bindCost(holder, t)
            }
        }
    }

    private fun bindCost(holder: ViewHolder, cost: DefaultCost) {
        holder.dataLayout.setBackgroundResource(R.drawable.shape_badge_selected_default)
        holder.month.text = cost.date.getMonthInPtBrAbbreviated()
        holder.day.text = cost.date.getDayFormatted()
        holder.valueText.text = "Meu reembolso:"
        holder.value.text = cost.value.toCurrencyPtBr()
        val description = buildString {
            append("Você gastou R$ 100,00 com borracharia." )
        }
        holder.description.text = description
    }

    private fun bindFreight(holder: ViewHolder, freight: Freight) {
        holder.dataLayout.setBackgroundResource(R.drawable.shape_badge_selected_dark_green)
        holder.month.text = freight.date.getMonthInPtBrAbbreviated()
        holder.day.text = freight.date.getDayFormatted()
        holder.valueText.text = "Minha comissão:"
        holder.value.text = freight.value.toCurrencyPtBr()
        val description = buildString {
            append("Você carregou ${freight.cargo} de ${freight.origin} para ${freight.destiny} com frete no valor de ${freight.value}." )
        }
        holder.description.text = description
    }

}
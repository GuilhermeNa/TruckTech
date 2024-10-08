package br.com.apps.trucktech.ui.public_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.getDayFormatted
import br.com.apps.model.expressions.getMonthInPtBrAbbreviated
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.travel.Freight
import br.com.apps.model.model.travel.Outlay
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemToReceiveBinding

private const val VALUE_TEXT_FREIGHT = "Minha comissão:"

private const val VALUE_TEXT_EXPEND = "Meu reembolso:"

class ToReceiveRecyclerAdapter<T>(private val context: Context) : RecyclerView.Adapter<ToReceiveRecyclerAdapter<T>.ViewHolder>() {

    private val dataSet = mutableListOf<T>()

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

            is Outlay -> {
                bindCost(holder, t)
            }
        }
    }

    private fun bindCost(holder: ViewHolder, outlay: Outlay) {
        holder.dataLayout.setBackgroundResource(R.drawable.shape_badge_selected_default)
        holder.month.text = outlay.date?.getMonthInPtBrAbbreviated()
        holder.day.text = outlay.date?.getDayFormatted()
        holder.valueText.text = VALUE_TEXT_EXPEND
        holder.value.text = outlay.value?.toCurrencyPtBr()
        val description = buildString {
            append("Você realizou um pagamento de R$ ${outlay.value}." )
        }
        holder.description.text = description
    }

    private fun bindFreight(holder: ViewHolder, freight: Freight) {
        holder.dataLayout.setBackgroundResource(R.drawable.shape_badge_selected_dark_green)
        holder.month.text = freight.loadingDate?.getMonthInPtBrAbbreviated()
        holder.day.text = freight.loadingDate?.getDayFormatted()
        holder.valueText.text = VALUE_TEXT_FREIGHT
        holder.value.text = freight.getCommissionValue().toCurrencyPtBr()
        val description = buildString {
            append("Você carregou ${freight.cargo} de ${freight.origin} para ${freight.destiny} com frete no valor de ${freight.value?.toCurrencyPtBr()}." )
        }
        holder.description.text = description
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<T>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
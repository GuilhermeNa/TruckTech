package br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.ItemAdvanceBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.toCurrencyPtBr
import br.com.apps.trucktech.model.payroll.PayrollAdvance

class AdvanceRecyclerAdapter(

    private val context: Context,
    private val dataSet: List<PayrollAdvance>

    ) : RecyclerView.Adapter<AdvanceRecyclerAdapter.ViewHolder>() {

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemAdvanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.itemAdvanceDate
        val value = binding.itemAdvanceValue
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdvanceBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val advance = dataSet[position]
        bind(holder, advance)
    }

    override fun getItemCount(): Int = dataSet.size

    fun bind(holder: ViewHolder, advance: PayrollAdvance) {
        holder.apply {
            date.text = advance.date.getMonthAndYearInPtBr()
            value.text = advance.value.toCurrencyPtBr()
        }
    }


}
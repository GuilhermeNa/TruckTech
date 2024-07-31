package br.com.apps.trucktech.ui.fragments.nav_home.fines.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.FleetFine
import br.com.apps.trucktech.databinding.ItemFineBinding
import br.com.apps.model.expressions.getMonthAndYearInPtBr
import br.com.apps.model.expressions.toCurrencyPtBr

class FineRecyclerAdapter(
    private val context: Context,
    dataSet: List<FleetFine>
) : RecyclerView.Adapter<FineRecyclerAdapter.ViewHolder>() {

    private val dataSet = dataSet.toMutableList()

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemFineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.itemFineDate
        val code = binding.itemFineCode
        val description = binding.itemFineDescription
        val value = binding.itemFineValue
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFineBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fine = dataSet[position]
        bind(holder, fine)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, fine: FleetFine) {
        holder.apply {
            date.text = fine.date?.getMonthAndYearInPtBr()
            code.text = buildString {
                append("COD. ")
                append(fine.code)
            }
            description.text = fine.description
            value.text = fine.value?.toCurrencyPtBr()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<FleetFine>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
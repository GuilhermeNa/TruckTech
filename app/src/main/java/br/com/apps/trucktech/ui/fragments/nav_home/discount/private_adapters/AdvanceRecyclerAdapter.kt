package br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.getMonthAndYearInPtBr
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.payroll.Advance
import br.com.apps.trucktech.databinding.ItemAdvanceBinding

class AdvanceRecyclerAdapter(private val context: Context): RecyclerView.Adapter<AdvanceRecyclerAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<Advance>()

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

    fun bind(holder: ViewHolder, advance: Advance) {
        holder.apply {
            date.text = advance.date.getMonthAndYearInPtBr()
            value.text = advance.value.toCurrencyPtBr()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<Advance>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }


}
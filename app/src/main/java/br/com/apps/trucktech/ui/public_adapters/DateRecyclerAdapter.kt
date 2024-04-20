package br.com.apps.trucktech.ui.public_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.ItemDateBinding

class DateRecyclerAdapter(

    private val context: Context,
    private val dataSet: List<String>

) : RecyclerView.Adapter<DateRecyclerAdapter.ViewHolder>() {

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.itemDateText
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDateBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateInString = dataSet[position]
        bind(holder, dateInString)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, dateInString: String) {
        holder.date.text = dateInString
    }

}
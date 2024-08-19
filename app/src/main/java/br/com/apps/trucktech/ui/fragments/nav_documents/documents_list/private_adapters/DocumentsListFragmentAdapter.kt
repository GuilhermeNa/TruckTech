package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.expressions.getDayFormatted
import br.com.apps.model.expressions.getMonthInPtBrAbbreviated
import br.com.apps.model.model.document.TruckDocument
import br.com.apps.trucktech.databinding.ItemDocumentBinding

class DocumentsListFragmentAdapter(
    private val context: Context,
    private val plate: String,
    dataSet: List<TruckDocument>,
    val itemCLickListener: (document: TruckDocument) -> Unit = {}
) : RecyclerView.Adapter<DocumentsListFragmentAdapter.ViewHolder>() {

    val dataSet = dataSet.toMutableList()

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDERS
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tittle = binding.itemDocumentRecyclerTitle
        val date = binding.itemDocumentRecyclerDate
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding =
            ItemDocumentBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document = dataSet[position]
        bind(holder, document)
        initClickListener(holder, document)

    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, document: TruckDocument) {
        holder.tittle.text = buildString {
            val plate = plate
            val name = document.getLabelName()

            append(plate)
            append(" - ")
            append(name)
        }
        holder.date.text = buildString {
            val day = document.expeditionDate?.getDayFormatted()
            val month = document.expirationDate?.getMonthInPtBrAbbreviated()

            append(day)
            append(" ")
            append(month)
        }
    }

    private fun initClickListener(holder: ViewHolder, document: TruckDocument) {
        holder.itemView.setOnClickListener {
            itemCLickListener(document)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<TruckDocument>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
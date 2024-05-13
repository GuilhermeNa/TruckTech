package br.com.apps.trucktech.ui.fragments.nav_documents.documents_list.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.Document
import br.com.apps.trucktech.databinding.ItemDocumentBinding
import br.com.apps.trucktech.expressions.getDayFormatted
import br.com.apps.trucktech.expressions.getMonthInPtBrAbbreviated

class DocumentsListFragmentAdapter(
    private val context: Context,
    dataSet: List<Document>,
    val itemCLickListener: (document: Document) -> Unit = {}
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

    private fun bind(holder: ViewHolder, document: Document) {
        holder.tittle.text = document.name

        val day = document.expeditionDate?.getDayFormatted()
        val month = document.expirationDate?.getMonthInPtBrAbbreviated()

        holder.date.text = buildString {
            append(day)
            append(" ")
            append(month)
        }

    }

    private fun initClickListener(holder: ViewHolder, document: Document) {
        holder.itemView.setOnClickListener {
            itemCLickListener(document)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<Document>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
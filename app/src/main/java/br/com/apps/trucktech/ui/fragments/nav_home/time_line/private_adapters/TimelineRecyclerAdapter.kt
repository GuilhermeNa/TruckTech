package br.com.apps.trucktech.ui.fragments.nav_home.time_line.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.TimeLineEvent
import br.com.apps.trucktech.databinding.ItemTimeLineBinding
import br.com.apps.trucktech.databinding.ItemTimeLineFirstBinding
import br.com.apps.trucktech.databinding.ItemTimeLineLastBinding
import br.com.apps.model.expressions.getMonthAndYearInPtBr

class TimelineRecyclerAdapter(
    private val context: Context,
    private val dataSet: List<TimeLineEvent>,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val firstItem = 0
    private val item = 1
    private val lastItem = 2

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDERS
    //--------------------------------------------------------------------------------------------//

    inner class FirstItemViewHolder(binding: ItemTimeLineFirstBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val title = binding.panelTimeLineFirstTitle
        private val date = binding.panelTimeLineFirstDate
        private val description = binding.panelTimeLineFirstDescription

        fun bind(event: TimeLineEvent) {
            title.text = event.title
            date.text = event.date.getMonthAndYearInPtBr()
            description.text = event.description
        }

    }

    inner class ItemViewHolder(binding: ItemTimeLineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val title = binding.panelTimeLineTitle
        private val date = binding.panelTimeLineDate
        private val description = binding.panelTimeLineDescription

        fun bind(event: TimeLineEvent) {
            title.text = event.title
            date.text = event.date.getMonthAndYearInPtBr()
            description.text = event.description
        }

    }

    inner class LastItemViewHolder(binding: ItemTimeLineLastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val title = binding.panelTimeLineLastTitle
        private val date = binding.panelTimeLineLastDate
        private val description = binding.panelTimeLineLastDescription

        fun bind(event: TimeLineEvent) {
            title.text = event.title
            date.text = event.date.getMonthAndYearInPtBr()
            description.text = event.description
        }

    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            firstItem -> {
                val firstItemBinding = ItemTimeLineFirstBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                FirstItemViewHolder(firstItemBinding)
            }

            item -> {
                val itemBinding = ItemTimeLineBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                ItemViewHolder(itemBinding)
            }

            lastItem -> {
                val lastItemBinding = ItemTimeLineLastBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                LastItemViewHolder(lastItemBinding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = dataSet[position]

        when (getItemViewType(position)) {
            firstItem -> (holder as FirstItemViewHolder).bind(event)

            item -> (holder as ItemViewHolder).bind(event)

            lastItem -> (holder as LastItemViewHolder).bind(event)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position] == dataSet.first()) {
            firstItem
        } else if (dataSet[position] == dataSet.last()) {
            lastItem
        } else {
            item
        }
    }

}
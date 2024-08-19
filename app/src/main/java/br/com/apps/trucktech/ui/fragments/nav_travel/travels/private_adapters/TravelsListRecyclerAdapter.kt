package br.com.apps.trucktech.ui.fragments.nav_travel.travels.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemTravelBinding
import br.com.apps.model.expressions.getCompleteDateInPtBr
import java.security.InvalidParameterException

class TravelsListRecyclerAdapter(

    private val context: Context,
    private val dataSet: List<Travel>,
    private val itemCLickListener: (id: String) -> Unit = {},
    private val deleteClickListener: (travel: Travel) -> Unit

) : RecyclerView.Adapter<TravelsListRecyclerAdapter.ViewHolder>() {

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDERS
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemTravelBinding) :
        RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {

        val startDate = binding.itemTravelStart
        val endDate = binding.itemTravelEnd
        val freightNumber = binding.itemTravelFreightNumber
        val refuelNumber = binding.itemTravelRefuelNumber
        val expendNumber = binding.itemTravelExpendNumber
        val validImage = binding.isValidImage

        lateinit var travel: Travel

        init {
            itemView.setOnClickListener {
                if (::travel.isInitialized) {
                    val id = travel.id ?: throw InvalidParameterException()
                    itemCLickListener(id)
                }
            }
            itemView.setOnLongClickListener {
                if (!travel.isFinished) {
                    PopupMenu(context, itemView).apply {
                        menuInflater.inflate(R.menu.menu_delete, menu)
                        setOnMenuItemClickListener(this@ViewHolder)
                    }.show()
                }
                true
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_delete_delete -> {
                        deleteClickListener(travel)
                    }

                    else -> {}
                }
            }
            return true
        }

    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTravelBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val travel = dataSet[position]
        bind(holder, travel)
        holder.travel = travel
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, travel: Travel) {
        holder.apply {
            startDate.text = travel.initialDate?.getCompleteDateInPtBr()
            endDate.text = travel.finalDate?.getCompleteDateInPtBr() ?: "Em Curso"

            freightNumber.text =
                if (travel.getSizeOf(Travel.FREIGHT) > 0)
                    travel.getSizeOf(Travel.FREIGHT).toString()
                else "-"

            refuelNumber.text =
                if (travel.getSizeOf(Travel.REFUEL) > 0)
                    travel.getSizeOf(Travel.REFUEL).toString()
                else "-"

            expendNumber.text =
                if (travel.getSizeOf(Travel.OUTLAY) > 0)
                    travel.getSizeOf(Travel.OUTLAY).toString()
                else "-"

            validImage.visibility = if(travel.isFinished) VISIBLE else GONE

        }
    }

}
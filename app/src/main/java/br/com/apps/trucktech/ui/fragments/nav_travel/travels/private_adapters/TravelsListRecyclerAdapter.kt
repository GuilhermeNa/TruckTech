package br.com.apps.trucktech.ui.fragments.nav_travel.travels.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.travel.Travel
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemTravelBinding
import br.com.apps.trucktech.expressions.getCompleteDateInPtBr
import br.com.apps.usecase.TravelIdsData
import java.security.InvalidParameterException

class TravelsListRecyclerAdapter(

    private val context: Context,
    private val dataSet: List<Travel>,
    private val itemCLickListener: (id: String) -> Unit = {},
    private val deleteClickListener: (idsData: TravelIdsData) -> Unit

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

        lateinit var travel: Travel

        init {
            itemView.setOnClickListener {
                if (::travel.isInitialized) {
                    val id = travel.id ?: throw InvalidParameterException()
                    itemCLickListener(id)
                }
            }
            itemView.setOnLongClickListener {
                PopupMenu(context, itemView).apply {
                    menuInflater.inflate(R.menu.menu_delete, menu)
                    setOnMenuItemClickListener(this@ViewHolder)
                }.show()
                true
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_delete_delete -> {
                        val idsData = getTravelIdsData()
                        deleteClickListener(idsData)
                    }

                    else -> {}
                }
            }
            return true
        }

        private fun getTravelIdsData(): TravelIdsData {
            val id = travel.id ?: throw InvalidParameterException()
            return TravelIdsData(
                travelId = id,
                freightIds = travel.getListOfIdsForList(Travel.FREIGHT),
                refuelIds = travel.getListOfIdsForList(Travel.REFUEL),
                expendIds = travel.getListOfIdsForList(Travel.EXPEND)
            )
        }

    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
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
                if (travel.getListSize(Travel.FREIGHT) > 0)
                    travel.getListSize(Travel.FREIGHT).toString()
                else "-"

            refuelNumber.text =
                if (travel.getListSize(Travel.REFUEL) > 0)
                    travel.getListSize(Travel.REFUEL).toString()
                else "-"

            expendNumber.text =
                if (travel.getListSize(Travel.EXPEND) > 0)
                    travel.getListSize(Travel.EXPEND).toString()
                else "-"

        }
    }

}
package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.private_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.enums.PaymentRequestStatusType
import br.com.apps.model.expressions.getDayFormatted
import br.com.apps.model.expressions.getMonthInPtBrAbbreviated
import br.com.apps.model.expressions.toCurrencyPtBr
import br.com.apps.model.model.request.Request
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRequestBinding

class RequestsListRecyclerAdapter(
    private val context: Context,
    dataSet: List<Request>,
    var itemCLickListener: (id: String) -> Unit = {},
    val deleteClickListener: (id: String) -> Unit
) : RecyclerView.Adapter<RequestsListRecyclerAdapter.ViewHolder>() {

    private val dataSet = dataSet.toMutableList()

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {
        val card = binding.itemRequestCard
        val orderNumber = binding.itemOrderNumber
        val day = binding.itemOrderDay
        val month = binding.itemOrderMonth
        val value = binding.itemOrderValue
        val costNumber = binding.itemOrderCostNumber

        lateinit var request: Request

        init {
            card.setOnLongClickListener {
                if (request.status == PaymentRequestStatusType.SENT) {
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
                    R.id.menu_delete_delete -> deleteClickListener(request.id)
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
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = dataSet[position]
        bind(holder, request)
        holder.request = request
        initClickListener(holder, request)
    }

    private fun initClickListener(holder: ViewHolder, request: Request) {
        holder.card.setOnClickListener {
            request.id?.let { itemCLickListener(it) }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, order: Request) {
        holder.apply {
            month.text = order.date.getMonthInPtBrAbbreviated()
            day.text = order.date.getDayFormatted()
            orderNumber.text = order.requestNumber.toString()
            value.text = order.getValue().toCurrencyPtBr()
            costNumber.text = getNumber(order)
        }
    }

    private fun getNumber(order: Request): String {
        val numberByType = order.items.size
        return if (numberByType >= 1) {
            numberByType.toString()
        } else {
            "-"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<Request>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
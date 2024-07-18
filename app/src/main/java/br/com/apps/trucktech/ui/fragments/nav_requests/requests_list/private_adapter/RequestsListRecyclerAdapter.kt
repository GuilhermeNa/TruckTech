package br.com.apps.trucktech.ui.fragments.nav_requests.requests_list.private_adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.request.travel_requests.PaymentRequest
import br.com.apps.model.model.request.travel_requests.PaymentRequestStatusType
import br.com.apps.model.model.request.travel_requests.RequestItemType
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemRequestBinding
import br.com.apps.model.expressions.getDayFormatted
import br.com.apps.model.expressions.getMonthInPtBrAbbreviated
import br.com.apps.model.expressions.toCurrencyPtBr

class RequestsListRecyclerAdapter(
    private val context: Context,
    dataSet: List<PaymentRequest>,
    var itemCLickListener: (id: String) -> Unit = {},
    val deleteClickListener: (request: PaymentRequest) -> Unit
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
        val refuelNumber = binding.itemOrderRefuelNumber
        val costNumber = binding.itemOrderCostNumber
        val walletNumber = binding.itemOrderWalletNumber

        lateinit var request: PaymentRequest

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
                    R.id.menu_delete_delete -> deleteClickListener(request)
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

    private fun initClickListener(holder: ViewHolder, request: PaymentRequest) {
        holder.card.setOnClickListener {
            request.id?.let { itemCLickListener(it) }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, order: PaymentRequest) {
        holder.apply {
            month.text = order.date?.getMonthInPtBrAbbreviated()
            day.text = order.date?.getDayFormatted()
            orderNumber.text = order.requestNumber?.toString()
            value.text = order.getTotalValue().toCurrencyPtBr()
            refuelNumber.text = getNumber(order, RequestItemType.REFUEL)
            costNumber.text = getNumber(order, RequestItemType.COST)
            walletNumber.text = getNumber(order, RequestItemType.WALLET)
        }
    }

    private fun getNumber(order: PaymentRequest, type: RequestItemType): String {
        val numberByType = order.getNumberOfItemsByType(type)
        return if (numberByType >= 1) {
            numberByType.toString()
        } else {
            "-"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<PaymentRequest>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
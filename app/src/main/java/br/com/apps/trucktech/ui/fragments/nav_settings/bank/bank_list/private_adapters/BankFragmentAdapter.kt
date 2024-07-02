package br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.bank.BankAccount
import br.com.apps.repository.util.INVALID_OBJECT_ID
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemBankBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.ui.fragments.nav_settings.bank.bank_list.BankLFData
import java.security.InvalidParameterException

class BankFragmentAdapter(
    private val context: Context,
    _clickedPos: Int,
    data: BankLFData,
    private val clickListener: (id: String, pos: Int) -> Unit,
    private val defineNewMainAccount: (id: String) -> Unit
) : RecyclerView.Adapter<BankFragmentAdapter.ViewHolder>() {

    private var bankList = data.bankList

    private val _dataSet = data.bankAccList.toMutableList()
    val data get() = _dataSet

    private var clickedPos = _clickedPos

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDERS
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemBankBinding) :
        RecyclerView.ViewHolder(binding.root),
        PopupMenu.OnMenuItemClickListener {
        val bankImage = binding.itemBankRecyclerImageBank
        val name = binding.itemBankRecyclerName
        val checkImage = binding.itemBankRecyclerImageSelected

        lateinit var bankAccount: BankAccount

        init {
            itemView.setOnLongClickListener {
                if (::bankAccount.isInitialized && !bankAccount.mainAccount) {
                    PopupMenu(context, itemView).apply {
                        menuInflater.inflate(R.menu.menu_define_main_acc, menu)
                        setOnMenuItemClickListener(this@ViewHolder)
                    }.show()
                }
                true
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_define_main_acc -> {
                        bankAccount.id?.let { id -> defineNewMainAccount(id) }
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
            ItemBankBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bankAccount = _dataSet[position]
        bind(holder, bankAccount)
        holder.bankAccount = bankAccount
        initCLickListener(holder, bankAccount)
    }

    private fun initCLickListener(holder: ViewHolder, bankAccount: BankAccount) {
        holder.itemView.setOnClickListener {
            val id = bankAccount.id ?: throw InvalidParameterException(INVALID_OBJECT_ID)
            setClickedPos(data.indexOf(bankAccount))
            clickListener(id, clickedPos)
        }
    }

    private fun setClickedPos(pos: Int) { clickedPos = pos }

    override fun getItemCount(): Int = _dataSet.size

    private fun bind(holder: ViewHolder, bankAccount: BankAccount) {
        holder.apply {
            val urlImage = bankList.first { it.code == bankAccount.code }.urlImage
            bankImage.loadImageThroughUrl(urlImage)
            name.text = bankAccount.bankName
            bankAccount.mainAccount.let { isTrue ->
                if (isTrue) checkImage.visibility = VISIBLE
                else checkImage.visibility = GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: BankLFData) {
        this.bankList = dataSet.bankList
        this._dataSet.clear()
        this._dataSet.addAll(dataSet.bankAccList)
        notifyDataSetChanged()
    }

    fun remove() {
        if(clickedPos != -1) {
            this._dataSet.removeAt(clickedPos)
            notifyItemRemoved(clickedPos)
        }
    }

    fun add(account: BankAccount) {
        val onTop = 0
        _dataSet.add(onTop, account)
        notifyItemInserted(onTop)
    }


}
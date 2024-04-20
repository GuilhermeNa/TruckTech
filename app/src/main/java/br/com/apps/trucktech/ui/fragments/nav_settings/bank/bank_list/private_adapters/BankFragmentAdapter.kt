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
import br.com.apps.model.model.employee.BankAccount
import br.com.apps.repository.INVALID_OBJECT_ID
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemBankBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import java.security.InvalidParameterException

class BankFragmentAdapter(
    private val context: Context,
    dataSet: List<BankAccount>,
    private val clickListener: (id: String) -> Unit,
    private val defineNewMainAccount: (id: String) -> Unit
) : RecyclerView.Adapter<BankFragmentAdapter.ViewHolder>() {

    private val dataSet = dataSet.toMutableList()

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
                if (::bankAccount.isInitialized && bankAccount.mainAccount == false) {
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
        val bankAccount = dataSet[position]
        bind(holder, bankAccount)
        holder.bankAccount = bankAccount
        initCLickListener(holder, bankAccount)
    }

    private fun initCLickListener(holder: ViewHolder, bankAccount: BankAccount) {
        holder.itemView.setOnClickListener {
            val id = bankAccount.id ?: throw InvalidParameterException(INVALID_OBJECT_ID)
            clickListener(id)
        }
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, bankAccount: BankAccount) {
        holder.apply {
            bankImage.loadImageThroughUrl(bankAccount.image, context)
            name.text = bankAccount.bankName
            bankAccount.mainAccount?.let { isTrue ->
                if (isTrue) checkImage.visibility = VISIBLE
                else checkImage.visibility = GONE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<BankAccount>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
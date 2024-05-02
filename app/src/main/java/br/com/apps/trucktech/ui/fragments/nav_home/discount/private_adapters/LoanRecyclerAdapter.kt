package br.com.apps.trucktech.ui.fragments.nav_home.discount.private_adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.model.model.payroll.Loan
import br.com.apps.trucktech.databinding.ItemLoanBinding
import br.com.apps.trucktech.expressions.getMonthAndYearInPtBr
import br.com.apps.trucktech.expressions.toCurrencyPtBr

class LoanRecyclerAdapter(
    private val context: Context,
    dataSet: List<Loan>
) : RecyclerView.Adapter<LoanRecyclerAdapter.ViewHolder>() {

    private val dataSet = dataSet.toMutableList()

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemLoanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.itemLoanDate
        val total = binding.itemLoanTotalValue
        val installmentValue = binding.itemLoanInstallment
        val paid = binding.itemLoanPaid
        val progressBar = binding.itemLoanProgressBar
    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemLoanBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loan = dataSet[position]
        bind(holder, loan)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, loan: Loan) {
        holder.apply {
            date.text = loan.date?.getMonthAndYearInPtBr()
            total.text = loan.value?.toCurrencyPtBr()
            installmentValue.text = loan.getInstallmentValue().toCurrencyPtBr()
            paid.text = getProgressBarText(loan)
            progressBar.progress = getProgressBarPercent(loan)
        }
    }

    private fun getProgressBarText(loan: Loan): String {
        return if (loan.installments != null &&
            loan.installmentsAlreadyPaid != null
        ) {
            "${loan.installmentsAlreadyPaid}/${loan.installments}"
        } else {
            "-/-"
        }
    }

    private fun getProgressBarPercent(loan: Loan): Int {
        return if (loan.installments != null &&
            loan.installmentsAlreadyPaid != null
        ) {
            val x = loan.installmentsAlreadyPaid!!.toDouble()
            val y = loan.installments!!.toDouble()
            val z = (x/y)*100
            z.toInt()
            
        } else {
            0
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(dataSet: List<Loan>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        notifyDataSetChanged()
    }

}
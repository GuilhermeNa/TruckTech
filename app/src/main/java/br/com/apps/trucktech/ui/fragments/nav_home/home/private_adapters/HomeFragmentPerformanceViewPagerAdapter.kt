package br.com.apps.trucktech.ui.fragments.nav_home.home.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.databinding.ItemPerformanceBinding
import br.com.apps.trucktech.ui.fragments.nav_home.home.PerformanceItem

class HomeFragmentPerformanceViewPagerAdapter(private var context: Context) :
    RecyclerView.Adapter<HomeFragmentPerformanceViewPagerAdapter.ViewHolder>() {

    private val dataSet = mutableListOf<PerformanceItem>()

    //---------------------------------------------------------------------------------------------//
    // VIEW HOLDER
    //---------------------------------------------------------------------------------------------//

    inner class ViewHolder(binding: ItemPerformanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.itemPerformanceTitle
        val goal = binding.itemPerformanceGoal
        val achieved = binding.itemPerformanceAchieved
        val percentage = binding.itemPerformanceBarPercent
        val progressBar = binding.itemPerformanceProgressBar
    }

    //---------------------------------------------------------------------------------------------//
    // ON CREATE VIEW HOLDER
    //---------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPerformanceBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ViewHolder(binding)
    }

    //---------------------------------------------------------------------------------------------//
    // ON BIND VIEW HOLDER
    //---------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val performance = dataSet[position]
        bind(holder, performance)
    }

    override fun getItemCount(): Int = dataSet.size

    private fun bind(holder: ViewHolder, performance: PerformanceItem) {
        holder.apply {
            title.text = performance.title
            goal.text = performance.meta
            achieved.text = performance.hit
            percentage.text = performance.percent
            progressBar.progress = performance.progressBar
        }
    }

    fun update(dataSet: List<PerformanceItem>) {
        this.dataSet.clear()
        this.dataSet.addAll(dataSet)
        for(i in dataSet.indices) notifyItemChanged(i)
    }

}



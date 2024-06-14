package br.com.apps.trucktech.ui.fragments.nav_travel.records.private_adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.apps.trucktech.ui.fragments.nav_travel.cost.expend_list.ExpendListFragment
import br.com.apps.trucktech.ui.fragments.nav_travel.freight.freights_list.FreightsListFragment
import br.com.apps.trucktech.ui.fragments.nav_travel.refuel.refuels_list.RefuelsListFragment

/**
 * A different constructor was used because this Adapter is used inside a fragment
 */
class RecordsFragmentViewPagerAdapter(

    fa: FragmentManager,
    lifeCycle: Lifecycle,
    private val numPages: Int

) : FragmentStateAdapter(fa, lifeCycle) {

    override fun getItemCount(): Int = numPages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FreightsListFragment()
            1 -> RefuelsListFragment()
            2 -> ExpendListFragment()
            else -> throw IllegalArgumentException("Invalid argument for fragments position")
        }
    }

}
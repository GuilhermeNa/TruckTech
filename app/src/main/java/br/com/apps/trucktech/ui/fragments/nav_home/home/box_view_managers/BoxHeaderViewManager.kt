package br.com.apps.trucktech.ui.fragments.nav_home.home.box_view_managers

import br.com.apps.trucktech.databinding.HomeFragBoxHeaderBinding
import br.com.apps.trucktech.ui.activities.main.LoggedUser

class BoxHeaderViewManager(private val binding: HomeFragBoxHeaderBinding) {

    fun bind(user: LoggedUser) {
        binding.apply {
            name.text = user.name
        }
    }

}
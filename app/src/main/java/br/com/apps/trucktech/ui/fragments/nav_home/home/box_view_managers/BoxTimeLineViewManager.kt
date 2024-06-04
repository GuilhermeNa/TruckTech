package br.com.apps.trucktech.ui.fragments.nav_home.home.box_view_managers

import android.content.Context
import br.com.apps.trucktech.databinding.PanelHomeFragmentTimeLineBinding
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.expressions.navigateTo
import br.com.apps.trucktech.ui.fragments.nav_home.home.HomeFragmentDirections

class BoxTimeLineViewManager(
    private val binding: PanelHomeFragmentTimeLineBinding,
    private val context: Context
) {

    fun initialize() {
        binding.apply {
            panelTimeLineImage.loadImageThroughUrl(
                "https://cdn.sanity.io/images/599r6htc/localized/e09081e08bcc400a488dd7c1fa88a4d1493b52aa-1108x1108.png?w=514&q=75&fit=max&auto=format",
                context
            )
            panelTimeLineCard.setOnClickListener {
                it.navigateTo(HomeFragmentDirections.actionHomeFragmentToTimelineFragment())
            }
        }
    }

}
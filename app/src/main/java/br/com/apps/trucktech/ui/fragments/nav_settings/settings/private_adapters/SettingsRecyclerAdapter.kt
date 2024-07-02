package br.com.apps.trucktech.ui.fragments.nav_settings.settings.private_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.apps.trucktech.R
import br.com.apps.trucktech.databinding.ItemHeaderSettingsBinding
import br.com.apps.trucktech.databinding.ItemSettingsBinding
import br.com.apps.trucktech.expressions.isLightTheme
import br.com.apps.trucktech.expressions.loadImageThroughUrl
import br.com.apps.trucktech.ui.fragments.nav_settings.settings.SettingsItem

class SettingsRecyclerAdapter(

    private val context: Context,
    private val dataSet: List<SettingsItem>,
    var itemCLickListener: (title: String) -> Unit = {}

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val headerType = 0
    private val itemType = 1

    //--------------------------------------------------------------------------------------------//
    //  VIEW HOLDERS
    //--------------------------------------------------------------------------------------------//

    inner class HeaderViewHolder(binding: ItemHeaderSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.headerSettingsImage
        private val title = binding.headerSettingsName

        fun bind(item: SettingsItem) {
            image.loadImageThroughUrl(item.imageUrl)
            title.text = item.title
        }

    }

    inner class ItemViewHolder(binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var item: SettingsItem

        private val icon = binding.itemSettingsIcon
        private val title = binding.itemSettingsTitle
        private val description = binding.itemSettingsDescription

        fun bind(item: SettingsItem) {
            item.imageId?.let { icon.setImageResource(it) }
            icon.isLightTheme().let { isLight ->
                if (isLight) icon.setColorFilter(R.color.black)
            }
            title.text = item.title
            description.text = item.description
        }

        init {
            itemView.setOnClickListener {
                if (::item.isInitialized) {
                    item.title?.let { itemCLickListener(it) }
                }
            }
        }

    }

    //--------------------------------------------------------------------------------------------//
    //  ON CREATE VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            headerType -> {
                val headerBinding = ItemHeaderSettingsBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
                HeaderViewHolder(headerBinding)
            }

            itemType -> {
                val itemBinding =
                    ItemSettingsBinding.inflate(LayoutInflater.from(context), parent, false)
                ItemViewHolder(itemBinding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    //--------------------------------------------------------------------------------------------//
    //  ON BIND VIEW HOLDER
    //--------------------------------------------------------------------------------------------//

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        when (getItemViewType(position)) {
            headerType -> {
                (holder as HeaderViewHolder).bind(item)
            }

            itemType -> {
                (holder as ItemViewHolder).bind(item)
                holder.item = item
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) headerType else itemType
    }


}
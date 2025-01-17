package ru.practicum.android.diploma.filter.ui.selectRegion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.fields.Area

class RegionSelectorAdapter(
    private var region: List<Area?>,
    private val onClick: (Area) -> Unit
) : RecyclerView.Adapter<RegionSelectorViewHolder>() {

    fun updateRegion(newRegion: List<Area?>) {
        val diffCallback = RegionDiffCallback(region, newRegion)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        region = newRegion
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionSelectorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return RegionSelectorViewHolder(view, onClick)
    }

    override fun getItemCount(): Int {
        return region.size
    }

    override fun onBindViewHolder(holder: RegionSelectorViewHolder, position: Int) {
        region[position]?.let { holder.bind(it) }
    }
}
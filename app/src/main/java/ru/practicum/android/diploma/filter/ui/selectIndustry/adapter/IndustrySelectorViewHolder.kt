package ru.practicum.android.diploma.filter.ui.selectIndustry.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemRegionsIndustriesBinding
import ru.practicum.android.diploma.filter.domain.models.fields.Industry

class IndustrySelectorViewHolder(itemView: View, val onClick: (Industry) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    val binding = ItemRegionsIndustriesBinding.bind(itemView)
    val regionAndIndustryName = binding.regionsAndIndustry
    val radioButton = binding.radioButton

    fun bind(industry: Industry) {
        regionAndIndustryName.text = industry.name

        radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                onClick(industry)
        }

    }
}
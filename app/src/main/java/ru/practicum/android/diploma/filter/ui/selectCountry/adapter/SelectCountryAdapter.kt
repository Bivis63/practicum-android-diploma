package ru.practicum.android.diploma.filter.ui.selectCountry.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.filter.domain.models.fields.Country

class SelectCountryAdapter(
    private var countries: List<Country?>,
    private val onClick: (Country) -> Unit
) : RecyclerView.Adapter<SelectCountryViewHolder>() {

    fun updateCountries(newCountries: List<Country?>) {
        val diffCallback = CountryDiffCallback(countries,newCountries)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        countries = newCountries
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return SelectCountryViewHolder(view, onClick)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: SelectCountryViewHolder, position: Int) {
        countries[position]?.let { holder.bind(it) }
    }
}
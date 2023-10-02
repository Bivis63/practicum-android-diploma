package ru.practicum.android.diploma.core.utils.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.search.domain.models.Vacancy



class VacancyAdapter(private val vacancies: ArrayList<Vacancy>) :
    RecyclerView.Adapter<VacancyViewHolder>() {

    var itemClickListener: ((Int, Vacancy) -> Unit)? = null
    var itemLongClickListener: ((Int, Vacancy) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        return VacancyViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancies[position]
        holder.bind(vacancy)
        holder.itemView.setOnClickListener() {
            itemClickListener?.invoke(position, vacancy)
        }

        holder.itemView.setOnLongClickListener(){
            itemLongClickListener?.invoke(position, vacancy)
            return@setOnLongClickListener true
        }
    }

    fun setVacancies(newVacancy: List<Vacancy>?) {
        vacancies.clear()
        if (!newVacancy.isNullOrEmpty()) {
            vacancies.addAll(newVacancy)
        }
        notifyDataSetChanged()
    }
}


package ru.practicum.android.diploma.core.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.practicum.android.diploma.vacancyDetails.ui.viewmodel.VacancyDetailsViewModel

val vacancyDetailsModule = module {

    viewModelOf(::VacancyDetailsViewModel).bind<VacancyDetailsViewModel>()
}
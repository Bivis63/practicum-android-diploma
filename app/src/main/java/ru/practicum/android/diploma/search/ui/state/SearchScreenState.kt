package ru.practicum.android.diploma.search.ui.state

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    object NothingFound : SearchScreenState

    object SearchStringEmpty : SearchScreenState

    data class Success (
    val vacancies: List<Vacancy>
    ) : SearchScreenState

    data class Error(
        val error: Int?
    ) : SearchScreenState

}
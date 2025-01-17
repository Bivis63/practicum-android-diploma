package ru.practicum.android.diploma.filter.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.utils.ErrorType
import ru.practicum.android.diploma.core.utils.Resource
import ru.practicum.android.diploma.filter.data.network.dto.fields.toDomain
import ru.practicum.android.diploma.filter.domain.CountryRepository
import ru.practicum.android.diploma.filter.domain.models.fields.Country
import ru.practicum.android.diploma.search.data.network.api.HeadHunterApiService


class CountryRepositoryImpl(private val api: HeadHunterApiService) : CountryRepository {
    override suspend fun getCountries(): Flow<Resource<List<Country>>> = flow {
        try {
            val countries = api.getCountries().map { it.toDomain() }
            emit(Resource.Success(countries))
        } catch (e: Exception) {
            emit(Resource.Error(ErrorType.NO_CONNECTION))
        }
    }

    override suspend fun getCountryById(countryId: String): Country {
        var country = api.getAreasInCountry(countryId)
        while (country.parentId != null) {
            country = api.getAreasInCountry(country.parentId as String)
        }
        return country.toDomain()
    }
}



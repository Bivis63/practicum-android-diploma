package ru.practicum.android.diploma.search.data.network.dto.fields

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.domain.models.fields.Area

data class AreaDto(
    val id: String?,
    val name: String?,
    @SerializedName("parent_id")
    val countryId: String?,
    val areas: List<AreaDto>?
)

fun AreaDto.toDomain(): Area {
    return Area(
        id = this.id,
        name = this.name,
        countryId = this.countryId,
        areas = this.areas?.map { it.toDomain() }
    )
}
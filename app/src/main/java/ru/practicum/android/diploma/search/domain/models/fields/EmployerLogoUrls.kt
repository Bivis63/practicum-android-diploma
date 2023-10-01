package ru.practicum.android.diploma.search.domain.models.fields

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.search.data.network.dto.fields.EmployerLogoUrlsDto

data class EmployerLogoUrls(
    @SerializedName("90")
    val smallLogo: String?,
    @SerializedName("240")
    val middleLogo: String?,
    val original: String?
) {
    fun toDto(): EmployerLogoUrlsDto {
        return EmployerLogoUrlsDto(
            smallLogo = this.smallLogo,
            middleLogo = this.middleLogo,
            original = this.original
        )
    }
}

package ru.practicum.android.diploma.favorites.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository

class IsInFavoritesCheck(private val repo: FavoritesRepository) {

    operator fun invoke(id:String) : Flow<Boolean> {
        return repo.isFavorite(id)
    }
}
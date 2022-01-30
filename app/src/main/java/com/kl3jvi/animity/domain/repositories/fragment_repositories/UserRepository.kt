package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.UserQuery
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val bearerToken: String?
    val guestToken: String?
    val isAuthenticated: Boolean
    val isGuest: Boolean
    fun setBearerToken(token: String?)
    fun setGuestToken(token: String?)
    fun clearStorage()

    fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>>
    fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>>
    fun getAnimeListData(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>>
}
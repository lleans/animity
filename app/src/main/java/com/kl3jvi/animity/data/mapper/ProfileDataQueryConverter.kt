package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.model.ui_models.*

fun UserQuery.Data.convert(): ProfileData {
    return if (user != null) ProfileData(
        User(
            user.id,
            user.name,
            user.about ?: "",
            UserAvatar(
                user.avatar?.large ?: "",
                user.avatar?.medium ?: ""
            ),
            user.bannerImage ?: "",
        )
    ) else ProfileData()
}

fun AnimeListCollectionQuery.Data.convert(): List<ProfileRow> {
    return this.media?.lists?.mapNotNull {
        ProfileRow(
            it?.name.orEmpty(),
            it?.entries.convert()
        )
    } ?: listOf()
}

private fun List<AnimeListCollectionQuery.Entry?>?.convert(): List<AniListMedia> {
    return this?.mapNotNull {
        AniListMedia(
            idAniList = it?.media?.id ?: 0,
            title = MediaTitle(romaji = it?.media?.title?.romaji ?: ""),
            type = it?.media?.type,
            format = it?.media?.format,
            status = it?.media?.status,
            nextAiringEpisode = it?.media?.nextAiringEpisode?.airingAt,
            description = it?.media?.description ?: "",
            startDate = if (it?.media?.startDate?.year != null) {
                FuzzyDate(
                    it.media.startDate.year,
                    it.media.startDate.month,
                    it.media.startDate.day
                )
            } else {
                null
            },
            coverImage = MediaCoverImage(
                it?.media?.coverImage?.large ?: "",
                it?.media?.coverImage?.large ?: "",
                it?.media?.coverImage?.large ?: ""
            ),
            genres = it?.media?.genres?.mapNotNull { Genre(name = it ?: "") } ?: listOf(),
            averageScore = it?.media?.averageScore ?: 0,
        )
    } ?: listOf()
}


data class ProfileData(
    val userData: User = User(),
    val profileRow: List<ProfileRow> = listOf()
)

data class ProfileRow(
    val title: String = "",
    val anime: List<AniListMedia> = listOf()
)

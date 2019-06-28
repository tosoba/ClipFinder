package com.example.there.domain.repo.soundcloud

import com.example.there.domain.entity.soundcloud.SoundCloudPlaylistEntity
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.repo.IFavouritePlaylistRepository
import com.example.there.domain.repo.IFavouriteTrackRepo

interface ISoundCloudDbDataStore :
        IFavouriteTrackRepo<SoundCloudTrackEntity>,
        IFavouritePlaylistRepository<SoundCloudPlaylistEntity> {

}
package com.example.there.findclips.di.player

import com.example.there.findclips.activities.player.PlayerActivity
import dagger.Subcomponent

@PlayerScope
@Subcomponent(modules = [PlayerModule::class])
interface PlayerSubComponent {
    fun inject(playerActivity: PlayerActivity)
}
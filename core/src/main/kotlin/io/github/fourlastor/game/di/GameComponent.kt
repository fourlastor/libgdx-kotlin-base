package io.github.fourlastor.game.di

import dagger.Component
import io.github.fourlastor.game.Game
import javax.inject.Singleton

@Singleton
@Component(modules = [ScreensModule::class, InputModule::class])
internal interface GameComponent {

    fun game(): Game

    companion object {
        fun create(): GameComponent = DaggerGameComponent.create()
    }
}

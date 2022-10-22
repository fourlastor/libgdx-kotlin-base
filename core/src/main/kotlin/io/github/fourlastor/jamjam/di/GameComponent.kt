package io.github.fourlastor.jamjam.di

import dagger.Component
import io.github.fourlastor.jamjam.Game
import javax.inject.Singleton

@Singleton
@Component
internal interface GameComponent {

    fun game(): Game

    companion object {
        fun create(): GameComponent = DaggerGameComponent.create()
    }
}
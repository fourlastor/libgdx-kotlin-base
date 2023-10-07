package io.github.fourlastor.game.di

import com.badlogic.gdx.InputMultiplexer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InputModule {

    @Provides @Singleton
    fun inputMultiplexer() = InputMultiplexer()
}

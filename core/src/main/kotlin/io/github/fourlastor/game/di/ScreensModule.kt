package io.github.fourlastor.game.di

import dagger.Module
import io.github.fourlastor.game.level.di.LevelComponent
import io.github.fourlastor.game.menu.di.MenuComponent

@Module(subcomponents = [MenuComponent::class, LevelComponent::class])
class ScreensModule

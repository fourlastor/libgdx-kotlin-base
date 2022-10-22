package io.github.fourlastor.jamjam.di

import dagger.Module
import io.github.fourlastor.jamjam.menu.di.MenuComponent

@Module(subcomponents = [MenuComponent::class])
class ScreensModule

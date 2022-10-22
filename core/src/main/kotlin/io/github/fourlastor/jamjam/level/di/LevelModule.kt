package io.github.fourlastor.jamjam.level.di

import dagger.Module
import dagger.Provides
import io.github.fourlastor.jamjam.di.ScreenScoped
import io.github.fourlastor.ldtk.LDtkLevelDefinition
import io.github.fourlastor.ldtk.LDtkMapData

@Module
class LevelModule constructor(
    private val levelIndex: Int,
) {
    @Provides
    @ScreenScoped
    fun levelDefinition(data: LDtkMapData): LDtkLevelDefinition = data.levelDefinitions[levelIndex]
}

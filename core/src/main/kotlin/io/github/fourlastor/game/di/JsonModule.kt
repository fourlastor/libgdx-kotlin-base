package io.github.fourlastor.game.di

import com.badlogic.gdx.Gdx
import dagger.Module
import dagger.Provides
import io.github.fourlastor.ldtk.LDtkMapData
import io.github.fourlastor.ldtk.LDtkReader
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
class JsonModule {

    @Provides
    @Singleton
    fun json(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun mapData(reader: LDtkReader): LDtkMapData = reader.data(Gdx.files.internal("maps.ldtk").read())
}

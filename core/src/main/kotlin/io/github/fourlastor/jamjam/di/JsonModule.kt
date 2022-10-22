package io.github.fourlastor.jamjam.di

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
class JsonModule {

    @Provides
    @Singleton
    fun json(): Json = Json { ignoreUnknownKeys = true }
}

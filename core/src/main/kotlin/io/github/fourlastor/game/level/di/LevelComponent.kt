package io.github.fourlastor.game.level.di

import dagger.Subcomponent
import io.github.fourlastor.game.di.ScreenScoped
import io.github.fourlastor.game.level.LevelScreen
import io.github.fourlastor.game.router.RouterModule

@ScreenScoped
@Subcomponent(modules = [RouterModule::class, LevelModule::class])
interface LevelComponent {

    @ScreenScoped
    fun screen(): LevelScreen

    @Subcomponent.Builder
    interface Builder {
        fun level(levelModule: LevelModule): Builder
        fun router(routerModule: RouterModule): Builder
        fun build(): LevelComponent
    }
}

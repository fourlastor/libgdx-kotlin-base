package io.github.fourlastor.game.level.di

import dagger.BindsInstance
import dagger.Subcomponent
import io.github.fourlastor.game.Router
import io.github.fourlastor.game.di.ScreenScoped
import io.github.fourlastor.game.level.LevelScreen

@ScreenScoped
@Subcomponent(modules = [LevelModule::class])
interface LevelComponent {

    @ScreenScoped fun screen(): LevelScreen

    @Subcomponent.Builder
    interface Builder {
        fun router(@BindsInstance router: Router): Builder
        fun build(): LevelComponent
    }
}

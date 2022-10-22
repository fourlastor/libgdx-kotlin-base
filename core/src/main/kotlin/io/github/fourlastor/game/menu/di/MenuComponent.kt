package io.github.fourlastor.game.menu.di

import dagger.Subcomponent
import io.github.fourlastor.game.di.ScreenScoped
import io.github.fourlastor.game.menu.MenuScreen
import io.github.fourlastor.game.router.RouterModule

@ScreenScoped
@Subcomponent(modules = [RouterModule::class])
interface MenuComponent {

    @ScreenScoped
    fun screen(): MenuScreen

    @Subcomponent.Builder
    interface Builder {
        fun router(routerModule: RouterModule): Builder
        fun build(): MenuComponent
    }
}

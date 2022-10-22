package io.github.fourlastor.jamjam.menu.di

import dagger.Subcomponent
import io.github.fourlastor.jamjam.di.ScreenScoped
import io.github.fourlastor.jamjam.menu.MenuScreen
import io.github.fourlastor.jamjam.router.RouterModule

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

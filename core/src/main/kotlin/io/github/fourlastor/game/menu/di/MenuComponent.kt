package io.github.fourlastor.game.menu.di

import dagger.BindsInstance
import dagger.Subcomponent
import io.github.fourlastor.game.Router
import io.github.fourlastor.game.di.ScreenScoped
import io.github.fourlastor.game.menu.MenuScreen

@ScreenScoped
@Subcomponent
interface MenuComponent {

    @ScreenScoped fun screen(): MenuScreen

    @Subcomponent.Builder
    interface Builder {
        fun router(@BindsInstance router: Router): Builder
        fun build(): MenuComponent
    }
}

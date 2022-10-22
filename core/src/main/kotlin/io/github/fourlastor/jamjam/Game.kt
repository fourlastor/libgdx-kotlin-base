package io.github.fourlastor.jamjam

import com.badlogic.gdx.Screen
import com.badlogic.gdx.physics.box2d.Box2D
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.jamjam.di.GameComponent
import io.github.fourlastor.jamjam.level.LevelScreen
import io.github.fourlastor.jamjam.level.di.LevelComponent
import io.github.fourlastor.jamjam.level.di.LevelModule
import io.github.fourlastor.jamjam.menu.MenuScreen
import io.github.fourlastor.jamjam.menu.di.MenuComponent
import io.github.fourlastor.jamjam.router.Router
import io.github.fourlastor.jamjam.router.RouterModule
import ktx.app.KtxGame
import ktx.scene2d.Scene2DSkin
import javax.inject.Inject


class Game @Inject constructor(
    private val levelBuilder: LevelComponent.Builder,
    private val menuBuilder: MenuComponent.Builder,
) : KtxGame<Screen>(), Router {

    private val routerModule = RouterModule(this)

    override fun create() {
        Box2D.init()
        VisUI.load(VisUI.SkinScale.X2)
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        goToMenu()
    }

    override fun goToMenu() {
        clear()
        addScreen(menuBuilder.router(routerModule).build().screen())
        setScreen<MenuScreen>()
    }

    override fun goToLevel(levelIndex: Int) {
        clear()
        addScreen(
            levelBuilder
                .router(routerModule)
                .level(LevelModule(levelIndex))
                .build()
                .screen()
        )
        setScreen<LevelScreen>()
    }

    private fun clear() {
        if (containsScreen<LevelScreen>()) {
            removeScreen<LevelScreen>()
        }
        if (containsScreen<MenuScreen>()) {
            removeScreen<MenuScreen>()
        }
    }

    companion object {
        fun create(): Game = GameComponent.create().game()
    }
}

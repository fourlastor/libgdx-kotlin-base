package io.github.fourlastor.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.physics.box2d.Box2D
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.game.di.GameComponent
import io.github.fourlastor.game.level.LevelScreen
import io.github.fourlastor.game.level.di.LevelComponent
import io.github.fourlastor.game.level.di.LevelModule
import io.github.fourlastor.game.menu.MenuScreen
import io.github.fourlastor.game.menu.di.MenuComponent
import io.github.fourlastor.game.router.Router
import io.github.fourlastor.game.router.RouterModule
import ktx.app.KtxGame
import ktx.scene2d.Scene2DSkin
import javax.inject.Inject


class Game @Inject constructor(
    private val levelBuilder: LevelComponent.Builder,
    private val menuBuilder: MenuComponent.Builder,
    private val inputMultiplexer: InputMultiplexer,
) : KtxGame<Screen>(), Router {

    private val routerModule = RouterModule(this)

    override fun create() {
        Box2D.init()
        VisUI.load(VisUI.SkinScale.X2)
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        Gdx.input.inputProcessor = inputMultiplexer
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

package io.github.fourlastor.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import io.github.fourlastor.game.di.GameComponent
import io.github.fourlastor.game.level.LevelScreen
import io.github.fourlastor.game.level.di.LevelComponent
import io.github.fourlastor.game.menu.MenuScreen
import io.github.fourlastor.game.menu.di.MenuComponent
import ktx.app.KtxGame
import javax.inject.Inject

class Game @Inject constructor(
    private val levelBuilder: LevelComponent.Builder,
    private val menuBuilder: MenuComponent.Builder,
    private val inputMultiplexer: InputMultiplexer,
) : KtxGame<Screen>(), Router {

    override fun create() {
        Gdx.input.inputProcessor = inputMultiplexer
        goToMenu()
    }

    override fun goToMenu() {
        clear()
        addScreen(menuBuilder.router(this).build().screen())
        setScreen<MenuScreen>()
    }

    override fun goToLevel(levelIndex: Int) {
        clear()
        addScreen(levelBuilder.router(this).build().screen())
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

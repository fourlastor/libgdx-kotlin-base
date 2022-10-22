package io.github.fourlastor.jamjam

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.physics.box2d.Box2D
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.jamjam.di.GameComponent
import io.github.fourlastor.jamjam.level.LevelScreen
import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkLevelDefinition
import io.github.fourlastor.ldtk.LDtkReader
import ktx.app.KtxGame
import ktx.scene2d.Scene2DSkin
import javax.inject.Inject


class Game @Inject constructor() : KtxGame<Screen>() {

    private val reader = LDtkReader()
    override fun create() {
        Box2D.init()

        VisUI.load(VisUI.SkinScale.X2)
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        val gameData = reader.data(Gdx.files.internal("maps.ldtk").read())

        addScreen(MenuScreen(this, gameData))
        setScreen<MenuScreen>()
        startGame(
            gameData.levelDefinitions[0],
            gameData.defs,
        )
    }

    fun startGame(levelDefinition: LDtkLevelDefinition, defs: Definitions) {
        clearLevel()
        addScreen(LevelScreen(levelDefinition, defs))
        setScreen<LevelScreen>()
    }

    private fun clearLevel() {
        if (containsScreen<LevelScreen>()) {
            removeScreen<LevelScreen>()
        }
    }

    companion object {
        fun create(): Game = GameComponent.create().game()
    }
}

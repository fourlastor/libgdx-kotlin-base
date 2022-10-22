package io.github.fourlastor.jamjam

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.ldtk.LDtkMapData
import ktx.actors.onClick
import ktx.app.KtxScreen
import ktx.scene2d.actors
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton

class MenuScreen(
    private val game: Game,
    private val gameData: LDtkMapData,
) : KtxScreen {

    private val stage = Stage()

    init {
        stage.actors {
            visTable(defaultSpacing = true) {
                setFillParent(true)
                gameData.levelDefinitions.forEachIndexed { index, levelDefinition ->
                    row()
                    visTextButton("Start level ${index + 1}").apply {
                        onClick {
                            game.startGame(
                                levelDefinition,
                                gameData.defs
                            )
                        }
                    }
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        VisUI.dispose()
    }
}

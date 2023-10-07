package io.github.fourlastor.game.level

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import io.github.fourlastor.game.di.ScreenScoped
import ktx.app.KtxScreen
import javax.inject.Inject

@ScreenScoped
class LevelScreen @Inject constructor(
    private val inputMultiplexer: InputMultiplexer,
) : KtxScreen {

    private val stage = Stage()

    override fun show() {
        inputMultiplexer.addProcessor(stage)
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(stage)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }
}

package io.github.fourlastor.game.menu

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.game.Router
import io.github.fourlastor.game.di.ScreenScoped
import ktx.actors.onClick
import ktx.app.KtxScreen
import ktx.scene2d.actors
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton
import javax.inject.Inject

@ScreenScoped
class MenuScreen @Inject constructor(
    private val inputMultiplexer: InputMultiplexer,
    private val router: Router,
) : KtxScreen {

    private val stage = Stage()

    init {
        VisUI.load()
        stage.actors {
            visTable(defaultSpacing = true) {
                setFillParent(true)
                (0..2).forEach { index ->
                    row()
                    visTextButton("Start level ${index + 1}").apply { onClick { router.goToLevel(index) } }
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        stage.viewport.update(width, height, true)
    }

    override fun show() {
        inputMultiplexer.addProcessor(stage)
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(stage)
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

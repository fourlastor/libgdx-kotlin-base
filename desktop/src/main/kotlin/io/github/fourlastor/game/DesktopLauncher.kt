package io.github.fourlastor.game

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val config =
        Lwjgl3ApplicationConfiguration().apply {
            val displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode()
            val width = displayMode.width * 0.75f
            val height = width / 16f * 9f
            setWindowedMode(width.toInt(), height.toInt())
            setForegroundFPS(60)
        }
    Lwjgl3Application(Game.create(), config)
}

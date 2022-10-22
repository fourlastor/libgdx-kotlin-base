package io.github.fourlastor.jamjam

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Disposable

class AssetFactory(private val scale: Float) : Disposable {

    private val atlas by lazy { TextureAtlas(Gdx.files.internal("tiles.atlas"), true) }
    private val parallaxAtlas by lazy { TextureAtlas(Gdx.files.internal("parallax.atlas"), true)}

    fun tile(
        name: String,
        x: Float,
        y: Float,
        flipX: Boolean,
        flipY: Boolean
    ): Sprite {
        return atlas.createSprite(name)
            .scaleAtOrigin()
            .apply {
                setPosition(x * scale, y * scale)
                flip(flipX, flipY)
            }
    }

    fun box(x: Float, y: Float, size: Float) = Rectangle(
        x * scale,
        y * scale,
        size * scale,
        size * scale,
    )

    fun playerBlueprint(x: Float, y: Float): Rectangle =
        Rectangle(x * scale, y * scale, 16f * scale, 16f * scale)

    private val characterStanding by lazy {
        atlas.createSprites("player_stand")
            .onEach { it.scaleAtOrigin() }
            .let { Animation(0.033f, it, Animation.PlayMode.LOOP) }

    }

    fun characterStanding(): Animation<Sprite> = characterStanding

    private val characterRunning by lazy {
        atlas.createSprites("player_run")
            .onEach { it.scaleAtOrigin() }
            .let { Animation(0.15f, it, Animation.PlayMode.LOOP) }
    }

    fun characterRunning(): Animation<Sprite> = characterRunning

    private fun Sprite.scaleAtOrigin() = apply {
        setOrigin(0f, 0f)
        setScale(scale)
    }

    fun parallaxBackground(): List<TextureRegion> = parallaxAtlas
        .findRegions("background")
        .toList()

    override fun dispose() {
        atlas.dispose()
        parallaxAtlas.dispose()
    }
}

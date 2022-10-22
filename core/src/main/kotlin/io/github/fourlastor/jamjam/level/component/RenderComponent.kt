package io.github.fourlastor.jamjam.level.component

import com.artemis.Component
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class RenderComponent: Component() {
    lateinit var render: Render
    var flipX = false
}


sealed class Render {

    abstract fun draw(batch: SpriteBatch, camera: Camera, flipX: Boolean)
    abstract fun increaseTime(delta: Float)

    abstract val dimensions: Rectangle

    class Blueprint(override val dimensions: Rectangle): Render() {
        override fun draw(batch: SpriteBatch, camera: Camera, flipX: Boolean) = Unit
        override fun increaseTime(delta: Float) = Unit
    }

    class BackgroundRender(
        private val backgroundLayers: List<BackgroundLayer>
    ): Render() {
        override fun draw(batch: SpriteBatch, camera: Camera, flipX: Boolean) {
            backgroundLayers.forEach {
                it.draw(batch, camera)
            }
        }

        override fun increaseTime(delta: Float) = Unit

        override val dimensions: Rectangle = Rectangle()
    }

    class BackgroundLayer(
        private val textureRegion: TextureRegion,
        private val factor: Float,
    ) {
        fun draw(batch: SpriteBatch, camera: Camera) {
            val xOffset = camera.position.x * factor

            batch.draw(
                textureRegion,
                camera.position.x - camera.viewportWidth / 2 - xOffset,
                camera.position.y - camera.viewportHeight / 2,
                textureRegion.regionWidth / 16f,
                textureRegion.regionHeight / 16f,
            )
        }
    }

    class SpriteRender(
        private val sprite: Sprite,
    ): Render() {
        override fun draw(batch: SpriteBatch, camera: Camera, flipX: Boolean) {
            sprite.draw(batch)
        }

        override fun increaseTime(delta: Float) = Unit

        override val dimensions: Rectangle
            get() = sprite.boundingRectangle
    }

    class AnimationRender(
        private val animation: Animation<Sprite>,
        override val dimensions: Rectangle,
    ): Render() {

        private var delta: Float = 0f

        override fun draw(batch: SpriteBatch, camera: Camera, flipX: Boolean) {
            val texture = animation.getKeyFrame(delta)
            val x = if (flipX) dimensions.x + dimensions.width else dimensions.x
            val width = if (flipX) -dimensions.width else dimensions.width
            batch.draw(texture, x, dimensions.y, width, dimensions.height)
        }

        override fun increaseTime(delta: Float) {
            this.delta += delta
        }
    }
}

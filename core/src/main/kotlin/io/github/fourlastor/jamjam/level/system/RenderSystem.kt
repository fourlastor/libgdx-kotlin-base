package io.github.fourlastor.jamjam.level.system

import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import io.github.fourlastor.jamjam.level.component.RenderComponent
import ktx.graphics.use

@All(RenderComponent::class)
class RenderSystem(
    private val camera: Camera,
) : BaseEntitySystem() {

    private lateinit var renders: ComponentMapper<RenderComponent>

    private val batch = SpriteBatch()

    override fun processSystem() {
        batch.use(camera) { batch ->
            val actives = subscription.entities
            val ids = actives.data
            for (i in 0 until actives.size()) {
                process(batch, ids[i])
            }
        }
    }

    private fun process(batch: SpriteBatch, entityId: Int) {
        val renderComponent = renders[entityId]
        renderComponent.render.draw(batch, camera, renderComponent.flipX)
    }

    override fun dispose() {
        batch.dispose()
    }
}

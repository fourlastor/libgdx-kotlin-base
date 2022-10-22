package io.github.fourlastor.jamjam.level.system

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import io.github.fourlastor.jamjam.level.component.PlayerBodyComponent
import io.github.fourlastor.jamjam.level.component.RenderComponent

@All(PlayerBodyComponent::class, RenderComponent::class)
class RenderFollowBodySystem : IteratingSystem() {

    private lateinit var bodies: ComponentMapper<PlayerBodyComponent>
    private lateinit var renders: ComponentMapper<RenderComponent>

    override fun process(entityId: Int) {
        val center = bodies[entityId].body.position
        val render = renders[entityId].render
        render.dimensions.setCenterScaled(center.x, center.y)
    }

    private fun Rectangle.setCenterScaled(x: Float, y: Float) {
        setPosition(x - width  / 2, y - height / 2)
    }
}

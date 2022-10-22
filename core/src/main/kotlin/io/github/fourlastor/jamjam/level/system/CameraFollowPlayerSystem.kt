package io.github.fourlastor.jamjam.level.system

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.badlogic.gdx.graphics.Camera
import io.github.fourlastor.jamjam.extension.SingleEntitySystem
import io.github.fourlastor.jamjam.level.component.PlayerBodyComponent
import io.github.fourlastor.jamjam.level.component.PlayerComponent
import ktx.graphics.update

@All(PlayerBodyComponent::class, PlayerComponent::class)
class CameraFollowPlayerSystem(
    private val camera: Camera,
) : SingleEntitySystem() {

    private lateinit var bodies: ComponentMapper<PlayerBodyComponent>

    private var lastX = camera.position.x

    override fun processSystem() {
        val entity = entity ?: return
        val center = bodies[entity].body.position
        val x = center.x
        if (lastX == x) {
            return
        }
        lastX = x
        camera.update { position.x = x }
    }
}

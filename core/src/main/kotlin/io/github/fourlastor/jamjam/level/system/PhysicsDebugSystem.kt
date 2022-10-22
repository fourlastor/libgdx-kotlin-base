package io.github.fourlastor.jamjam.level.system

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World

class PhysicsDebugSystem(
    private val camera: Camera,
    private val box2dWorld: World,
) : BaseSystem() {

    private val debugRenderer = Box2DDebugRenderer()

    override fun processSystem() {
        debugRenderer.render(box2dWorld, camera.combined)
    }

    override fun dispose() {
        debugRenderer.dispose()
    }

}

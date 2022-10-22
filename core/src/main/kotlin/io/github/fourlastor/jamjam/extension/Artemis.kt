package io.github.fourlastor.jamjam.extension

import com.artemis.BaseEntitySystem
import com.artemis.Component
import com.artemis.World

abstract class SingleEntitySystem: BaseEntitySystem() {

    protected var entity: Int? = null
        private set

    override fun inserted(entityId: Int) {
        entity = entityId
    }
}

abstract class BaseEntityIntervalSystem(
    private val interval: Float
) : BaseEntitySystem() {

    private var accumulator = 0f

    private var intervalDelta = 0f

    override fun checkProcessing(): Boolean {
        accumulator += world.delta
        if (accumulator >= interval) {
            accumulator -= interval
            intervalDelta = accumulator - intervalDelta
            return true
        }
        return false
    }
}


inline fun World.create(action: World.(entityId: Int) -> Unit) = create().also { action(it) }
inline fun <reified T: Component> World.component(entityId: Int, action: T.() -> Unit = {}) = edit(entityId)
    .create(T::class.java).apply(action)

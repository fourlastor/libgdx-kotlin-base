package io.github.fourlastor.jamjam.level.system

import com.artemis.ComponentMapper
import com.artemis.annotations.One
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import io.github.fourlastor.jamjam.extension.BaseEntityIntervalSystem
import io.github.fourlastor.jamjam.level.component.PlayerBodyComponent
import io.github.fourlastor.jamjam.level.component.StaticBodyComponent
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle
import ktx.box2d.filter
import ktx.box2d.loop
import ktx.math.vec2
import kotlin.experimental.or
import kotlin.math.pow

@One(StaticBodyComponent::class, PlayerBodyComponent::class)
class PhysicsSystem(
    private val config: Config,
    private val box2dWorld: World,
) : BaseEntityIntervalSystem(config.step) {

    private lateinit var statics: ComponentMapper<StaticBodyComponent>
    private lateinit var player: ComponentMapper<PlayerBodyComponent>

    override fun processSystem() {
        box2dWorld.step(config.step, 6, 2)
    }

    override fun inserted(entityId: Int) {
        when {
            statics.has(entityId) -> {
                val component = statics[entityId]
                component.body = box2dWorld.body(type = BodyType.StaticBody) {
                    component.boxes.forEach { box ->
                        loop(
                            vec2(box.x, box.y),
                            vec2(box.x + box.width, box.y),
                            vec2(box.x + box.width, box.y + box.height),
                            vec2(box.x, box.y + box.height),
                        ) {
                            filter {
                                categoryBits = CategoryBits.GROUND.bits
                                maskBits = MaskBits.GROUND.bits
                            }
                        }
                    }
                }
            }

            player.has(entityId) -> {
                val component = player[entityId]
                component.body = box2dWorld.body(type = BodyType.DynamicBody) {
                    val box = component.box
                    position.apply {
                        x = box.x + box.width / 2
                        y = box.y + box.height / 2
                    }
                    box(
                        width = box.width,
                        height = box.height,
                    )
                    circle(
                        radius = box.width / 5f,
                        position = vec2(0f, box.height / 2),
                    ) {
                        isSensor = true
                        userData = "foot"
                        filter {
                            categoryBits = CategoryBits.FOOT.bits
                            maskBits = MaskBits.FOOT.bits
                        }
                    }
                }
            }
        }
    }

    override fun removed(entityId: Int) {
        super.removed(entityId)
        if (statics.has(entityId)) {
            box2dWorld.destroyBody(statics[entityId].body)
        }
        if (player.has(entityId)) {
            box2dWorld.destroyBody(player[entityId].body)
        }
    }

    data class Config(
        val step: Float,
    )
}


enum class CategoryBits {
    FOOT,
    GROUND;

    val bits: Short = 2f.pow(ordinal).toInt().toShort()
}

enum class MaskBits(
    vararg masks: CategoryBits,
) {
    FOOT(CategoryBits.GROUND),
    GROUND(CategoryBits.FOOT);

    val bits: Short = masks.fold(0.toShort()) { acc, bits -> acc or bits.bits}
}

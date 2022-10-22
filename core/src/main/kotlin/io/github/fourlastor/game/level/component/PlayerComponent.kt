package io.github.fourlastor.game.level.component

import com.artemis.Component
import io.github.fourlastor.game.level.system.InputState
import io.github.fourlastor.game.level.system.InputStateMachine

class PlayerComponent: Component() {
    lateinit var stateMachine: InputStateMachine
    lateinit var onGround: InputState
    lateinit var jumping: InputState
    lateinit var fallingFromGround: InputState
    lateinit var fallingFromJump: InputState
}

package io.github.fourlastor.jamjam.level.component

import com.artemis.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Body

class PlayerBodyComponent : Component() {
    lateinit var box: Rectangle
    lateinit var body: Body
}

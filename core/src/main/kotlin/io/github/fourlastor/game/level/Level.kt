package io.github.fourlastor.game.level

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle

class Level(
    val statics: LevelStatics,
    val player: Player,
)

class LevelStatics(
    val spriteLayers: List<SpriteLayer>,
    val staticBodies: List<Rectangle>,
)

class SpriteLayer(
    val tiles: List<Sprite>
)

class Player(
    val dimensions: Rectangle,
)

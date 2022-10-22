package io.github.fourlastor.jamjam.level

import com.badlogic.gdx.math.Rectangle
import io.github.fourlastor.jamjam.AssetFactory
import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkEntityInstance
import io.github.fourlastor.ldtk.LDtkLayerInstance
import io.github.fourlastor.ldtk.LDtkLevelDefinition
import io.github.fourlastor.ldtk.LDtkTileInstance

class LDtkConverter(
    private val factory: AssetFactory,
) {

    fun convert(levelDefinition: LDtkLevelDefinition, definitions: Definitions): Level {
        val layerInstances = levelDefinition.layerInstances.orEmpty().reversed()
        return Level(
            statics = LevelStatics(
                spriteLayers = layerInstances
                    .mapNotNull { it.toLayer(definitions) },
                staticBodies = layerInstances.firstOrNull { it.type == "IntGrid" }
                    .toBoxes()
            ),
            player = layerInstances
                .firstOrNull { it.type == "Entities" }
                .let { checkNotNull(it) { "Entities layer missing from level." } }
                .let { layer ->
                    layer.entityInstances
                        .firstOrNull { it.identifier == "Player" }
                        .let { checkNotNull(it) { "Player missing from entity layer." } }
                        .let {
                            Player(
                                dimensions = factory.playerBlueprint(it.x, it.y)
                            )
                        }
                }


        )
    }

    /** Converts an IntGrid layer to definitions used in the physics world. */
    private fun LDtkLayerInstance?.toBoxes(): List<Rectangle> = this?.run {

        fun Int.x() = (this % cWid).toFloat() * gridSize
        fun Int.y() = (this / cWid).toFloat() * gridSize

        intGridCSV.orEmpty()
            .mapIndexedNotNull { index, i ->
                index.takeIf { i == 1 }?.let {
                    factory.box(index.x(), index.y(), gridSize.toFloat())
                }
            }
    }.orEmpty()

    private fun LDtkLayerInstance.toLayer(definitions: Definitions): SpriteLayer? =
        when (type) {
            "AutoLayer" -> {
                definitions.tilesets.find { it.uid == tilesetDefUid }
                    ?.let { tileset ->
                        SpriteLayer(
                            autoLayerTiles.mapNotNull { tile ->
                                tile.t
                                    .let { tileId -> tileset.customData.find { it.tileId == tileId } }
                                    ?.let { factory.tile(
                                        it.data,
                                        tile.x,
                                        tile.y,
                                        tile.flipX,
                                        tile.flipY
                                    ) }
                            })
                    }
            }

            else -> null
        }
}

private val LDtkTileInstance.x: Float
    get() = px[0].toFloat()
private val LDtkTileInstance.y: Float
    get() = px[1].toFloat()
private val LDtkTileInstance.flipX: Boolean
    get() = f and 1 == 1
private val LDtkTileInstance.flipY: Boolean
    get() = f shr 1 and 1 == 1
private val LDtkEntityInstance.x: Float
    get() = px[0].toFloat()
private val LDtkEntityInstance.y: Float
    get() = px[1].toFloat()

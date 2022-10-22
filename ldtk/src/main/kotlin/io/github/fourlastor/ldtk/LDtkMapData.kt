package io.github.fourlastor.ldtk

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

/**
 * From LittleKt
 * https://github.com/littlektframework/littlekt/blob/eeb1e9e73b3eb335664c6995f7f2c138e98fe8da/core/src/commonMain/kotlin/com/lehaine/littlekt/file/ldtk/LDtkJson.kt
 *
 * This file is a JSON schema of files created by LDtk level editor (https://ldtk.io).
 *
 * This is the root of any Project JSON file. It contains: - the project settings, - an array of
 * levels, - and a definition object (that can probably be safely ignored for most users).
 */
@Serializable
data class LDtkMapData(

    /** A structure containing all the definitions of this project */
    val defs: Definitions,

    /**
     * All levels. The order of this array is only relevant in `LinearHorizontal` and
     * `linearVertical` world layouts (see `worldLayout` value). Otherwise, you should refer to the
     * `worldX`,`worldY` coordinates of each Level.
     */
    @SerialName("levels") val levelDefinitions: List<LDtkLevelDefinition>,
)

/**
 * A structure containing all the definitions of this project
 *
 * If you're writing your own LDtk importer, you should probably just ignore *most* stuff in the
 * `defs` section, as it contains data that are mostly important to the editor. To keep you away
 * from the `defs` section and avoid some unnecessary JSON parsing, important data from definitions
 * is often duplicated in fields prefixed with a double underscore (eg. `__identifier` or `__type`).
 * The 2 only definition types you might need here are **Tilesets** and **Enums**.
 */
@Serializable
data class Definitions(
    val enums: List<LDtkEnumDefinition>,
    val tilesets: List<LDtkTilesetDefinition>
)

@Serializable
data class LDtkEnumDefinition(
    val externalFileChecksum: String? = null,

    /** Relative path to the external file providing this Enum */
    val externalRelPath: String? = null,

    /** Tileset UID if provided */
    val iconTilesetUid: Int? = null,

    /** Unique String identifier */
    val identifier: String,

    /** Unique Int identifier */
    val uid: Int,

    /** All possible enum values, with their optional Tile infos. */
    val values: List<LDtkEnumValueDefinition>,

    /** A list of user-defined tags to organize the Enums */
    val tags: List<String> = emptyList()
)

@Serializable
data class LDtkEnumValueDefinition(
    /**
     * An array of 4 Int values that refers to the tile in the tileset image: `[ x, y, width, height
     * ]`
     */
    @SerialName("__tileSrcRect") val tileSrcRect: List<Int>?,

    /** Enum value */
    val id: String,

    /** The optional ID of the tile */
    @SerialName("tileId") val tileID: Int? = null,

    /** The color value of the enum value */
    val color: Int
)

/** Type of the layer as enum possible values: `IntGrid`, `Entities`, `Tiles`, `AutoLayer` */
@Serializable
enum class LDtkLayerType(val value: String) {
  AutoLayer("AutoLayer"),
  Entities("Entities"),
  IntGrid("IntGrid"),
  Tiles("Tiles");

  companion object : KSerializer<LDtkLayerType> {
    override val descriptor: SerialDescriptor
      get() {
        return PrimitiveSerialDescriptor(
            "com.lehaine.littlekt.file.ldtk.Type", PrimitiveKind.STRING)
      }

    override fun deserialize(decoder: Decoder): LDtkLayerType =
        when (val value = decoder.decodeString()) {
          "AutoLayer" -> AutoLayer
          "Entities" -> Entities
          "IntGrid" -> IntGrid
          "Tiles" -> Tiles
          else -> throw IllegalArgumentException("Type could not parse: $value")
        }

    override fun serialize(encoder: Encoder, value: LDtkLayerType) {
      return encoder.encodeString(value.value)
    }
  }
}

/**
 * The `Tileset` definition is the most important part among project definitions. It contains some
 * extra info about each integrated tileset. If you only had to parse one definition section, that
 * would be the one.
 */
@Serializable
data class LDtkTilesetDefinition(

    /** Unique String identifier */
    val identifier: String,

    /** Distance in pixels from image borders */
    val padding: Int,

    /** Image height in pixels */
    val pxHei: Int,

    /** Image width in pixels */
    val pxWid: Int,

    /** Path to the source file, relative to the current project JSON file */
    val relPath: String,

    /** Space in pixels between all tiles */
    val spacing: Int,
    val tileGridSize: Int,

    /** Unique Int identifier */
    val uid: Int,

    /**
     * If this value is set, then it means that this atlas uses an internal LDtk atlas image instead
     * of a loaded one.
     *
     * Possible values: `null`, `LdtkIcons`
     */
    val embedAtlas: String? = null,

    val customData: List<TilesetCustomData>
)

@Serializable
data class TilesetCustomData(
    val tileId: Int,
    val data: String,
)

/**
 * This section contains all the level data. It can be found in 2 distinct forms, depending on
 * Project current settings: - If "*Separate level files*" is **disabled** (default): full level
 * data is *embedded* inside the main Project JSON file, - If "*Separate level files*" is
 * **enabled**: level data is stored in *separate* standalone `.ldtkl` files (one per level). In
 * this case, the main Project JSON file will still contain most level data, except heavy sections,
 * like the `layerInstances` array (which will be null). The `externalRelPath` string points to the
 * `ldtkl` file. A `ldtkl` file is just a JSON file containing exactly what is described below.
 */
@Serializable
data class LDtkLevelDefinition(
    /**
     * Background color of the level (same as `bgColor`, except the default value is automatically
     * used here if its value is `null`)
     */
    @SerialName("__bgColor") val bgColor: String,

    /** Position informations of the background image, if there is one. */
    @SerialName("__bgPos") val bgPos: LDtkLevelBackgroundPositionData? = null,

    /**
     * An array listing all other levels touching this one on the world map. In "linear" world
     * layouts, this array is populated with previous/next levels in array, and `dir` depends on the
     * linear horizontal/vertical layout.
     */
    @SerialName("__neighbours") val neighbours: List<LDtkNeighbourLevelData>?,

    /**
     * Background color of the level. If `null`, the project `defaultLevelBgColor` should be used.
     */
    @SerialName("bgColor") val levelBgColor: String? = null,

    /** Background image X pivot (0-1) */
    val bgPivotX: Float,

    /** Background image Y pivot (0-1) */
    val bgPivotY: Float,

    /**
     * An enum defining the way the background image (if any) is positioned on the level. See
     * `__bgPos` for resulting position info. Possible values: &lt;`null`&gt;, `Unscaled`,
     * `Contain`, `Cover`, `CoverDirty`
     */
    @SerialName("bgPos") val levelBgPos: LDtkBgPos? = null,

    /** The *optional* relative path to the level background image. */
    val bgRelPath: String? = null,

    /**
     * This value is not null if the project option "*Save levels separately*" is enabled. In this
     * case, this **relative** path points to the level Json file.
     */
    val externalRelPath: String? = null,

    /** An array containing this level custom field values. */
    val fieldInstances: List<LDtkFieldInstance>,

    /** Unique String identifier */
    val identifier: String,

    /**
     * An array containing all Layer instances. **IMPORTANT**: if the project option "*Save levels
     * separately*" is enabled, this field will be `null`.<br/> This array is **sorted in display
     * order**: the 1st layer is the top-most and the last is behind.
     */
    val layerInstances: List<LDtkLayerInstance>? = null,

    /** Height of the level in pixels */
    val pxHei: Int,

    /** Width of the level in pixels */
    val pxWid: Int,

    /** Unique Int identifier */
    val uid: Int,

    /**
     * World X coordinate in pixels.
     *
     * Only relevant for world layouts where level spatial positioning is manual (ie. GridVania,
     * Free). For Horizontal and Vertical layouts, the value is always -1 here.
     */
    val worldX: Int,

    /**
     * World Y coordinate in pixels.
     *
     * Only relevant for world layouts where level spatial positioning is manual (ie. GridVania,
     * Free). For Horizontal and Vertical layouts, the value is always -1 here.
     */
    val worldY: Int,

    /** Unique instance identifier */
    val iid: String = "",

    /**
     * Index that represents the "depth" of the level in the world. Default is 0, greater means
     * "above", lower means "below".
     *
     * This value is mostly used for display only and is intended to make stacking of levels easier
     * to manage.
     */
    val worldDepth: Int = 0,
)

/** Level background image position info */
@Serializable
data class LDtkLevelBackgroundPositionData(
    /**
     * An array of 4 float values describing the cropped sub-rectangle of the displayed background
     * image. This cropping happens when original is larger than the level bounds. Array format: `[
     * cropX, cropY, cropWidth, cropHeight ]`
     */
    val cropRect: List<Float>,

    /**
     * An array containing the `[scaleX,scaleY]` values of the **cropped** background image,
     * depending on `bgPos` option.
     */
    val scale: List<Float>,

    /**
     * An array containing the `[x,y]` pixel coordinates of the top-left corner of the **cropped**
     * background image, depending on `bgPos` option.
     */
    val topLeftPx: List<Int>
)

@Serializable
data class LDtkFieldInstance(
    /** Field definition identifier */
    @SerialName("__identifier") val identifier: String,

    /** Type of the field, such as `Int`, `Float`, `Enum(my_enum_name)`, `Bool`, etc. */
    @SerialName("__type") val type: String,

    /**
     * Actual value of the field instance. The value type may vary, depending on `__type` (Integer,
     * Boolean, String etc.)<br/> It can also be an `Array` of those same types.
     */
    @SerialName("__value") val value: MultiAssociatedValue?,

    /** Reference of the **Field definition** UID */
    val defUid: Int,

    /**
     * Optional TilesetRect used to display this field (this can be the field own Tile, or some
     * other Tile guessed from the value, like an Enum)
     */
    @SerialName("__tile") val tile: LDtkTileRect? = null
)

@Serializable(with = MultiAssociatedValueSerializer::class)
data class MultiAssociatedValue(
    val stringList: List<String>? = null,
    val stringMapList: List<Map<String, String>>? = null,
    val stringMap: Map<String, String>? = null,
    val content: String? = null
)

private object MultiAssociatedValueSerializer : KSerializer<MultiAssociatedValue> {
  override val descriptor: SerialDescriptor =
      buildClassSerialDescriptor("com.lehaine.littlekt.file.ldtk.MultiAssociatedValueSerializer") {
        element<List<String>>("stringList", isOptional = true)
        element<List<Map<String, String>>>("stringMapList", isOptional = true)
        element<Map<String, String>>("stringMap", isOptional = true)
        element<String>("content", isOptional = true)
      }

  override fun serialize(encoder: Encoder, value: MultiAssociatedValue) {
    throw NotImplementedError("MultiAssociatedValueSerializer serialization is not supported!")
  }

  override fun deserialize(decoder: Decoder): MultiAssociatedValue {
    val input = decoder as? JsonDecoder ?: error("Unable to cast to JsonDecoder")
    val json = input.decodeJsonElement()
    if (json is JsonArray) {
      val arrList = json.jsonArray
      val isMap = arrList.isNotEmpty() && arrList[0] is JsonObject
      if (isMap) {
        val newList =
            arrList.map { jsonMap ->
              val map = mutableMapOf<String, String>()
              jsonMap.jsonObject.forEach { map[it.key] = it.value.jsonPrimitive.content }
              map
            }
        return MultiAssociatedValue(stringMapList = newList)
      }

      return MultiAssociatedValue(stringList = arrList.map { it.jsonPrimitive.content })
    } else if (json is JsonObject) {
      val map = mutableMapOf<String, String>()
      json.jsonObject.forEach {
        if (it.value is JsonPrimitive) {
          map[it.key] = it.value.jsonPrimitive.content
        }
      }
      return MultiAssociatedValue(stringMap = map)
    }
    return MultiAssociatedValue(content = json.jsonPrimitive.contentOrNull)
  }
}

@Serializable
data class LDtkLayerInstance(
    /** Grid-based height */
    @SerialName("__cHei") val cHei: Int,

    /** Grid-based width */
    @SerialName("__cWid") val cWid: Int,

    /** Grid size */
    @SerialName("__gridSize") val gridSize: Int,

    /** Layer definition identifier */
    @SerialName("__identifier") val identifier: String,

    /** Layer opacity as Float [0-1] */
    @SerialName("__opacity") val opacity: Float,

    /** Total layer X pixel offset, including both instance and definition offsets. */
    @SerialName("__pxTotalOffsetX") val pxTotalOffsetX: Int,

    /** Total layer Y pixel offset, including both instance and definition offsets. */
    @SerialName("__pxTotalOffsetY") val pxTotalOffsetY: Int,

    /** The definition UID of corresponding Tileset, if any. */
    @SerialName("__tilesetDefUid") val tilesetDefUid: Int? = null,

    /** The relative path to corresponding Tileset, if any. */
    @SerialName("__tilesetRelPath") val tilesetRelPath: String? = null,

    /** Layer type (possible values: IntGrid, Entities, Tiles or AutoLayer) */
    @SerialName("__type") val type: String,

    /**
     * An array containing all tiles generated by Auto-layer rules. The array is already sorted in
     * display order (ie. 1st tile is beneath 2nd, which is beneath 3rd etc.).<br/><br/> Note: if
     * multiple tiles are stacked in the same cell as the result of different rules, all tiles
     * behind opaque ones will be discarded.
     */
    val autoLayerTiles: List<LDtkTileInstance>,
    val entityInstances: List<LDtkEntityInstance>,
    val gridTiles: List<LDtkTileInstance>,

    /**
     * **WARNING**: this deprecated value will be *removed* completely on version 0.9.0+ Replaced
     * by: `intGridCsv`
     */
    @Deprecated("Removed in versions 0.9.0+", replaceWith = ReplaceWith("this.intGridCsv"))
    val intGrid: List<LDtkIntGridValueInstance>? = null,

    /**
     * A list of all values in the IntGrid layer, stored from left to right, and top to bottom (ie.
     * first row from left to right, followed by second row, etc). `0` means "empty cell" and
     * IntGrid values start at 1. This array size is `__cWid` x `__cHei` cells.
     */
    @SerialName("intGridCsv") val intGridCSV: List<Int>?,

    /** Reference the Layer definition UID */
    val layerDefUid: Int,

    /** Reference to the UID of the level containing this layer instance */
    @SerialName("levelId") val levelID: Int,

    /** This layer can use another tileset by overriding the tileset UID here. */
    val overrideTilesetUid: Int? = null,

    /**
     * X offset in pixels to render this layer, usually 0 (IMPORTANT: this should be added to the
     * `LayerDef` optional offset, see `__pxTotalOffsetX`)
     */
    val pxOffsetX: Int,

    /**
     * Y offset in pixels to render this layer, usually 0 (IMPORTANT: this should be added to the
     * `LayerDef` optional offset, see `__pxTotalOffsetY`)
     */
    val pxOffsetY: Int,

    /** Random seed used for Auto-Layers rendering */
    val seed: Int,

    /** Layer instance visibility */
    val visible: Boolean,

    /** Unique instance id */
    val iid: String = "",
)

/** This structure represents a single tile from a given Tileset. */
@Serializable
data class LDtkTileInstance(
    /**
     * Internal data used by the editor.
     *
     * For auto-layer tiles: `(ruleId, coordId)`.
     *
     * For tile-layer tiles: `(coordId)`.
     */
    val d: List<Int>,

    /**
     * "Flip bits", a 2-bits integer to represent the mirror transformations of the tile.<br/>
     * - Bit 0 = X flip<br/> - Bit 1 = Y flip<br/>
     *
     * Examples: f=0 (no flip), f=1 (X flip only), f=2 (Y flip only), f=3 (both flips)
     */
    val f: Int,

    /**
     * Pixel coordinates of the tile in the **layer** (`[x,y]` format). Don't forget optional layer
     * offsets, if they exist!
     */
    val px: List<Int>,

    /** Pixel coordinates of the tile in the **tileset** (`[x,y]` format) */
    val src: List<Int>,

    /** The *Tile ID* in the corresponding tileset. */
    val t: Int
)

@Serializable
data class LDtkEntityInstance(
    /** Grid-based coordinates (`[x,y]` format) */
    @SerialName("__grid") val grid: List<Int>,

    /** Entity definition identifier */
    @SerialName("__identifier") val identifier: String,

    /** Pivot coordinates (`[x,y]` format, values are from 0 to 1) of the Entity */
    @SerialName("__pivot") val pivot: List<Float>,

    /**
     * Optional Tile used to display this entity (it could either be the default Entity tile, or
     * some tile provided by a field value, like an Enum).
     */
    @SerialName("__tile") val tile: LDtkTileRect? = null,

    /** Reference of the **Entity definition** UID */
    val defUid: Int,

    /** An array of all custom fields and their values. */
    val fieldInstances: List<LDtkFieldInstance>,

    /**
     * Entity height in pixels. For non-resizable entities, it will be the same as Entity
     * definition.
     */
    val height: Int,

    /**
     * Pixel coordinates (`[x,y]` format) in current level coordinate space. Don't forget optional
     * layer offsets, if they exist!
     */
    val px: List<Int>,

    /**
     * Entity width in pixels. For non-resizable entities, it will be the same as Entity definition.
     */
    val width: Int,

    /** List of tags defined */
    @SerialName("__tags") val tags: List<String> = emptyList(),

    /** Unique instance identifier */
    val iid: String = "",
)

/** Tile data in an Entity instance */
@Serializable
data class LDtkTileRect(
    /**
     * A list of 4 Int values that refers to the tile in the tileset image: `[ x, y, width, height
     * ]`
     */
    @Deprecated(
        "Removed in version 1.0.0", replaceWith = ReplaceWith("this.x\nthis.y\nthis.w\nthis.h"))
    val srcRect: List<Int> = emptyList(),

    /** Tileset UID */
    val tilesetUid: Int,

    /** Height in pixels. */
    val h: Int = 0,

    /** Width in pixels. */
    val w: Int = 0,

    /** X pixel coordinate relative to top-left corner of the tileset image */
    val x: Int = 0,

    /** Y pixel coordinate relative to top-left corner of the tileset image */
    val y: Int = 0,
)

/** IntGrid value instance */
@Serializable
data class LDtkIntGridValueInstance(
    /** Coordinate ID in the layer grid */
    @SerialName("coordId") val coordID: Int,

    /** IntGrid value */
    val v: Int
)

@Serializable
enum class LDtkBgPos(val value: String) {
  Contain("Contain"),
  Cover("Cover"),
  CoverDirty("CoverDirty"),
  Unscaled("Unscaled");

  companion object : KSerializer<LDtkBgPos> {
    override val descriptor: SerialDescriptor
      get() {
        return PrimitiveSerialDescriptor(
            "com.lehaine.littlekt.file.ldtk.BgPos", PrimitiveKind.STRING)
      }

    override fun deserialize(decoder: Decoder): LDtkBgPos =
        when (val value = decoder.decodeString()) {
          "Contain" -> Contain
          "Cover" -> Cover
          "CoverDirty" -> CoverDirty
          "Unscaled" -> Unscaled
          else -> throw IllegalArgumentException("BgPos could not parse: $value")
        }

    override fun serialize(encoder: Encoder, value: LDtkBgPos) {
      return encoder.encodeString(value.value)
    }
  }
}

/** Nearby level info */
@Serializable
data class LDtkNeighbourLevelData(
    /**
     * A single lowercase character tipping on the level location (`n`orth, `s`outh, `w`est,
     * `e`ast).
     */
    val dir: String,

    /** Neighbor instance identifier */
    val levelIid: String = "",
    @Deprecated(
        "Will be removed completely in LDtk '1.2.0+'", replaceWith = ReplaceWith("levelIid"))
    val levelUid: Int = 0
)

/**
 * An enum that describes how levels are organized in this project (ie. linearly or in a 2D space).
 * Possible values: `Free`, `GridVania`, `LinearHorizontal`, `LinearVertical`
 */
@Serializable
enum class LDtkWorldLayout(val value: String) {
  Free("Free"),
  GridVania("GridVania"),
  LinearHorizontal("LinearHorizontal"),
  LinearVertical("LinearVertical");

  companion object : KSerializer<LDtkWorldLayout> {
    override val descriptor: SerialDescriptor
      get() {
        return PrimitiveSerialDescriptor(
            "com.lehaine.littlekt.file.ldtk.WorldLayout", PrimitiveKind.STRING)
      }

    override fun deserialize(decoder: Decoder): LDtkWorldLayout =
        when (val value = decoder.decodeString()) {
          "Free" -> Free
          "GridVania" -> GridVania
          "LinearHorizontal" -> LinearHorizontal
          "LinearVertical" -> LinearVertical
          else -> throw IllegalArgumentException("WorldLayout could not parse: $value")
        }

    override fun serialize(encoder: Encoder, value: LDtkWorldLayout) {
      return encoder.encodeString(value.value)
    }
  }
}

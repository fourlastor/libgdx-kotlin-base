package io.github.fourlastor.ldtk

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class LDtkReader @Inject constructor(
  private val json: Json
) {
  fun data(stream: InputStream): LDtkMapData {
    return json.decodeFromStream(stream)
  }
}

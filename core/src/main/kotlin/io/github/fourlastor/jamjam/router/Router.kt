package io.github.fourlastor.jamjam.router

import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkLevelDefinition

interface Router {

    fun goToMenu()

    fun goToLevel(levelDefinition: LDtkLevelDefinition, defs: Definitions)
}

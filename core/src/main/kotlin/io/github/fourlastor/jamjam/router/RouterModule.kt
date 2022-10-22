package io.github.fourlastor.jamjam.router

import dagger.Module
import dagger.Provides

@Module
class RouterModule constructor(
    private val router: Router,
){

    @Provides
    fun router(): Router = router
}

@file:JvmName("ServerApiInit")

package felis.kittens.core.server

import felis.ModLoader
import felis.kittens.core.Kittens
import felis.kittens.event.LoaderEvents
import felis.kittens.event.MapEventContainer
import felis.side.OnlyIn
import felis.side.Side

@OnlyIn(Side.SERVER)
interface ServerEntrypoint {
    companion object {
        const val KEY = "server"
    }

    fun onClientInit()
}

@Suppress("unused")
@OnlyIn(Side.SERVER)
fun serverApiInit() {
    Kittens.logger.trace("Calling server entrypoint")
    ModLoader.callEntrypoint(ServerEntrypoint.KEY, ServerEntrypoint::onClientInit)
    LoaderEvents.entrypointLoaded.fire(MapEventContainer.JointEventContext(ServerEntrypoint.KEY, Unit))
}
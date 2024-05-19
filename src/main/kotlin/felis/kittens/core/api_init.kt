@file:JvmName("ApiInit")

package felis.kittens.core

import felis.ModLoader
import felis.kittens.event.LoaderEvents
import felis.kittens.event.MapEventContainer
import net.minecraft.server.Bootstrap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface CommonEntrypoint {
    companion object {
        const val KEY = "common"
    }

    fun onInit()
}

object Kittens {
    val logger: Logger = LoggerFactory.getLogger("Kittens API")
    const val MODID = "kittens"
}

fun apiInit() {
    Bootstrap.wrapStreams()
    Kittens.logger.debug("Calling common entrypoint")
    ModLoader.callEntrypoint(CommonEntrypoint.KEY, CommonEntrypoint::onInit)
    LoaderEvents.entrypointLoaded.fire(MapEventContainer.JointEventContext(CommonEntrypoint.KEY, Unit))
}
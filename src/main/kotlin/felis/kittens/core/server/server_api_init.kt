@file:JvmName("ServerApiInit")

package felis.kittens.core.server

import felis.ModLoader
import felis.asm.InjectionPoint
import felis.asm.openMethod
import felis.kittens.core.Kittens
import felis.kittens.event.LoaderEvents
import felis.kittens.event.MapEventContainer
import felis.side.OnlyIn
import felis.side.Side
import felis.transformer.ClassContainer
import felis.transformer.Transformation
import org.objectweb.asm.Type
import java.io.File

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

object MainTransformation : Transformation {
    override fun transform(container: ClassContainer) {
        container.openMethod("main", "([Ljava/lang/String;)V") {
            inject(
                InjectionPoint.Invoke(typeOf(File::class), "<init>", Type.VOID_TYPE, typeOf(String::class), limit = 1)
            ) {
                invokeStatic(locate("felis.kittens.core.server.ServerApiInit"), "serverApiInit")
            }
        }
    }
}
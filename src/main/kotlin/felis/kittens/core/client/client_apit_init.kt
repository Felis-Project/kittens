@file:JvmName("ClientApiInit")

package felis.kittens.core.client

import felis.ModLoader
import felis.asm.InjectionPoint
import felis.asm.openMethod
import felis.kittens.core.Kittens
import felis.kittens.core.event.LoaderEvents
import felis.kittens.core.event.MapEventContainer
import felis.side.OnlyIn
import felis.side.Side
import felis.transformer.ClassContainer
import felis.transformer.Transformation
import net.minecraft.client.Options
import net.minecraft.client.main.GameConfig
import org.objectweb.asm.Type
import java.io.File

@OnlyIn(Side.CLIENT)
interface ClientEntrypoint {
    companion object {
        const val KEY = "client"
    }

    fun onClientInit()
}

@Suppress("unused")
@OnlyIn(Side.CLIENT)
fun clientApiInit() {
    Kittens.logger.trace("Calling client entrypoint")
    ModLoader.callEntrypoint(ClientEntrypoint.KEY, ClientEntrypoint::onClientInit)
    LoaderEvents.entrypointLoaded.fire(MapEventContainer.JointEventContext(ClientEntrypoint.KEY, Unit))
}

object MinecraftTransformation : Transformation {
    override fun transform(container: ClassContainer) {
        container.openMethod("<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(GameConfig::class.java))) {
            inject(
                InjectionPoint.Invoke(
                    typeOf(Options::class),
                    "<init>",
                    Type.VOID_TYPE,
                    typeOf(owner),
                    typeOf(File::class),
                    limit = 1
                )
            ) {
                invokeStatic(locate("felis.kittens.core.client.ClientApiInit"), "clientApiInit")
            }
        }
    }
}
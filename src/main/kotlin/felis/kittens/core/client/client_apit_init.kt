@file:JvmName("ClientApiInit")

package felis.kittens.core.client

import felis.ModLoader
import felis.kittens.core.Kittens
import felis.kittens.event.LoaderEvents
import felis.kittens.event.MapEventContainer
import felis.side.OnlyIn
import felis.side.Side
import felis.transformer.ClassContainer
import felis.transformer.Transformation
import net.minecraft.client.Options
import net.minecraft.client.main.GameConfig
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.MethodInsnNode

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
    Kittens.logger.debug("Calling client entrypoint")
    ModLoader.callEntrypoint(ClientEntrypoint.KEY, ClientEntrypoint::onClientInit)
    LoaderEvents.entrypointLoaded.fire(MapEventContainer.JointEventContext(ClientEntrypoint.KEY, Unit))
}

object MinecraftTransformation : Transformation {
    /*
    this.toast = new ToastComponent(this);
    this.gameThread = Thread.currentThread();
    =======================================================
    ClientApiInit.clientApiInit();                        |
    =======================================================
    this.options = new Options(this, this.gameDirectory);
    RenderSystem.setShaderGlintAlpha(this.options.glintStrength().get());
    this.running = true;
     */
    override fun transform(container: ClassContainer) {
        container.node { node ->
            node.methods.first {
                it.name == "<init>" && it.desc == Type.getMethodDescriptor(
                    Type.VOID_TYPE,
                    Type.getType(GameConfig::class.java)
                )
            }.apply {
                val target = instructions.first {
                    it is MethodInsnNode &&
                            it.owner == Type.getType(Options::class.java).internalName &&
                            it.name == "<init>"
                }
                instructions.insertBefore(
                    target,
                    MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "felis/kittens/core/client/ClientApiInit",
                        "clientApiInit",
                        "()V"
                    )
                )
            }
        }
    }
}
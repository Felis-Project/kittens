package felis.kittens.pack

import felis.ModLoader
import felis.kittens.core.Kittens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.*
import net.minecraft.server.packs.metadata.MetadataSectionSerializer
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.Pack.Metadata
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier
import net.minecraft.server.packs.repository.PackCompatibility
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.server.packs.repository.RepositorySource
import net.minecraft.server.packs.resources.IoSupplier
import net.minecraft.world.flag.FeatureFlagSet
import java.io.InputStream
import java.nio.file.Files
import java.util.Optional
import java.util.function.Consumer
import kotlin.io.path.*
import kotlin.streams.asSequence

// TODO: Reloadable listener
object ModPackSource : RepositorySource {
    override fun loadPacks(registrar: Consumer<Pack>) {
        val modPacks = ModLoader.discoverer.mods.asSequence()
            .map { it.metadata.modid }
            // since MC is considered a mod, including it leads to resources going missing ;)
            .filter { it != Kittens.MODID && it != "minecraft" }
            .map { ModPackResources(it) }
            .toList()
        val primary = ModPackResources(Kittens.MODID)
        val pack = CompositePackResources(primary, modPacks)

        registrar.accept(
            Pack(
                PackLocationInfo(
                    Kittens.MODID,
                    Component.translatable("kittens.resources"),
                    PackSource.DEFAULT,
                    Optional.empty()
                ),
                SimpleReferenceSupplier(pack),
                Metadata(
                    Component.translatable("kittens.resources"),
                    PackCompatibility.COMPATIBLE,
                    FeatureFlagSet.of(),
                    listOf()
                ),
                PackSelectionConfig(true, Pack.Position.TOP, true)
            )
        )
    }
}

data class SimpleReferenceSupplier(val ref: PackResources) : ResourcesSupplier {
    override fun openPrimary(locInfo: PackLocationInfo): PackResources = this.ref
    override fun openFull(locInfo: PackLocationInfo, meta: Metadata): PackResources = this.ref
}

data class ModPackResources(private val modid: String) : PackResources {
    private val mod = ModLoader.discoverer.mods.first { it.metadata.modid == this.modid }
    private val location by lazy {
        PackLocationInfo(
            this.modid,
            Component.translatable("kittens.resources"),
            PackSource.DEFAULT,
            Optional.empty()
        )
    }

    override fun close() {}

    override fun getRootResource(vararg path: String): IoSupplier<InputStream>? =
        this.getResource(path.joinToString("/"))

    override fun getResource(type: PackType, path: ResourceLocation): IoSupplier<InputStream>? =
        this.getResource("${type.directory}/${path.namespace}/${path.path}")

    private fun getResource(path: String): IoSupplier<InputStream>? =
        this.mod.getContentPath(path)?.let { IoSupplier.create(it) }

    override fun listResources(type: PackType, namespace: String, path: String, output: PackResources.ResourceOutput) {
        val rootPath = this.mod.getContentPath("${type.directory}/$namespace") ?: return
        val targetPath = rootPath / path

        if (!targetPath.exists()) return

        Files.walk(targetPath)
            .asSequence()
            .filter { !it.isDirectory() }
            .forEach {
                output.accept(
                    ResourceLocation.fromNamespaceAndPath(namespace, it.relativeTo(rootPath).pathString),
                    IoSupplier.create(it)
                )
            }
    }

    override fun getNamespaces(type: PackType): MutableSet<String> =
        this.mod.getContentPath(type.directory)?.let { path ->
            Files.list(path).use { stream ->
                stream.asSequence()
                    .filter { it.isDirectory() }
                    .map { it.name }
                    .toCollection(mutableSetOf())
            }
        } ?: mutableSetOf()

    override fun <T : Any?> getMetadataSection(ser: MetadataSectionSerializer<T>): T? = null
    override fun location(): PackLocationInfo = this.location
    override fun packId(): String = this.modid
}
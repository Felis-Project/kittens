package felis.kittens.keymapping

import felis.transformer.ClassContainer
import felis.transformer.Transformation
import net.minecraft.client.KeyMapping
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

// TODO: Make this better?
object KeyMappingRegistry {
    private val customMappings: MutableSet<KeyMapping> = hashSetOf()
    private val categories: MutableMap<String, Int> by lazy {
        val categoryMap = Class.forName("net.minecraft.client.KeyMapping").getDeclaredField("CATEGORY_SORT_ORDER")
        categoryMap.isAccessible = true
        @Suppress("UNCHECKED_CAST") // we do hacky stuff so yea
        categoryMap.get(null) as MutableMap<String, Int>
    }

    @JvmStatic
    fun register(mapping: KeyMapping) {
        this.addCategory(mapping.category)
        if (!this.customMappings.add(mapping)) {
            throw IllegalArgumentException("Mapping ${mapping.name} has already been registered")
        }
    }

    private fun addCategory(category: String) {
        if (categories[category] != null) return
        this.categories[category] = this.categories.size + 1
    }

    fun inject(oldMappings: Array<KeyMapping>): Array<KeyMapping> {
        val res = mutableListOf(*oldMappings)
        res.removeAll(this.customMappings)
        res.addAll(this.customMappings)
        return res.toTypedArray()
    }
}

// TODO: Temporary, use Mutable once it's merged
object OptionsTransformation : Transformation {
    override fun transform(container: ClassContainer) {
        // implement a pseudo mutable field
        container.node.fields
            .first { it.name == "keyMappings" && it.desc == Type.getDescriptor(Array<KeyMapping>::class.java) }
            .let { it.access = it.access and Opcodes.ACC_FINAL.inv() }
    }
}
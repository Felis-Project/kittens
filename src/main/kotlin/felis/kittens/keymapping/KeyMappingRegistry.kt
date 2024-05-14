package felis.kittens.keymapping

import net.minecraft.client.KeyMapping

// TODO: Make this better?
object KeyMappingRegistry {
    private val customMappings: MutableSet<KeyMapping> = hashSetOf()

    @JvmStatic
    fun register(mapping: KeyMapping) {
        this.addCategory(mapping.category)
        if (!this.customMappings.add(mapping)) {
            throw IllegalArgumentException("Mapping ${mapping.name} has already been registered")
        }
    }

    private fun addCategory(category: String) {
        if (KeyMapping.CATEGORY_SORT_ORDER[category] != null) return
        KeyMapping.CATEGORY_SORT_ORDER[category] = KeyMapping.CATEGORY_SORT_ORDER.size + 1
    }

    fun inject(oldMappings: Array<KeyMapping>): Array<KeyMapping> {
        val res = mutableListOf(*oldMappings)
        res.removeAll(this.customMappings)
        res.addAll(this.customMappings)
        return res.toTypedArray()
    }
}
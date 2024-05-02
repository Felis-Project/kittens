package felis.kittens.keymapping

import net.minecraft.client.KeyMapping

// TODO: Make this better?
object KeyMappingRegistry {
    private val customMappings: MutableSet<KeyMapping> = hashSetOf()
    // TODO: Replace with access transformer/widener once it's added
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
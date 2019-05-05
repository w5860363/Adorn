package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.block.SuffixedBlockItemModel
import io.github.cottonmc.jsonfactory.gens.block.SuffixedLootTable
import io.github.cottonmc.jsonfactory.plugin.Plugin

object AdornPlugin : Plugin {
    //adorn:red,adorn:black,adorn:green,adorn:brown,adorn:blue,adorn:purple,adorn:cyan,adorn:light_gray,adorn:gray,adorn:pink,adorn:lime,adorn:yellow,adorn:light_blue,adorn:magenta,adorn:orange,adorn:white
    //adorn:oak,adorn:spruce,adorn:birch,adorn:jungle,adorn:acacia,adorn:dark_oak

    private val planksItem = fun(id: Identifier) = Identifier.mc(id.path + "_planks")
    private val slabItem = fun(id: Identifier) = Identifier.mc(id.path + "_slab")
    private val woolItem = fun(id: Identifier) = Identifier.mc(id.path + "_wool")

    private fun constItem(const: Identifier) = fun(_: Identifier) = const

    val SOFA = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Sofas
    )
    val CHAIR = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Chairs
    )
    val TABLE = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Tables
    )
    val KITCHEN = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Kitchen
    )
    val DRAWER = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Drawers
    )
    val OTHER = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Other
    )

    override val generators: List<ContentGenerator> = listOf(
        SofaBlockModel,
        SofaBlockState,
        SofaItemModel,
        SuffixedLootTable("Sofa", "sofa", SOFA),
        SofaRecipe,
        SuffixedRecipeAdvancementGenerator(
            "Sofa Recipe Advancement",
            SOFA,
            "sofa",
            keyItems = listOf(woolItem)
        ),

        ChairBlockModel,
        ChairBlockState,
        SuffixedBlockItemModel("Chair", "chair", CHAIR),
        SuffixedLootTable("Chair", "chair", CHAIR),
        ChairRecipe,
        SuffixedRecipeAdvancementGenerator(
            "Chair Recipe Advancement",
            CHAIR,
            "chair",
            keyItems = listOf(slabItem)
        ),

        TableBlockModel,
        TableBlockState,
        TableItemModel,
        SuffixedLootTable("Table", "table", TABLE),
        TableRecipe,
        SuffixedRecipeAdvancementGenerator(
            "Table Recipe Advancement",
            TABLE,
            "table",
            keyItems = listOf(slabItem)
        ),

        KitchenCounterBlockModel,
        KitchenCounterBlockState,
        SuffixedBlockItemModel("Kitchen Counter", "kitchen_counter", KITCHEN),
        SuffixedLootTable("Kitcher Counter", "kitchen_counter", KITCHEN),
        KitchenCounterRecipe,
        SuffixedRecipeAdvancementGenerator(
            "Kitchen Counter Recipe Advancement",
            KITCHEN,
            "kitchen_counter",
            keyItems = listOf(planksItem)
        ),

        KitchenCupboardBlockModel,
        KitchenCupboardBlockState,
        KitchenCupboardItemModel,
        SuffixedLootTable("Kitcher Cupboard", "kitchen_cupboard", KITCHEN),
        KitchenCupboardRecipe,
        SuffixedRecipeAdvancementGenerator(
            "Kitchen Cupboard Recipe Advancement",
            KITCHEN,
            "kitchen_cupboard",
            keyItems = listOf(planksItem, { it.suffixPath("_kitchen_counter") })
        ),

        DrawerBlockModel,
        DrawerBlockState,
        SuffixedBlockItemModel("Drawer", "drawer", DRAWER),
        SuffixedLootTable("Drawer", "drawer", DRAWER),
        DrawerRecipe,
        SuffixedRecipeAdvancementGenerator(
            "Drawer Recipe Advancement",
            DRAWER,
            "drawer",
            keyItems = listOf(slabItem)
        ),

        RecipeAdvancementGenerator(
            "Trading Station Recipe Advancement",
            OTHER,
            keyItems = listOf(constItem(Identifier.mc("emerald")))
        )
    )

    object AdornCategory : GeneratorInfo.Category {
        override val description = null
        override val displayName = "Adorn"
        override val placeholderTexturePath = null
    }

    enum class Subcategories(override val displayName: String, override val description: String? = null) : GeneratorInfo.Subcategory {
        Sofas("Sofas"),
        Chairs("Chairs"),
        Tables("Tables"),
        Kitchen("Kitchen"),
        Drawers("Drawer"),
        Other("Other"),
    }

    @JvmStatic
    fun main(args: Array<String>) {
        io.github.cottonmc.jsonfactory.gui.main(
            arrayOf(AdornPlugin::class.qualifiedName!!)
        )
    }
}

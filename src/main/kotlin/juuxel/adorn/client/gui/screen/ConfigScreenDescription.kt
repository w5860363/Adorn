package juuxel.adorn.client.gui.screen

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WToggleButton
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.NoticeScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.StringRenderable
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import kotlin.reflect.KMutableProperty

@Environment(EnvType.CLIENT)
class ConfigScreenDescription(previous: Screen) : LightweightGuiDescription() {
    internal var restartRequired: Boolean = false

    init {
        val root = WPlainPanel()
        setRootPanel(root)

        root.add(WLabel(TranslatableText("gui.adorn.config.title"), Colors.WHITE).setHorizontalAlignment(HorizontalAlignment.CENTER), 0, 0, 11 * 18, 18)

        // val tabbed = WTabbedPanel()
        val config = ConfigManager.CONFIG

        val general = WGridPanel()
        with(general) {
            add(createConfigToggle(config::skipNightOnSofas), 0, 0)
            add(createConfigToggle(config::protectTradingStations), 0, 1)
            add(createConfigToggle(config::sittingOnTables, true), 0, 2)
            add(createConfigToggle(config.client::showTradingStationTooltips), 0, 3)

            backgroundPainter = BackgroundPainter.VANILLA
            setSize(11 * 18, height)
        }

        val advanced = WGridPanel()
        with(advanced) {
            backgroundPainter = BackgroundPainter.VANILLA
            setSize(11 * 18, height)
        }

        // tabbed.addTab(TranslatableText("gui.adorn.config.category.general"), general)
        // tabbed.addTab(TranslatableText("gui.adorn.config.category.advanced"), advanced)

        root.add(general, 0, 18 + 5, general.width, general.height)
        root.add(
            WButton(TranslatableText("gui.done")).apply {
                setOnClick { close(previous) }
            },
            11 * 9 - 5 * 9, 6 * 18, 5 * 18, 18
        )
        root.validate(this)
    }

    override fun addPainters() {
        // Overridden to disable the default root panel painter
    }

    fun close(previous: Screen) {
        if (restartRequired) {
            MinecraftClient.getInstance().openScreen(
                NoticeScreen(
                    { MinecraftClient.getInstance().openScreen(previous) },
                    TranslatableText("gui.adorn.config.restart_required.title"),
                    TranslatableText("gui.adorn.config.restart_required.message"),
                    TranslatableText("gui.ok")
                )
            )
        } else {
            MinecraftClient.getInstance().openScreen(previous)
        }
    }

    private fun createConfigToggle(property: KMutableProperty<Boolean>, restartRequired: Boolean = false): WWidget =
        object : WToggleButton(TranslatableText("gui.adorn.config.option.${property.name}")) {
            init {
                toggle = property.getter.call()
            }

            override fun onToggle(on: Boolean) {
                property.setter.call(on)
                ConfigManager.save()

                if (restartRequired) {
                    this@ConfigScreenDescription.restartRequired = true
                }
            }

            override fun addTooltip(tooltip: MutableList<StringRenderable>) {
                tooltip += TranslatableText("gui.adorn.config.option.${property.name}.tooltip")

                if (restartRequired) {
                    tooltip += TranslatableText("gui.adorn.config.requires_restart")
                        .formatted(Formatting.ITALIC, Formatting.GOLD)
                }
            }
        }
}

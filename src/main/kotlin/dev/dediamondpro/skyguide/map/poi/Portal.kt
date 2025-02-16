package dev.dediamondpro.skyguide.map.poi

import dev.dediamondpro.skyguide.config.Config
import dev.dediamondpro.skyguide.map.navigation.*
import dev.dediamondpro.skyguide.utils.GuiUtils
import dev.dediamondpro.skyguide.utils.RenderUtils
import gg.essential.universal.UMinecraft
import kotlinx.serialization.Serializable

/**
 * @param name The name of the portal
 * @param destination The destination of the portal, null if the portal is command only
 * @param command The command to teleport to the portal, null if no command available
 * @param x The X coordinate of the portal
 * @param y The Y coordinate of the portal
 * @param z The Z coordinate of the portal
 */
@Serializable
data class Portal(
    val name: String = "",
    val destination: String? = null,
    val command: String? = null,
    val mvp: Boolean = false,
    override val x: Float,
    override val y: Float,
    override val z: Float,
) : PointOfInterest(), NavigationProvider {
    override val destinations = if (destination == null) listOf() else listOf(destination)

    override fun shouldDraw(): Boolean {
        return command != null && (!mvp || Config.showMVPWarps)
    }

    override fun drawIcon(x: Float, y: Float) {
        RenderUtils.drawImage(
            "/assets/skyguide/portal.png",
            x - 6f,
            y - 9f,
            12f,
            18f
        )
    }

    override fun getTooltip(): List<String> {
        return mutableListOf("Warp to $name", "Left Click to teleport", "Right Click to navigate")
    }

    override fun onLeftClick() {
        UMinecraft.getMinecraft().thePlayer.sendChatMessage("/$command")
    }

    override fun onRightClick() {
        NavigationHandler.navigateTo(Destination(island!!, x, y, z, name))
    }

    override fun getAction(destination: Destination): NavigationAction = PortalAction(this, destination)
}

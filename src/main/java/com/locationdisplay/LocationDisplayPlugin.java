package com.locationdisplay;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Objects;

@Slf4j
@PluginDescriptor(
		name = "Location Display",
		description = "Shows the name of a location upon entering"
)
public class LocationDisplayPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private LocationDisplayConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private LocationDisplayOverlay overlay;

	@Getter
	@Setter
	private WorldPoint playerPosition = new WorldPoint(0,0,0);;

	@Getter
	@Setter
	private String lastArea = "";

	@Getter
	@Setter
	private Region playerRegion = new Region(-1, -1);

	@Inject
	private RegionMap regionMap;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		regionMap.loadFromJson();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		playerPosition = client.getLocalPlayer().getWorldLocation();

		//converting RegionID to region coordinates
		int currentX = playerPosition.getRegionID() >> 8;
		int currentY = playerPosition.getRegionID() & 0xFF;

		if (playerPosition.getRegionX() != currentX || playerPosition.getRegionY() != currentY) {
			playerRegion.setX(currentX);
			playerRegion.setY(currentY);
			String currentArea = regionMap.getAreaName(playerRegion);

			if (!Objects.equals(currentArea, lastArea)) {
				lastArea = currentArea;
			}
		}

	}

	@Provides
	LocationDisplayConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LocationDisplayConfig.class);
	}
}

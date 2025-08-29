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

@Slf4j
@PluginDescriptor(
		name = "Location Display",
		description = "Displays name of the current location upon entering area"
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
	private String currentArea = "";

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
		regionMap.clear();
	}

	private void updatePlayerRegion(WorldPoint playerPosition) {
		int currentX = playerPosition.getRegionID() >> 8;
		int currentY = playerPosition.getRegionID() & 0xFF;

		if (playerRegion.getX() != currentX || playerRegion.getY() != currentY) {
			playerRegion.setX(currentX);
			playerRegion.setY(currentY);
			setCurrentArea(regionMap.getAreaName(playerRegion));

			//log.info("Player region changed: Area: {},ID = {}, X = {}, Y = {}", currentArea, playerPosition.getRegionID(), currentX, currentY);
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		WorldPoint playerPosition = client.getLocalPlayer().getWorldLocation();
		updatePlayerRegion(playerPosition);
	}

	@Provides
	LocationDisplayConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LocationDisplayConfig.class);
	}
}

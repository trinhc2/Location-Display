package com.locationdisplay;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Objects;

@Slf4j
@PluginDescriptor(
	name = "Location Display"
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

	private final RegionMap regionMap = new RegionMap();

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);

		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		playerPosition = client.getLocalPlayer().getWorldLocation();

		int currentX = playerPosition.getRegionID() >> 8;
		int currentY = playerPosition.getRegionID() & 0xFF;

		if (playerPosition.getRegionX() != currentX || playerPosition.getRegionY() != currentY) {
			playerRegion.setX(currentX);
			playerRegion.setY(currentY);
			String currentArea = regionMap.getAreaName(playerRegion);

			if (!Objects.equals(currentArea, lastArea)) {
				lastArea = currentArea;
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", lastArea, null);

			}
		}

	}

	@Provides
	LocationDisplayConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LocationDisplayConfig.class);
	}
}

package com.locationdisplay;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.BackgroundComponent;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import java.awt.*;

public class LocationDisplayOverlay extends Overlay {

    private final Client client;
    private final LocationDisplayConfig config;
    private final LocationDisplayPlugin plugin;
    //private final BackgroundComponent backgroundComponent = new BackgroundComponent();
    private final TextComponent textComponent = new TextComponent();

    @Inject
    private LocationDisplayOverlay(Client client, LocationDisplayPlugin plugin, LocationDisplayConfig config) {

        setPosition(OverlayPosition.TOP_CENTER);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        textComponent.setText("itemString");
        textComponent.setColor(Color.WHITE);
        textComponent.setPosition(new java.awt.Point(50, 50));
        textComponent.render(graphics);

        return null;
    }
}

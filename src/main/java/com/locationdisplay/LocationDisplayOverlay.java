package com.locationdisplay;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import java.awt.*;

public class LocationDisplayOverlay extends Overlay {

    private final Client client;
    private final LocationDisplayConfig config;
    private final LocationDisplayPlugin plugin;
    private final TextComponent textComponent = new TextComponent();
    private long fadeStartTime = 0;
    private float fadeDuration = 1000f;
    private float holdDuration = 2000f;
    private float alpha = 0f;
    private String lastArea = "";

    private enum FadeState { IDLE, FADING_IN, HOLDING, FADING_OUT }
    private FadeState fadeState = FadeState.IDLE;

    @Inject
    private LocationDisplayOverlay(Client client, LocationDisplayPlugin plugin, LocationDisplayConfig config) {

        setPosition(OverlayPosition.TOP_CENTER);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        String currentArea = plugin.getLastArea();

        if (!currentArea.equals(lastArea)) {
            fadeStartTime = System.currentTimeMillis();
            fadeState = FadeState.FADING_IN;
            lastArea = currentArea;
        }

        // Skip everything if not fading
        if (fadeState == FadeState.IDLE)
        {
            return null;
        }

        long elapsed = System.currentTimeMillis() - fadeStartTime;

        switch (fadeState)
        {
            case FADING_IN:
                alpha = Math.min(1f, elapsed / fadeDuration);
                if (elapsed >= fadeDuration)
                {
                    fadeState = FadeState.HOLDING;
                    fadeStartTime = System.currentTimeMillis();
                    alpha = 1f;
                }
                break;

            case HOLDING:
                alpha = 1f;
                if (elapsed >= holdDuration)
                {
                    fadeState = FadeState.FADING_OUT;
                    fadeStartTime = System.currentTimeMillis();
                }
                break;

            case FADING_OUT:
                alpha = Math.max(0f, 1f - (elapsed / fadeDuration));
                if (elapsed >= fadeDuration)
                {
                    fadeState = FadeState.IDLE;
                    alpha = 0f;
                }
                break;
        }

        Color fadeColor = new Color(1f, 1f, 1f, alpha);
        textComponent.setColor(fadeColor);
        textComponent.setPosition(new Point(50, 50));
        textComponent.setText(currentArea);

        // Included because runelite doesn't render 0 alpha completely, 0 alpha will still leave text
        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        textComponent.render(graphics);
        graphics.setComposite(originalComposite);

        return null;
    }
}

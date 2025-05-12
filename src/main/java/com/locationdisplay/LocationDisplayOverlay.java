package com.locationdisplay;

import net.runelite.client.ui.FontManager;
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
    private float alpha = 0f;
    private String lastArea = "";
    private boolean suppressNextArea;
    private enum FadeState { IDLE, FADING_IN, HOLDING, FADING_OUT }
    private FadeState fadeState = FadeState.IDLE;

    @Inject
    private LocationDisplayOverlay(Client client, LocationDisplayPlugin plugin, LocationDisplayConfig config) {

        setPosition(OverlayPosition.TOP_CENTER);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.suppressNextArea = config.suppressOnLogin();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        String currentArea = plugin.getLastArea();

        if (!currentArea.equals(lastArea)) {
            lastArea = currentArea;
            if (!lastArea.equals("Unknown Area")) {
                fadeStartTime = System.currentTimeMillis();
                fadeState = FadeState.FADING_IN;
            }
        }

        // Skip everything if not fading
        if (fadeState == FadeState.IDLE || suppressNextArea)
        {
            suppressNextArea = false;
            return null;
        }

        long elapsed = System.currentTimeMillis() - fadeStartTime;

        switch (fadeState)
        {
            case FADING_IN:
                alpha = Math.min(1f, (float) elapsed / config.fadeDuration());
                if (elapsed >= config.fadeDuration())
                {
                    fadeState = FadeState.HOLDING;
                    fadeStartTime = System.currentTimeMillis();
                    alpha = 1f;
                }
                break;

            case HOLDING:
                alpha = 1f;
                if (elapsed >= config.holdDuration())
                {
                    fadeState = FadeState.FADING_OUT;
                    fadeStartTime = System.currentTimeMillis();
                }
                break;

            case FADING_OUT:
                alpha = Math.max(0f, 1f - ((float) elapsed / config.fadeDuration()));
                if (elapsed >= config.fadeDuration())
                {
                    fadeState = FadeState.IDLE;
                    alpha = 0f;
                }
                break;
        }

        //Setting text component properties
        Font font;
        switch (config.font()) {
            case Regular:
                font = FontManager.getRunescapeFont().deriveFont((float) config.fontSize());
                break;
            case Bold:
                font = FontManager.getRunescapeBoldFont().deriveFont((float) config.fontSize());
                break;
            case Small:
                font = FontManager.getRunescapeSmallFont().deriveFont((float) config.fontSize());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + config.font());
        }
        textComponent.setFont(font);

        textComponent.setOutline(config.outline());

        Color fadeColor = new Color(1f, 1f, 1f, alpha);
        textComponent.setColor(fadeColor);

        if (client.isResized()) {
            textComponent.setPosition(new Point(0, config.textHeight()));
        }
        else {
            //if not resized, top center offset is directly in middle so have to adjust by string width
            int stringWidth = graphics.getFontMetrics(font).stringWidth(lastArea) / 2;
            textComponent.setPosition(new Point(-stringWidth, config.textHeight()));
        }

        textComponent.setText(currentArea);

        // Included because runelite doesn't render 0 alpha completely, 0 alpha will still leave text
        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        textComponent.render(graphics);
        graphics.setComposite(originalComposite);

        return null;
    }
}

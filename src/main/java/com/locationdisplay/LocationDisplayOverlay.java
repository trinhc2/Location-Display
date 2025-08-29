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
    private String displayedArea = null;
    private boolean suppressFirstLocation;
    private enum FadeState { IDLE, FADING_IN, HOLDING, FADING_OUT }
    private FadeState fadeState = FadeState.IDLE;
    private static final long SOUND_COOLDOWN_MS = 5000; // 5 seconds cooldown
    private long lastSoundTime = 0;


    @Inject
    private LocationDisplayOverlay(Client client, LocationDisplayPlugin plugin, LocationDisplayConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.suppressFirstLocation = config.suppressOnLogin();
    }

    private Font getFontFromConfig() {
        Font baseFont;

        switch (config.font()) {
            case Regular:
                baseFont = FontManager.getRunescapeFont();
                break;
            case Bold:
                baseFont = FontManager.getRunescapeBoldFont();
                break;
            case Small:
                baseFont = FontManager.getRunescapeSmallFont();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + config.font());
        }

        int style = baseFont.getStyle();
        if (config.italic()) {
            style |= Font.ITALIC;
        }

        return baseFont.deriveFont(style, (float) config.fontSize());
    }

    private OverlayPosition getPositionFromConfig() {
        switch (config.position()) {
            case TOP_LEFT:
                return OverlayPosition.TOP_LEFT;
            case TOP_RIGHT:
                return OverlayPosition.TOP_RIGHT;
            case TOP_CENTER:
                return OverlayPosition.TOP_CENTER;
            case BOTTOM_LEFT:
                return OverlayPosition.BOTTOM_LEFT;
            case BOTTOM_RIGHT:
                return OverlayPosition.BOTTOM_RIGHT;
            default:
                throw new IllegalStateException("Unexpected value: " + config.position());
        }
    }

    private Point calculateTextPosition(Graphics2D graphics, Font font, String text) {
        int y = config.textYOffset();
        int x = config.textXOffset();
        int stringWidth = graphics.getFontMetrics(font).stringWidth(text);
        OverlayPosition position = getPositionFromConfig();

        switch (position) {
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return new Point(x,y);
            case TOP_CENTER:
                return new Point(x - (stringWidth / 2),y);
            default:
                return new Point(x - stringWidth, y);
        }
    }

    private void drawUnderline(Graphics2D graphics, Font font, Color fadeColor, Point position){
        FontMetrics metrics = graphics.getFontMetrics(font);
        int textWidth = metrics.stringWidth(displayedArea);

        graphics.setColor(fadeColor);

        Stroke originalStroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke((float) config.underlineThickness()));

        graphics.drawLine(position.x - config.underlineWidth(), position.y + config.underlineHeight(), position.x + textWidth + config.underlineWidth(), position.y + config.underlineHeight());

        graphics.setStroke(originalStroke);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        String currentArea = plugin.getCurrentArea();

        if (!currentArea.equals(lastArea)) {
            lastArea = currentArea;
            if (!lastArea.equals("Unknown Area")) {
                String suffix = new StringBuilder(config.prefixSuffix()).reverse().toString();
                displayedArea = config.prefixSuffix() + lastArea + suffix;
                fadeStartTime = System.currentTimeMillis();
                fadeState = FadeState.FADING_IN;

                if (config.soundEffect()){
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - lastSoundTime >= SOUND_COOLDOWN_MS) {
                        client.playSoundEffect(config.soundEffectID());
                        lastSoundTime = currentTime;
                    }
                }
            }
        }

        // Skip everything if not fading
        if (fadeState == FadeState.IDLE || suppressFirstLocation || displayedArea == null)
        {
            suppressFirstLocation = false;
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

        Font font = getFontFromConfig();
        textComponent.setFont(font);

        textComponent.setOutline(config.outline());

        Color baseColor = config.colorConfig();
        Color fadeColor = new Color(baseColor.getRed() / 255f, baseColor.getGreen() / 255f, baseColor.getBlue() / 255f, alpha);
        textComponent.setColor(fadeColor);

        setPosition(getPositionFromConfig());

        Point textPosition = calculateTextPosition(graphics, font, displayedArea);
        textComponent.setPosition(textPosition);

        textComponent.setText(displayedArea);

        if (config.underline()){
            drawUnderline(graphics, font, fadeColor, textPosition);
        }

        // Included because runelite doesn't render 0 alpha completely, 0 alpha will still leave text
        Composite originalComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        textComponent.render(graphics);
        graphics.setComposite(originalComposite);

        return null;
    }
}

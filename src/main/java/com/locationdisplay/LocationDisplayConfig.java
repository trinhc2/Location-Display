package com.locationdisplay;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("example")
public interface LocationDisplayConfig extends Config
{
	@ConfigItem(
			position = 1,
			keyName = "textPositionConfig",
			name = "Text Position",
			description = "Set position of text overlay"
	)
	@Range(min = -200)
	default Dimension textPositionConfig() { return new Dimension(0, 60);
	}

	@ConfigItem(
			position = 2,
			keyName = "fadeConfig",
			name = "Fade Duration (ms)",
			description = "Sets the duration of fading in/fading out in milliseconds"
	)
	default int fadeConfig() { return 1000; }

	@ConfigItem(
			position = 3,
			keyName = "fadeConfig",
			name = "Hold Duration (ms)",
			description = "Sets the duration of holding after fading in in milliseconds"
	)
	default int holdConfig() { return 2000; }

	@ConfigItem(
			position = 4,
			keyName = "fontSizeConfig",
			name = "Font Size",
			description = "Sets the font size"
	)
	default int fontSizeConfig() { return 32; }

	@ConfigItem(
			position = 5,
			keyName = "suppressOnLoginConfig",
			name = "Suppress on login",
			description = "Prevents location name from showing immediately after login"
	)
	default boolean suppressOnLoginConfig() { return false; }
}

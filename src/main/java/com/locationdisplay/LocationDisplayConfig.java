package com.locationdisplay;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface LocationDisplayConfig extends Config
{
	@ConfigItem(
			position = 1,
			keyName = "textHeight",
			name = "Text Height",
			description = "Set the height of text overlay, higher number = lower on screen"
	)
	default int textHeight() { return 60;}

	@ConfigItem(
			position = 2,
			keyName = "fadeDuration",
			name = "Fade Duration (ms)",
			description = "Sets the duration of fading in/fading out in milliseconds"
	)
	default int fadeDuration() { return 1000; }

	@ConfigItem(
			position = 3,
			keyName = "holdDuration",
			name = "Hold Duration (ms)",
			description = "Sets the duration of holding after fading in in milliseconds"
	)
	default int holdDuration() { return 2000; }

	@ConfigItem(
			position = 4,
			keyName = "fontSize",
			name = "Font Size",
			description = "Sets the font size"
	)
	default int fontSize() { return 32; }

	@ConfigItem(
			position = 5,
			keyName = "outline",
			name = "Outline",
			description = "Outlines the text"
	)
	default boolean outline() { return false; }

	enum FontEnum {
		Small,
		Regular,
		Bold
	}

	@ConfigItem(
			position = 6,
			keyName = "Font",
			name = "Font Style",
			description = "Select font style"
	)
	default FontEnum font() { return FontEnum.Bold; }

	@ConfigItem(
			position = 7,
			keyName = "suppressOnLogin",
			name = "Suppress on login",
			description = "Prevents location name from showing immediately after login"
	)
	default boolean suppressOnLogin() { return false; }
}

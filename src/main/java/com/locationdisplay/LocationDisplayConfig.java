package com.locationdisplay;

import lombok.AllArgsConstructor;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("Location Text Configuration")
public interface LocationDisplayConfig extends Config
{
	@AllArgsConstructor
	enum PositionEnum {
		TOP_LEFT("Top Left"),
		TOP_CENTER("Top Center"),
		TOP_RIGHT("Top Right"),
		BOTTOM_LEFT("Bottom Left"),
		BOTTOM_RIGHT("Bottom Right");

		private final String name;

		@Override
		public String toString() {
			return name;
		}
	}

	@ConfigItem(
			position = 1,
			keyName = "position",
			name = "Position",
			description = "Set text position. You will likely have to play around with text height if changed."
	)
	default PositionEnum position() { return PositionEnum.TOP_CENTER; }

	@Range(min = -1000)
	@ConfigItem(
			position = 2,
			keyName = "textHeight",
			name = "Text Height",
			description = "Set the height of text overlay, higher number = lower on screen"
	)
	default int textHeight() { return 60;}

	@ConfigItem(
			position = 3,
			keyName = "fadeDuration",
			name = "Fade Duration (ms)",
			description = "Sets the duration of fading in/fading out in milliseconds"
	)
	default int fadeDuration() { return 1000; }

	@ConfigItem(
			position = 4,
			keyName = "holdDuration",
			name = "Hold Duration (ms)",
			description = "Sets the duration of holding after fading in in milliseconds"
	)
	default int holdDuration() { return 2000; }

	@ConfigItem(
			position = 5,
			keyName = "fontSize",
			name = "Font Size",
			description = "Sets the font size"
	)
	default int fontSize() { return 32; }

	@ConfigItem(
			position = 6,
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
			position = 7,
			keyName = "Font",
			name = "Font Style",
			description = "Select font style"
	)
	default FontEnum font() { return FontEnum.Bold; }

	@ConfigItem(
			position = 8,
			keyName = "suppressOnLogin",
			name = "Suppress on login",
			description = "Prevents location name from showing immediately after login"
	)
	default boolean suppressOnLogin() { return false; }
}

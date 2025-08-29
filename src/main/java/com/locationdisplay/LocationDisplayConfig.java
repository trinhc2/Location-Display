package com.locationdisplay;

import lombok.AllArgsConstructor;
import net.runelite.client.config.*;

import java.awt.*;

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

	@ConfigSection(
			name = "Position Settings",
			description = "Settings relating to position",
			position = 1,
			closedByDefault = true
	)
	String positionSettings = "positionSettings";

	@ConfigSection(
			name = "Font Settings",
			description = "Settings relating to font",
			position = 2,
			closedByDefault = true
	)
	String fontSettings = "fontSettings";

	@ConfigSection(
			name = "Fade Settings",
			description = "Settings relating to fade",
			position = 2,
			closedByDefault = true
	)
	String fadeSettings = "fadeSettings";

	@ConfigSection(
			name = "Underline Settings",
			description = "Settings relating to underline",
			position = 6,
			closedByDefault = true
	)
	String underlineSettings = "underlineSettings";

	@ConfigSection(
			name = "Sound Effect Settings",
			description = "Settings relating to sound effects",
			position = 10,
			closedByDefault = true
	)
	String soundEffectSettings = "soundEffectSettings";

	@ConfigItem(
			position = 1,
			keyName = "position",
			name = "Position",
			description = "Set text position. You will likely have to play around with text height if changed.",
			section = positionSettings
	)
	default PositionEnum position() { return PositionEnum.TOP_CENTER; }

	@Range(min = -1000)
	@ConfigItem(
			position = 3,
			keyName = "textYOffset",
			name = "Text Y Offset",
			description = "Set the Y position of text overlay, higher number = lower on screen",
			section = positionSettings
	)
	default int textYOffset() { return 60;}

	@Range(min = -1000)
	@ConfigItem(
			position = 2,
			keyName = "textXOffset",
			name = "Text X Offset",
			description = "Set the X position of text overlay, higher number = more right on screen",
			section = positionSettings
	)
	default int textXOffset() { return 0;}

	@ConfigItem(
			position = 4,
			keyName = "fadeDuration",
			name = "Fade Duration (ms)",
			description = "Sets the duration of fading in/fading out in milliseconds",
			section = fadeSettings
	)
	default int fadeDuration() { return 1000; }

	@ConfigItem(
			position = 5,
			keyName = "holdDuration",
			name = "Hold Duration (ms)",
			description = "Sets the duration of holding after fading in in milliseconds",
			section = fadeSettings
	)
	default int holdDuration() { return 2000; }

	@ConfigItem(
			position = 6,
			keyName = "fontSize",
			name = "Font Size",
			description = "Sets the font size",
			section = fontSettings
	)
	default int fontSize() { return 32; }

	@ConfigItem(
			position = 7,
			keyName = "outline",
			name = "Outline",
			description = "Outlines the text",
			section = fontSettings
	)
	default boolean outline() { return false; }

	enum FontEnum {
		Small,
		Regular,
		Bold
	}

	@ConfigItem(
			position = 8,
			keyName = "Font",
			name = "Font Style",
			description = "Select font style",
			section = fontSettings
	)
	default FontEnum font() { return FontEnum.Bold; }

	@ConfigItem(
			position = 50,
			keyName = "suppressOnLogin",
			name = "Suppress on Login",
			description = "Prevents location name from showing immediately after login"
	)
	default boolean suppressOnLogin() { return false; }

	@ConfigItem(
			position = 10,
			keyName = "Color",
			name = "Text Color",
			description = "Select the color of the text",
			section = fontSettings
	)
	default Color colorConfig() { return Color.WHITE; }

	@ConfigItem(
			position = 15,
			keyName = "underline",
			name = "Underline",
			description = "Underlines the text",
			section = underlineSettings
	)
	default boolean underline() { return false; }

	@ConfigItem(
			position = 16,
			keyName = "underlineThickness",
			name = "Underline Thickness",
			description = "Sets the underline thickness",
			section = underlineSettings
	)
	default int underlineThickness() { return 2; }

	@ConfigItem(
			position = 17,
			keyName = "underlineWidth",
			name = "Underline Width",
			description = "Adds additional width to underline",
			section = underlineSettings
	)
	default int underlineWidth() { return 5; }

	@Range(min = -1000)
	@ConfigItem(
			position = 18,
			keyName = "underlineHeight",
			name = "Underline Height",
			description = "Adjust height of underline",
			section = underlineSettings
	)
	default int underlineHeight() { return -2; }

	@ConfigItem(
			position = 25,
			keyName = "italic",
			name = "Italic",
			description = "Adds italic to font",
			section = fontSettings
	)
	default boolean italic() { return false; }

	@ConfigItem(
			position = 30,
			keyName = "Prefix/Suffix",
			name = "Prefix/Suffix",
			description = "Adds a prefix and suffix to the name"
	)
	default String prefixSuffix() { return ""; }

	@ConfigItem(
			position = 32,
			keyName = "soundEffect",
			name = "Sound Effect",
			description = "Plays a sound effect aswell",
			section = soundEffectSettings
	)
	default boolean soundEffect() { return false; }

	@ConfigItem(
			position = 33,
			keyName = "effectid",
			name = "Sound Effect ID",
			description = "Change sound effect, check out: https://oldschool.runescape.wiki/w/List_of_sound_IDs",
			section = soundEffectSettings
	)
	default int soundEffectID() { return 4218; }

	@ConfigItem(
			position = 34,
			keyName = "soundCooldown",
			name = "Sound Effect Cooldown (ms)",
			description = "Set the cooldown before playing the sound effect again",
			section = soundEffectSettings
	)
	default int soundEffectCooldown() { return 5000; }

}

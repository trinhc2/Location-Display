# Location Display
The Location Display plugin displays the name of the location you are in upon entering. 391 current locations!

![Preview](/media/preview.gif)

## Customising your sounds

### 1. Locate your `.runelite` folder

On windows this is likely to be here: `C:\Users\<your username>\.runelite`

If you aren't sure, it's the same place that stores your `settings.properties`

Within this `.runelite` folder, there should be a `location-display` folder, which is where the custom sound should be stored.

### 2. Prepare your sound files

Make sure your file is a `.wav` format (just changing the extension won't work, actually convert them)

Make sure the file name __exactly__ matches `custom.wav`

### 3. If it fails to play your sound

Check that your file is actually a valid `.wav` and not just a renamed `.mp3` or similar

Check that the file is named `custom.wav`, if you accidentally used an incorrect file name it won't work.

File path should end up being `C:\Users\<your username>\.runelite\location-display\custom.wav`

Check that you have selected `Custom` in the plugin settings under Sound Settings > Sound.
## Help
If you've experienced an issue with a location or have any recommendations please [create an issue](https://github.com/trinhc2/Location-Display/issues).

Feel free to help out by editing the resources/Locations.json file.

## Note / Thanks
Not every area is included, namely, quest specific areas and those not appearing on mejrs' map.

This plugin is based on the [Runescape Region Grid](https://mejrs.github.io/osrs), therefore, some areas may not be 100% accurate. Big thanks to mejrs.

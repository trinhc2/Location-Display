package com.locationdisplay;

import net.runelite.client.RuneLite;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class SoundEngine {

    private final LocationDisplayConfig config;
    private final AudioPlayer audioPlayer;

    private static final Path DATA_FOLDER = Path.of(RuneLite.RUNELITE_DIR.getPath(), "location-display");
    private static final String CUSTOM_SOUND_FILE = "custom.wav";

    // Constructor Injection
    @Inject
    public SoundEngine(LocationDisplayConfig config, AudioPlayer audioPlayer) {
        this.config = config;
        this.audioPlayer = audioPlayer;
    }

    public void ensureDownloadDirectoryExists() {
        try {
            if (!Files.exists(DATA_FOLDER)) {
                Files.createDirectories(DATA_FOLDER);
                log.info("folder created at {}", DATA_FOLDER);
            }
        } catch (FileAlreadyExistsException ignored) {
            log.info("folder exists at {}", DATA_FOLDER);
            /* ignored */
        } catch (IOException e) {
            log.error("Could not create download directory or warning file", e);
        }
    }

    public void playCustomSound() {
        float gain = 20f * (float) Math.log10(config.soundEffectVolume()/ 100f);
        try {
            audioPlayer.play(DATA_FOLDER.resolve(CUSTOM_SOUND_FILE).toFile(), gain);
        } catch (Exception e) {
            log.warn("Failed to load sound", e);
        }
    }
}

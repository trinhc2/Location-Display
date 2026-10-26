package com.locationdisplay;

import net.runelite.client.RuneLite;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.util.Filepath;

@Slf4j
@Singleton
public class SoundEngine {

    private final LocationDisplayConfig config;
    private final AudioPlayer audioPlayer;
    private Filepath filePath;

    private static final String CUSTOM_SOUND_FILE = "custom.wav";

    // Constructor Injection
    @Inject
    public SoundEngine(LocationDisplayConfig config, AudioPlayer audioPlayer) {
        this.config = config;
        this.audioPlayer = audioPlayer;
    }

    public void initialize(Filepath filePath) {
        this.filePath = filePath;
        ensureDownloadDirectoryExists();
    }

    public void ensureDownloadDirectoryExists() {
        if (filePath == null) {
            log.warn("Sound engine has not been initialized");
            return;
        }

        try {
            filePath.createDirectories();
        }
        catch (IOException e) {
            log.error("Could not create plugin data directory", e);
        }
    }

    public void playCustomSound() {
        if (filePath == null) {
            log.warn("Cannot play custom sound: sound engine is not initialized");
            return;
        }
        float gain = 20f * (float) Math.log10(config.soundEffectVolume()/ 100f);
        try {
            Filepath soundFile = filePath.join(CUSTOM_SOUND_FILE);
            audioPlayer.play(soundFile, gain);
        }
        catch (Exception e) {
            log.warn("Failed to load custom sound", e);
        }
    }
}

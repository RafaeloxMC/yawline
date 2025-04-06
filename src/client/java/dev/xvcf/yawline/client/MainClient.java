package dev.xvcf.yawline.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.xvcf.yawline.client.input.KeyManager;
import dev.xvcf.yawline.client.input.keys.OpenMenuKey;
import dev.xvcf.yawline.client.input.keys.SetYawPitchKey;
import dev.xvcf.yawline.client.util.Logger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainClient implements ClientModInitializer {

    private static MainClient instance;
    private static final String MOD_ID = "yawline";
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private boolean debug;
    private Logger logger;
    private KeyManager keyManager;

    public static final float[] yawValues = new float[5];
    public static final float[] pitchValues = new float[5];

    private boolean ticked = false;
    public void tick() {
        if(!ticked) {
            ticked = true;
            keyManager.register(OpenMenuKey.getMapping(), new OpenMenuKey());
            keyManager.registerFromArray(SetYawPitchKey.getMapping(), new SetYawPitchKey());

            keyManager.inject();
        }
        if(getClient().isRunning()) {
            keyManager.tick();
        }
    }

    @Override
    public void onInitializeClient() {
        instance = this;

        keyManager = new KeyManager();

        System.setProperty("java.awt.headless", "false");
        debug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
        logger = new Logger();

        if (debug) {
            logger.console_debug("Debug mode enabled");
        }

        // INITIALIZE CONFIG FILE
        loadConfig();

        logger.console_info("Yawline (Client) Initialized");
    }

    public void loadConfig() {
        try {
            File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "yawline/config.json");
            logger.console_info("Loading configuration from " + configFile.getAbsolutePath());
            if (configFile.exists()) {
                String content = Files.readString(configFile.toPath());
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();

                for (int i = 0; i < 5; i++) {
                    yawValues[i] = json.get("yaw" + i).getAsFloat();
                    pitchValues[i] = json.get("pitch" + i).getAsFloat();
                }

                logger.console_info("Configuration loaded successfully.");
            } else {
                logger.console_warn("Configuration file not found. Generating a new one.");
                generateConfigFile();
                loadConfig();
            }
        } catch (IOException | JsonSyntaxException e) {
            logger.console_error("Failed to load configuration: " + e.getMessage());
        }
    }

    public void generateConfigFile() {
        try {
            File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "yawline/config.json");
            logger.ingame_info("Creating new config file at " + configFile.getAbsolutePath());
            if (!configFile.exists()) {
                if(!configFile.getParentFile().mkdirs()) {
                    logger.console_error("Failed to create config directory.");
                    return;
                }
                if(!configFile.createNewFile()) {
                    logger.console_error("Failed to create config file.");
                    return;
                }
                writeConfigFromSaved(configFile);
                logger.console_info("Configuration file generated successfully.");
            }
        } catch (IOException e) {
            logger.console_error("Failed to generate configuration file: " + e.getMessage());
        }
    }

    public void saveConfig() {
        try {
            File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "yawline/config.json");
            logger.console_info("Saving configuration to " + configFile.getAbsolutePath());
            writeConfigFromSaved(configFile);
            logger.console_info("Configuration saved successfully.");
        } catch (IOException e) {
            logger.console_error("Failed to save configuration: " + e.getMessage());
        }
    }

    private void writeConfigFromSaved(File configFile) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder("{\n");
        for (int i = 0; i < 5; i++) {
            jsonBuilder.append("\"yaw").append(i).append("\": ").append(yawValues[i]).append(",\n");
            jsonBuilder.append("\"pitch").append(i).append("\": ").append(pitchValues[i]);
            if (i < 4) {
                jsonBuilder.append(",\n");
            } else {
                jsonBuilder.append("\n");
            }
        }
        jsonBuilder.append("}");
        Files.writeString(configFile.toPath(), jsonBuilder.toString());
    }

    public static MainClient getInstance() {
        return instance;
    }

    public MinecraftClient getClient() {
        return client;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isDebug() {
        return debug;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
}

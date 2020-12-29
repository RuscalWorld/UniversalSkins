package ru.ruscalworld.universalskins.systems.elyby;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ruscalworld.universalskins.UniversalSkins;
import ru.ruscalworld.universalskins.skins.Property;
import ru.ruscalworld.universalskins.skins.Textures;
import ru.ruscalworld.universalskins.systems.SkinSystem;
import ru.ruscalworld.universalskins.systems.mojang.MojangResponse;
import ru.ruscalworld.universalskins.util.ResultHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Collectors;

public class ElybySkinSystem extends SkinSystem {

    private final static String TEXTURE_URL = "http://skinsystem.ely.by/textures/signed/%username%";

    private final UniversalSkins plugin;

    public ElybySkinSystem(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    @Override
    public void getTextures(String username, ResultHandler<Textures> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(TEXTURE_URL.replace("%username%", username));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        callback.handle(null);
                        return;
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String result = bufferedReader.lines().collect(Collectors.joining());

                    JsonMapper mapper = new JsonMapper();
                    MojangResponse response = mapper.readValue(result, MojangResponse.class);

                    Property property = response.properties.get(0);
                    callback.handle(new Textures(property.value, property.signature));
                } catch (Exception ignored) {
                    callback.handle(null);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public void getTextures(UUID uuid, ResultHandler<Textures> callback) {
        throw new UnsupportedOperationException("Ely.by doesn't allow to get skins by UUID");
    }

    @Override
    public String getName() {
        return "elyby";
    }

    @Override
    public String getSkinUrl(String name) {
        return "http://skinsystem.ely.by/skins/" + name + ".png";
    }

    @Override
    public String getHeadUrl(String name) {
        return "https://ely.by/services/skins-renderer?url=" + getSkinUrl(name) + "&scale=20&renderFace=1";
    }
}
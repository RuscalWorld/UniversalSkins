package ru.ruscalworld.universalskins.systems.mojang;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ruscalworld.universalskins.UniversalSkins;
import ru.ruscalworld.universalskins.skins.Textures;
import ru.ruscalworld.universalskins.util.ResultHandler;
import ru.ruscalworld.universalskins.util.UuidHelper;
import ru.ruscalworld.universalskins.skins.Property;
import ru.ruscalworld.universalskins.systems.SkinSystem;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.stream.Collectors;

public class MojangSkinSystem extends SkinSystem {

    private final static String TEXTURE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%uuid%?unsigned=false";
    private final static String USERNAME_URL = "https://api.mojang.com/profiles/minecraft";

    private final UniversalSkins plugin;

    public MojangSkinSystem(UniversalSkins plugin) {
        this.plugin = plugin;
    }

    // Делаем HTTP запросы вручную: хардкор, только хардкор!
    // Да, можно было использовать готовые библиотеки для работы с Mojang API, но религия не позволяет
    // Кроме того, там костыли и их много, не смотрите, это просто говнокод
    @Override
    public void getTextures(String username, ResultHandler<Textures> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(USERNAME_URL);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write("[\"" + username + "\"]");
                    writer.close();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        callback.handle(null);
                        return;
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String result = bufferedReader.lines().collect(Collectors.joining());

                    JsonMapper mapper = new JsonMapper();
                    UsersResponse response = mapper.readValue("{\"response\":" + result + "}", UsersResponse.class);
                    String id = response.response.get(0).id;

                    getTextures(UuidHelper.fromTrimmed(id), callback);
                } catch (Exception ignored) {
                    callback.handle(null);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    // То же самое, что и выше, только не получает UUID по никнейму, а сразу получает текстуры по UUID игрока
    // Ещё немного говнокода, короче
    @Override
    public void getTextures(UUID uuid, ResultHandler<Textures> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(TEXTURE_URL.replace("%uuid%", uuid.toString().replace("-", "")));
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
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
    public String getSkinUrl(String name) {
        return "https://minotar.net/skin/" + name;
    }

    @Override
    public String getHeadUrl(String name) {
        return "https://minotar.net/avatar/" + name;
    }

    @Override
    public String getName() {
        return "mojang";
    }
}
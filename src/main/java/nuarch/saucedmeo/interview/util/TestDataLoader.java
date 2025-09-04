package nuarch.saucedmeo.interview.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nuarch.saucedmeo.interview.model.ConfigData;
import nuarch.saucedmeo.interview.model.LoginData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestDataLoader {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private TestDataLoader() {}

    public static <T> List<T> loadListFromResource(String resourceName, Class<T> elementClass) {
        try (Reader reader = openReader(resourceName)) {
            Type listType = TypeToken.getParameterized(List.class, elementClass).getType();
            return GSON.fromJson(reader, listType);
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Failed to load JSON list from '" + resourceName + "'", e);
        }
    }

    public static <T> T loadObjectFromResource(String resourceName, Class<T> clazz) {
        try (Reader reader = openReader(resourceName)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Failed to load JSON object from '" + resourceName + "'", e);
        }
    }

    public static List<LoginData> loadLoginDataFromResource(String resourceName) {
        return loadListFromResource(resourceName, LoginData.class);
    }

    public static ConfigData loadConfig(String resourceName) {
        return loadObjectFromResource(resourceName, ConfigData.class);
    }

    private static Reader openReader(String resourceName) {
        InputStream inputStream = TestDataLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new IllegalStateException("Resource not found on classpath: " + resourceName);
        }

        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

}

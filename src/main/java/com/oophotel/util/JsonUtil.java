package com.oophotel.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public final class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                    (com.google.gson.JsonSerializer<LocalDate>) (src, t, ctx) ->
                            new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalTime.class,
                    (com.google.gson.JsonSerializer<LocalTime>) (src, t, ctx) ->
                            new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_TIME)))
            .registerTypeAdapter(LocalDate.class,
                    (com.google.gson.JsonDeserializer<LocalDate>) (json, t, ctx) ->
                            LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalTime.class,
                    (com.google.gson.JsonDeserializer<LocalTime>) (json, t, ctx) ->
                            LocalTime.parse(json.getAsString()))
            .create();

    private JsonUtil() {}

    /**
     * Writes a JSON envelope response:
     *   success → { "success": true,  "data":  <payload> }
     *   error   → { "success": false, "error": <payload> }
     */
    public static void write(HttpServletResponse res, int status, Object payload) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setStatus(status);

        JsonObject envelope = new JsonObject();
        envelope.addProperty("success", status < 400);
        if (status < 400) envelope.add("data",  GSON.toJsonTree(payload));
        else              envelope.add("error", GSON.toJsonTree(payload));

        res.getWriter().write(GSON.toJson(envelope));
    }

    /** Reads the full request body and deserializes it into the given class. */
    public static <T> T readBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining());
        if (body == null || body.isBlank()) return null;
        return GSON.fromJson(body, clazz);
    }
}

package com.oophotel.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonUtilTest {

    @Test
    void writeSuccessEnvelope() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        JsonUtil.write(response, 200, Map.of("id", 7, "name", "Ada"));

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);

        String written = body.toString();
        assertTrue(written.contains("\"success\":true"));
        assertTrue(written.contains("\"data\""));
        assertTrue(written.contains("\"id\":7"));
        assertTrue(written.contains("\"name\":\"Ada\""));
    }

    @Test
    void writeErrorEnvelope() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        JsonUtil.write(response, 400, "Bad request");

        verify(response).setStatus(400);

        String written = body.toString();
        assertTrue(written.contains("\"success\":false"));
        assertTrue(written.contains("\"error\":\"Bad request\""));
    }

    @Test
    void readBodyReturnsDeserializedObject() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String json = "{\"name\":\"Ada\",\"age\":30}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        Person parsed = JsonUtil.readBody(request, Person.class);

        assertEquals("Ada", parsed.name);
        assertEquals(30, parsed.age);
    }

    @Test
    void readBodyReturnsNullForBlank() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));

        assertNull(JsonUtil.readBody(request, Person.class));
    }

    @Test
    void readBodyReturnsNullForWhitespace() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("   \n  ")));

        assertNull(JsonUtil.readBody(request, Person.class));
    }

    private static class Person {
        String name;
        int age;
    }
}

package ru.netology.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

class MessageSenderImplTest {

    private GeoService geoService;
    private LocalizationService localizationService;
    private MessageSenderImpl messageSender;

    @BeforeEach
    void setUp() {
        geoService = mock(GeoService.class);
        localizationService = mock(LocalizationService.class);
        messageSender = new MessageSenderImpl(geoService, localizationService);
    }

    @Test
    void testRussianMessageForRussianIP() {
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        Location russianLocation = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        when(geoService.byIp("172.123.12.19")).thenReturn(russianLocation);
        when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        String result = messageSender.send(headers);

        assertEquals("Добро пожаловать", result);
        verify(geoService, times(1)).byIp("172.123.12.19");
        verify(localizationService, times(1)).locale(Country.RUSSIA);
    }

    @Test
    void testEnglishMessageForUSIP() {
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");

        Location usLocation = new Location("New York", Country.USA, "10th Avenue", 32);
        when(geoService.byIp("96.44.183.149")).thenReturn(usLocation);
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        String result = messageSender.send(headers);

        assertEquals("Welcome", result);
        verify(geoService, times(1)).byIp("96.44.183.149");
        verify(localizationService, times(1)).locale(Country.USA);
    }

    @Test
    void testEnglishMessageForOtherIP() {
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "8.8.8.8");

        when(geoService.byIp("8.8.8.8")).thenReturn(null);
        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        String result = messageSender.send(headers);

        assertEquals("Welcome", result);
        verify(geoService, times(1)).byIp("8.8.8.8");
        verify(localizationService, times(1)).locale(Country.USA);
    }

    @Test
    void testEnglishMessageWhenNoIPInHeaders() {
        Map<String, String> headers = new HashMap<>();

        when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        String result = messageSender.send(headers);

        assertEquals("Welcome", result);
        verify(geoService, never()).byIp(anyString());
        verify(localizationService, times(1)).locale(Country.USA);
    }
}
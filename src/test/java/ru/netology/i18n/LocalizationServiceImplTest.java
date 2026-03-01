package ru.netology.i18n;

import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceImplTest {

    private LocalizationServiceImpl localizationService = new LocalizationServiceImpl();

    @Test
    void testLocaleRussia() {
        String message = localizationService.locale(Country.RUSSIA);
        assertEquals("Добро пожаловать", message);
    }

    @Test
    void testLocaleUSA() {
        String message = localizationService.locale(Country.USA);
        assertEquals("Welcome", message);
    }
}
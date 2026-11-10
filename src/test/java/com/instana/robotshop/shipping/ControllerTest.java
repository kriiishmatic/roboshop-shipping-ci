package com.instana.robotshop.shipping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerTest {

    private Controller controller;
    private CityRepository cityRepository;
    private CodeRepository codeRepository;

    @BeforeEach
    void setUp() {
        controller = new Controller();
        cityRepository = mock(CityRepository.class);
        codeRepository = mock(CodeRepository.class);

        ReflectionTestUtils.setField(controller, "cityrepo", cityRepository);
        ReflectionTestUtils.setField(controller, "coderepo", codeRepository);
    }

    @Test
    void healthReturnsOk() {
        assertEquals("OK", controller.health());
    }

    @Test
    void countReturnsCityRepositoryCountAsString() {
        when(cityRepository.count()).thenReturn(12L);

        assertEquals("12", controller.count());
    }

    @Test
    void citiesReturnsCitiesForRequestedCountryCode() {
        City city = new City();
        city.setCode("GB");
        city.setCity("London");
        when(cityRepository.findByCode("GB")).thenReturn(Collections.singletonList(city));

        List<City> result = controller.cities("GB");

        assertEquals(1, result.size());
        assertEquals("London", result.get(0).getCity());
    }

    @Test
    void matchRejectsSearchTextShorterThanThreeCharacters() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> controller.match("GB", "Lo")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void matchReturnsRepositoryResultsWhenWithinLimit() {
        City city = new City();
        city.setCity("London");
        when(cityRepository.match("GB", "Lon")).thenReturn(Collections.singletonList(city));

        List<City> result = controller.match("GB", "Lon");

        assertEquals(1, result.size());
        assertEquals("London", result.get(0).getCity());
    }

    @Test
    void matchLimitsResultsToNineWhenRepositoryReturnsMoreThanTen() {
        List<City> cities = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            City city = new City();
            city.setCity("City-" + i);
            cities.add(city);
        }
        when(cityRepository.match("GB", "Cit")).thenReturn(cities);

        List<City> result = controller.match("GB", "Cit");

        // This asserts the current implementation's subList(0, 9) behavior.
        assertEquals(9, result.size());
    }

    @Test
    void calcReturnsNotFoundWhenCityDoesNotExist() {
        when(cityRepository.findById(99L)).thenReturn(null);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> controller.caclc(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("city not found", exception.getReason());
    }

    @Test
    void calcReturnsShippingDistanceAndCostForExistingCity() {
        City city = new City();
        city.setLatitude(51.164896);
        city.setLongitude(7.068792);
        when(cityRepository.findById(1L)).thenReturn(city);

        Ship result = controller.caclc(1L);

        assertEquals(0, result.getDistance());
        assertEquals(0.0, result.getCost(), 0.0001);
    }
}

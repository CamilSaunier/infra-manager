package com.inframanager.equipment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EquipmentTest {

    @Test
    public void shouldCreateEquipmentWithValidData() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );

        assertEquals("Routeur principal", equipment.getName());
        assertEquals("Cisco", equipment.getManufacturer());
        assertEquals("Catalyst 9200", equipment.getModel());


    }

    @Test
    public void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Equipment(
                        "",
                        "Cisco",
                        "Catalyst 9200"
                )

        );
    }

    @Test
    public void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Equipment(
                        null,
                        "Cisco",
                        "Catalyst 9200"
                )

        );
    }


    @Test
    public void shouldThrowExceptionWhenManufacturerIsBlank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Equipment(
                        "Routeur principal",
                        "",
                        "Catalyst 9200"
                )

        );
    }

    @Test
    public void shouldThrowExceptionWhenManufacturerIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Equipment(
                        "Routeur principal",
                        null,
                        "Catalyst 9200"
                )

        );
    }

    @Test
    public void shouldThrowExceptionWhenModelIsBlank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Equipment(
                        "Routeur principal",
                        "Cisco",
                        ""
                )

        );
    }

    @Test
    public void shouldThrowExceptionWhenModelIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Equipment(
                        "Routeur principal",
                        "Cisco",
                        null
                )

        );
    }

    @Test
    public void shouldSetValidIpAddress() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );
        equipment.setIpAddress("192.168.1.10");
        assertEquals("192.168.1.10", equipment.getIpAddress());

    }

    @Test
    public void shouldThrowExceptionWhenIpAddressOctetIsGreaterThan255() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Equipment equipment = new Equipment(
                            "Routeur principal",
                            "Cisco",
                            "Catalyst 9200"
                    );

                    equipment.setIpAddress("192.168.1.256");
                }
        );
    }

    @Test
    public void shouldThrowExceptionWhenIpAddressHasInvalidNumberOfParts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Equipment equipment = new Equipment(
                            "Routeur principal",
                            "Cisco",
                            "Catalyst 9200"
                    );

                    equipment.setIpAddress("192.168.1");
                }
        );
    }

    @Test
    public void shouldThrowExceptionWhenIpAddressContainsNonNumericCharacters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Equipment equipment = new Equipment(
                            "Routeur principal",
                            "Cisco",
                            "Catalyst 9200"
                    );

                    equipment.setIpAddress("192.168.1.A");
                }
        );
    }

    @Test
    public void shouldKeepPreviousIpAddressWhenNewAddressIsInvalid() {
        Equipment equipment = new Equipment(
                "Routeur principal",
                "Cisco",
                "Catalyst 9200"
        );
        equipment.setIpAddress("192.168.1.10");
        assertThrows(
                IllegalArgumentException.class,
                () -> {

                    equipment.setIpAddress("192.168.1.256");
                });
        assertEquals("192.168.1.10", equipment.getIpAddress());
    }

}

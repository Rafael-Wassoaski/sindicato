package com.rafaelwassoaski.sindicato.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AddresTest {

    @Test
    public void shouldCreateAddress(){
        Address address = new Address("Street A", "Number B", "City C", "State D", "Country E");

        Assertions.assertEquals("Street A", address.getStreet());
        Assertions.assertEquals("Number B", address.getHouseNumber());
        Assertions.assertEquals("City C", address.getCity());
        Assertions.assertEquals("State D", address.getState());
        Assertions.assertEquals("Country E", address.getCountry());
    }
}

package com.example.luigi.restclients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ECBKoersClientTest {
    private ECBKoersClient client;
    @BeforeEach
    void beforeEach(){
        client = new ECBKoersClient();
    }
    @Test void deKoersIsPositief(){
        assertThat(client.getDollarKoers()).isPositive();
    }
}
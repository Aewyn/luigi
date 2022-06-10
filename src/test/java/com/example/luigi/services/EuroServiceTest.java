package com.example.luigi.services;

import com.example.luigi.restclients.FixerKoersClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EuroServiceTest {
    private EuroService euroService;

    @Mock
    private FixerKoersClient koersClient;

    @BeforeEach
    void beforeEach(){
        System.out.println(koersClient.getClass());
        System.out.println(koersClient.getDollarKoers());
        euroService = new EuroService(koersClient);
    }

    @Test
    void naarDollar(){

    }
}
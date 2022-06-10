package com.example.luigi.services;

import com.example.luigi.restclients.FixerKoersClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EuroService {
    private final FixerKoersClient koersClient;

    public EuroService(FixerKoersClient koersClient){
        this.koersClient = koersClient;
    }

    public BigDecimal naarDollar(BigDecimal euro){
        return euro.multiply(koersClient.getDollarKoers()).setScale(2, RoundingMode.HALF_UP);
    }
}

package com.example.luigi.services;

import com.example.luigi.exceptions.KoersClientException;
import com.example.luigi.restclients.KoersClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class EuroService {
    private final KoersClient[] koersClients;

    public EuroService(KoersClient[] koersClients) {
        this.koersClients = koersClients;
    }

    public BigDecimal naarDollar(BigDecimal euro) {
        Exception laatse = null;
        for (var client : koersClients) {
            try {
                return euro.multiply(client.getDollarKoers()).setScale(2, RoundingMode.HALF_UP);

            } catch (KoersClientException e) {
                laatse = e;
            }
        }
        throw new KoersClientException("Kan dollar koers nergens lezen.", laatse);
    }
}

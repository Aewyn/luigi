package com.example.luigi.domain;

public class Adres {
    private final String straat;
    private final String huisnr;
    private final int postcode;
    private final String gemeente;

    public Adres(String straat, String huisnr, int postcode, String gemeente) {
        this.straat = straat;
        this.huisnr = huisnr;
        this.postcode = postcode;
        this.gemeente = gemeente;
    }

    public String getStraat() {
        return straat;
    }

    public String getHuisnr() {
        return huisnr;
    }

    public int getPostcode() {
        return postcode;
    }

    public String getGemeente() {
        return gemeente;
    }
}

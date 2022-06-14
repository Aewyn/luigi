package com.example.luigi.restclients;

import com.example.luigi.exceptions.KoersClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

@Component
@Qualifier("Fixer")
class FixerKoersClient implements KoersClient {
    private static final Pattern PATTERN = Pattern.compile("^[\\S|\\s]*?\"USD\": *(\\d+.\\d*)[\\S|\\s]*$");
    private final URL url;
    public FixerKoersClient(){
        try{
            url = new URL("https://api.apilayer.com/fixer/latest?symbols=USD&base=EUR&apikey=wxYeM4Xfz5mLHNCOHaJlgCwOmWctNWe6");
        } catch (MalformedURLException e) {
            throw new KoersClientException("Fixer URL is verkeerd");
        }
    }

    @Override
    public BigDecimal getDollarKoers(){
        try(var stream = url.openStream()){
            var matcher = PATTERN.matcher(new String(stream.readAllBytes()));
            if(!matcher.matches()){
                throw new KoersClientException("Fixer data ongeldig");
            }
            return new BigDecimal(matcher.group(1));
        } catch (IOException ex){
            throw new KoersClientException("Kan koers niet lezen via Fixer.", ex);
        }
    }

}

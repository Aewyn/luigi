package com.example.luigi.restclients;

import com.example.luigi.exceptions.KoersClientException;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class ECBKoersClient {
    private final URL url;
    private final XMLInputFactory factory = XMLInputFactory.newInstance();

    ECBKoersClient() {
        try {
            url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        } catch (MalformedURLException e) {
            throw new KoersClientException("ECB URL is verkeerd.", e);
        }
    }

    public BigDecimal getDollarKoers() {
        try (var stream = url.openStream()) {
            for (var reader = factory.createXMLStreamReader(stream);
                 reader.hasNext(); ) {
                reader.next();
                if (reader.isStartElement()) {
                    if ("USD".equals(reader.getAttributeValue(0))) {
                        return new BigDecimal(reader.getAttributeValue(1));
                    }
                }
            }
            throw new KoersClientException("XML van ECB bevat geen USD.");
        } catch (IOException | NumberFormatException | XMLStreamException e) {
            throw new KoersClientException("Kan koers niet lezen via ECB", e);
        }
    }
}

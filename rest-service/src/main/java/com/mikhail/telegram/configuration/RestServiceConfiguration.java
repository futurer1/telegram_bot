package com.mikhail.telegram.configuration;

import com.mikhail.telegram.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestServiceConfiguration {

    @Value("${salt.photo}")
    private String saltPhoto;

    @Value("${salt.doc}")
    private String saltDoc;

    @Qualifier("CryptoToolPhoto")
    @Bean
    public CryptoTool getCryptoToolPhoto() {
        return new CryptoTool(saltPhoto);
    }

    @Qualifier("CryptoToolDoc")
    @Bean
    public CryptoTool getCryptoToolDoc() {
        return new CryptoTool(saltDoc);
    }
}

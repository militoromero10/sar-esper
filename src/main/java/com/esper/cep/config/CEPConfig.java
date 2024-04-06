package com.esper.cep.config;

import com.esper.cep.records.events.MarketingDesignEvent;
import com.joshlong.esper.autoconfiguration.EsperConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Map;

@Configuration
public class CEPConfig {
    @Bean
    EsperConfigurationCustomizer esperConfigurationCustomizer() {
        return configuration -> {
            Map<String, Object> mdMap = Map.of("user", String.class, "name", String.class, "budget", Double.class, "date", LocalDate.class, "approval", Boolean.class);
            configuration.getCommon().addEventType(MarketingDesignEvent.class.getSimpleName(), mdMap);
        };
    }
}

package com.esper.cep.config;

import com.esper.cep.records.events.CustomerCreatedEvent;
import com.esper.cep.records.events.MarketingDesignEvent;
import com.esper.cep.records.events.WithdrawalEvent;
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
            Map<String, Object> user = Map.of("user", String.class, "location", String.class, "amount", Float.class);
            configuration.getCommon().addEventType(WithdrawalEvent.class.getSimpleName(), user);

            Map<String, Object> name = Map.of("name", String.class, "age", Integer.class);
            configuration.getCommon().addEventType(CustomerCreatedEvent.class.getSimpleName(), name);

            Map<String, Object> mdMap = Map.of("name", String.class, "budget", Double.class, "date", LocalDate.class, "approval", Boolean.class);
            configuration.getCommon().addEventType(MarketingDesignEvent.class.getSimpleName(), mdMap);
        };
    }
}

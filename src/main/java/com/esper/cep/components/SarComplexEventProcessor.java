package com.esper.cep.components;

import com.esper.cep.records.MarketingDesign;
import com.esper.cep.records.events.MarketingDesignEvent;
import com.espertech.esper.runtime.client.EPEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SarComplexEventProcessor {

    private final EPEventService eventService;

    public void createMarketingDesignEvent(@NonNull MarketingDesign marketingDesign) {
        Map<String, Object> name = Map.of("user", marketingDesign.user(), "name", marketingDesign.name(), "budget", marketingDesign.budget(), "date", marketingDesign.date(), "approval", marketingDesign.approval());
        this.eventService.sendEventMap(name, MarketingDesignEvent.class.getSimpleName());
    }
}
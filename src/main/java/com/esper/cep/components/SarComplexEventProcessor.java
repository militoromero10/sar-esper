package com.esper.cep.components;

import com.esper.cep.records.MarketingDesign;
import com.esper.cep.records.events.MarketingDesignEvent;
import com.espertech.esper.runtime.client.EPEventService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SarComplexEventProcessor {

    private final EPEventService eventService;

    public SarComplexEventProcessor(EPEventService eventService) {
        this.eventService = eventService;
    }

    public void createMarketingDesignEvent(MarketingDesign marketingDesign) {
        Map<String, Object> name = Map.of("user", marketingDesign.user(), "name", marketingDesign.name(), "budget", marketingDesign.budget(), "date", marketingDesign.date(), "approval", marketingDesign.approval());
        this.eventService.sendEventMap(name, MarketingDesignEvent.class.getSimpleName());
    }
}
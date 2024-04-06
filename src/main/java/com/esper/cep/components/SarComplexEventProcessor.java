package com.esper.cep.components;

import com.esper.cep.records.Customer;
import com.esper.cep.records.events.CustomerCreatedEvent;
import com.esper.cep.records.events.WithdrawRequest;
import com.esper.cep.records.events.WithdrawalEvent;
import com.espertech.esper.runtime.client.EPEventService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SarComplexEventProcessor {

    private final EPEventService eventService;

    public SarComplexEventProcessor(EPEventService eventService) {
        this.eventService = eventService;
    }

    public void createEvent(Customer customer) {
        Map<String, Object> name = Map.of("name", customer.name(), "age", customer.age());
        Class<?> customerCreatedEventClass = CustomerCreatedEvent.class;
        this.eventService.sendEventMap(name, customerCreatedEventClass.getSimpleName());
    }

    public void withdraw(WithdrawRequest withdrawRequest) {
        this.eventService.sendEventMap(Map.of("user", withdrawRequest.user().name(), "location", withdrawRequest.location(), "amount", withdrawRequest.amount()),
                WithdrawalEvent.class.getSimpleName());
    }
}
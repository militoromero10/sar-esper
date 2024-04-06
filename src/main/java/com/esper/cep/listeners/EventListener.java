package com.esper.cep.listeners;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener implements UpdateListener {

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        for (var newEvent : newEvents) {
            System.out.println("\tnewEvent " + newEvent.getUnderlying());
        }
    }
}

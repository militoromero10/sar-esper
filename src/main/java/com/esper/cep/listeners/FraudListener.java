package com.esper.cep.listeners;

import com.esper.cep.components.EmailService;
import com.esper.cep.records.Mail;
import com.esper.cep.records.events.MarketingDesignEvent;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FraudListener implements UpdateListener {

    private final EmailService emailService;

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        var e1 = (Map<String, Object>) newEvents[0].get("i");
        var e2 = (Map<String, Object>) newEvents[0].get("j");

        String user = e1.get("user").toString();
        String name = e1.get("name").toString();
        Double budget = Double.parseDouble(e1.get("budget").toString());
        LocalDate ld = LocalDate.parse(e1.get("date").toString());

        String user2 = e2.get("user").toString();
        String name2 = e2.get("name").toString();
        Double budget2 = Double.parseDouble(e2.get("budget").toString());
        LocalDate ld2 = LocalDate.parse(e2.get("date").toString());

        String fmt = """
            El proyecto '%s' tienen las mismas caracteristicas y ha sido llevado a cabo por diferentes usuarios
            
            Usuario 1: %s
            Proyecto: %s
            Presupuesto: %s
            Fecha: %s
            
            Usuario 2: %s
            Proyecto: %s
            Presupuesto: %s
            Fecha: %s
            
            Rectifique antes de aprobar las siguientes etapas.
            """;
        var msg = String.format(fmt, name, user, name, budget, ld, user2, name2, budget2, ld2);

        var to ="militoromero10@gmail.com";
        var subject = "Motor de Eventos Complejos a detectado un posible fraude.";

        var mail = new Mail(to,subject,msg);

        emailService.sendSimpleMessage(mail);
    }
}

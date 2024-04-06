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
        var aMap = (Map<String, Object>) newEvents[0].get("i");

        String userA = aMap.get("user").toString();
        String locationA = aMap.get("name").toString();
        Double budgetA = Double.parseDouble(aMap.get("budget").toString());
        LocalDate ldA = LocalDate.parse(aMap.get("date").toString());

        String fmt = """
                Los ultimos 3 proyectos tienen las mismas caracteristicas y han sido llevadas a cabo por le mismo usuario
                
                Usuario: %s
                Proyecto: %s
                Presupuesto: %s
                Fecha: %s
                
                Rectifique antes de aprobar las siguientes etapas.
                """;
        var msg = String.format(fmt, userA, locationA, budgetA, ldA);
        var to ="militoromero10@gmail.com";
        var subject = "Motor de Eventos Complejos a detectado un posible fraude.";

        var mail = new Mail(to,subject,msg);

        emailService.sendSimpleMessage(mail);
    }
}

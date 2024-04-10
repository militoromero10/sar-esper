package com.esper.cep.listeners;

import com.esper.cep.components.EmailService;
import com.esper.cep.records.Mail;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoEnoughInfoListener implements UpdateListener {

    private final EmailService emailService;

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        var name = newEvents[0].get("name").toString();
        var budget = newEvents[0].get("budget").toString();
        var date = newEvents[0].get("date").toString();

        String to = "lospolloshermanosprocesosdeneg@gmail.com";
        String subject = "Se ha detectado falencia en requisitos";
        String text = """
                Proyecto [%s] con las siguientes caracteriticas ha sido rechazado continuamente
                - Presupuesto: %s COP
                - Fecha: %s
                - Revisar caso antes de reprocesar.
                """;
        String msg = String.format(text, name, budget, date);
        var mail = new Mail(to,subject,msg);

        emailService.sendSimpleMessage(mail);
    }
}

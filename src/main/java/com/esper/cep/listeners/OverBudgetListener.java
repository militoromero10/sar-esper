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
public class OverBudgetListener implements UpdateListener {

    private final EmailService emailService;

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        var name = newEvents[0].get("name").toString();
        var amount = Double.parseDouble(newEvents[0].get("amount").toString());

        String to = "lospolloshermanosprocesosdeneg@gmail.com";
        String subject = "Se ha detectado sobrecostos";
        String text = """
                Proyecto '%s' esta en sobrecostos por un valor de %s COP.
                Presupuesto maximo: 10'000.000 COP
                Valor actual: %s COP
                """;
        String msg = String.format(text, name, amount-10000000d, amount);

        var mail = new Mail(to,subject,msg);

        emailService.sendSimpleMessage(mail);
    }
}

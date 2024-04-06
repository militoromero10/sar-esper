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
        var counter = Long.parseLong(newEvents[0].get("counter").toString());
        if(counter == 3) {
            String to = "militoromero10@gmail.com";
            String subject = "Se ha detectado posibles sobrecostos";
            String text = """
                    Proyecto esta incurriendo en sobrecostos.
                    Revisar caso cuanto antes.
                    """;
            var mail = new Mail(to,subject,text);

            emailService.sendSimpleMessage(mail);
        }
    }
}

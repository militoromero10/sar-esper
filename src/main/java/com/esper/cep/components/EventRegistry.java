package com.esper.cep.components;

import com.esper.cep.listeners.EventListener;
import com.esper.cep.listeners.FraudListener;
import com.esper.cep.listeners.OverBudgetListener;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.runtime.client.EPDeploymentService;
import com.espertech.esper.runtime.client.UpdateListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventRegistry {

    private final Configuration esperConfiguration;
    private final EPCompiler epCompiler;
    private final EPDeploymentService epDeploymentService;
    private final FraudListener fraudListener;
    private final OverBudgetListener overBudgetListener;
    private final EventListener eventListener;

    @PostConstruct
    public void registry() throws Exception {
        register(EventQuery.ALL.getQuery(), EventQuery.ALL.getStateName(), eventListener);
        register(EventQuery.REJECTED_DESIGN.getQuery(), EventQuery.REJECTED_DESIGN.getStateName(), overBudgetListener);
        register(EventQuery.FRAUD.getQuery(), EventQuery.FRAUD.getStateName(), fraudListener);
    }

    private void register(String query, String statementName, UpdateListener listener) throws Exception {
        var compiled = this.epCompiler.compile(query, new CompilerArguments(this.esperConfiguration));
        var deployment = this.epDeploymentService.deploy(compiled);

        epDeploymentService.getStatement(deployment.getDeploymentId(), statementName)
                .addListener((newEvents, oldEvents, statement, runtime) -> {
                    System.out.println("event [" + statementName + "] registered.");
                    listener.update(newEvents, oldEvents, statement, runtime);
                });
    }
}

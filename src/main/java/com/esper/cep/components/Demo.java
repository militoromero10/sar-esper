package com.esper.cep.components;

import com.esper.cep.records.Customer;
import com.esper.cep.records.FraudEvent;
import com.esper.cep.records.events.WithdrawRequest;
import com.esper.cep.records.events.WithdrawalEvent;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.runtime.client.EPDeploymentService;
import com.espertech.esper.runtime.client.UpdateListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.espertech.esper.common.client.configuration.Configuration;

import java.util.List;
import java.util.Map;

@Component
public class Demo implements ApplicationRunner {

    private final com.espertech.esper.common.client.configuration.Configuration esperConfiguration;
    private final EPCompiler epCompiler;
    private final EPDeploymentService epDeploymentService;
    private final SarComplexEventProcessor client;
    private final UpdateListener updateListener = (newEvents, oldEvents, statement, runtime) -> {
        for (var ne : newEvents)
            System.out.println("\tnewEvent " + ne.getUnderlying());
    };

    public Demo(Configuration esperConfiguration, EPCompiler epCompiler, EPDeploymentService epDeploymentService, SarComplexEventProcessor client) {
        this.esperConfiguration = esperConfiguration;
        this.epCompiler = epCompiler;
        this.epDeploymentService = epDeploymentService;
        this.client = client;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String allQuery = "@name('all') select * from CustomerCreatedEvent", all ="all";
//        register(allQuery, all, this.updateListener);


        String allQualifiedQuery = "@name('all-qualified') select * from CustomerCreatedEvent(age >=40) ", allQualified = "all-qualified";
//        register(allQualifiedQuery, allQualified, this.updateListener);

        String withDrawalsFromMultipleLocationsQuery = """
                create context PartitionedByUser partition by user from WithdrawalEvent ;
                        
                @name('withdrawals-from-multiple-locations')
                context PartitionedByUser select * from pattern [
                    every  (
                        c1 = WithdrawalEvent ->
                        c2 = WithdrawalEvent ( user = c1.user, location != c1.location) where timer:within(5 minutes)
                    )
                ]
                    """,  withdrawalsFromMultipleLocations = "withdrawals-from-multiple-locations";
        register(withDrawalsFromMultipleLocationsQuery, withdrawalsFromMultipleLocations, (newEvents, oldEvents, statement, runtime) -> {
            var aMap = (Map<String, Object>) newEvents[0].get("c1");
            var bMap = (Map<String, Object>) newEvents[0].get("c2");
            var aWE = new WithdrawalEvent((String) aMap.get("user"), (String) aMap.get("location"), (float) aMap.get("amount"));
            var bWE = new WithdrawalEvent((String) bMap.get("user"), (String) bMap.get("location"), (float) bMap.get("amount"));
            var fe = new FraudEvent(aWE, bWE);
            System.out.println("fraud! " + fe);
        });

        var camilo = new Customer("Camilo", 30);
        var juan = new Customer("Juan", 38);
        var andres = new Customer("Andres", 30);
        var ivonne = new Customer("Ivonne", 30);
        var jose = new Customer("Jose", 38);
        var augusto = new Customer("Augusto", 58);
        var paula = new Customer("Paula", 38);
        var sandra = new Customer("Sandra", 52);

        List.of(camilo, juan, andres, ivonne, jose, augusto, paula, sandra)
                .forEach(client::createEvent);

        var req1 = new WithdrawRequest(camilo, "Leticia", 100);
        var req2 = new WithdrawRequest(camilo, "Riohacha", 10);

        client.withdraw(req1);
        client.withdraw(req2);
    }

    private void register(String query, String statementName, UpdateListener listener) throws Exception {
        var compiled = this.epCompiler.compile(query, new CompilerArguments(this.esperConfiguration));
        var deployment = this.epDeploymentService.deploy(compiled);
        epDeploymentService.getStatement(deployment.getDeploymentId(), statementName)
                .addListener((newEvents, oldEvents, statement, runtime) -> {
                    System.out.println("statement [" + statementName + "] results ");
                    listener.update(newEvents, oldEvents, statement, runtime);
                });
    }
}
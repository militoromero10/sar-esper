package com.esper.cep.components;

import com.esper.cep.records.events.MarketingDesignEvent;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompiler;
import com.espertech.esper.runtime.client.EPDeploymentService;
import com.espertech.esper.runtime.client.UpdateListener;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class EventRegistry {

    private final com.espertech.esper.common.client.configuration.Configuration esperConfiguration;
    private final EPCompiler epCompiler;
    private final EPDeploymentService epDeploymentService;

    public EventRegistry(Configuration esperConfiguration, EPCompiler epCompiler, EPDeploymentService epDeploymentService/*, SarComplexEventProcessor client*/) {
        this.esperConfiguration = esperConfiguration;
        this.epCompiler = epCompiler;
        this.epDeploymentService = epDeploymentService;
    }

    @PostConstruct
    public void registry() throws Exception {
        String allQuery = "@name('all') select * from MarketingDesignEvent", all = "all";
        String allQualifiedQuery = "@name('all-qualified') select * from MarketingDesignEvent(budget>1000) ", allQualified = "all-qualified";

        UpdateListener updateListener = (newEvents, oldEvents, statement, runtime) -> {
            for (var ne : newEvents)
                System.out.println("\tnewEvent " + ne.getUnderlying());
        };


        String multipleLocationsQuery = """
                create context PartitionedByUser partition by user from MarketingDesignEvent ;
                        
                @name('multiple-locations')
                context PartitionedByUser select * from pattern [
                    every  (
                        i = MarketingDesignEvent ->
                        j = MarketingDesignEvent ( user = i.user, name != i.name) where timer:within(5 minutes)
                    )
                ]
                    """, multipleLocations = "multiple-locations";

        UpdateListener updateListener1 = (newEvents, oldEvents, statement, runtime) -> {
            var aMap = (Map<String, Object>) newEvents[0].get("i");
            var bMap = (Map<String, Object>) newEvents[0].get("j");

            String userA = aMap.get("user").toString();
            String locationA = aMap.get("name").toString();
            Double budgetA = Double.parseDouble(aMap.get("budget").toString());
            LocalDate ldA = LocalDate.parse(aMap.get("date").toString());
            Boolean approvalA = Boolean.getBoolean(aMap.get("approval").toString());
            var md1 = new MarketingDesignEvent(userA, locationA, budgetA, ldA, approvalA);

            String userB = bMap.get("user").toString();
            String locationB = bMap.get("name").toString();
            Double budgetB = Double.parseDouble(bMap.get("budget").toString());
            LocalDate ldB = LocalDate.parse(bMap.get("date").toString());
            Boolean approvalB = Boolean.getBoolean(bMap.get("approval").toString());
            var md2 = new MarketingDesignEvent(userB, locationB, budgetB, ldB, approvalB);

            System.out.printf("MD1= [%s]\nMD2= [%s]\n", md1, md2);
        };


//        register(allQuery, all, updateListener);
//        register(allQualifiedQuery, allQualified, updateListener);
        register(multipleLocationsQuery, multipleLocations, updateListener1);
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

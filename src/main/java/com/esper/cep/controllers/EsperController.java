package com.esper.cep.controllers;

import com.esper.cep.components.SarComplexEventProcessor;
import com.esper.cep.records.MarketingDesign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/sar/cep")
public class EsperController {


    private final SarComplexEventProcessor sarCEP;

    public EsperController(SarComplexEventProcessor sarCEP) {
        this.sarCEP = sarCEP;
    }


    @GetMapping("/{user}/{name}/{budget}")
    public String marketingDesignEvent(@PathVariable String user, @PathVariable String name, @PathVariable Double budget) {
        var md1 = new MarketingDesign(user, name, budget, LocalDate.now(), Boolean.TRUE);
        sarCEP.createMarketingDesignEvent(md1);
        return "buena esa!";
    }

}


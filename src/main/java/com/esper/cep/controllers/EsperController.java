package com.esper.cep.controllers;

import com.esper.cep.components.SarComplexEventProcessor;
import com.esper.cep.records.BonitaBody;
import com.esper.cep.records.MarketingDesign;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/sar/cep")
@RequiredArgsConstructor
public class EsperController {

    private final SarComplexEventProcessor sarCEP;

    @PostMapping
    public void marketingDesignEvent(@RequestBody BonitaBody body) {
        var md1 = new MarketingDesign(body.user(), body.project(), body.budget(), LocalDate.now(), body.approval());
        sarCEP.createMarketingDesignEvent(md1);
    }
}


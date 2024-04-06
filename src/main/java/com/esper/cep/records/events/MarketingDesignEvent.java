package com.esper.cep.records.events;

import java.time.LocalDate;

public record MarketingDesignEvent(String user, String name, Double budget, LocalDate date, Boolean approval) {
}

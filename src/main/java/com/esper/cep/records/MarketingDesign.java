package com.esper.cep.records;

import java.time.LocalDate;

public record MarketingDesign(String user, String name, Double budget, LocalDate date, Boolean approval) {
}

package com.esper.cep.records.events;

public record WithdrawalEvent(String user, String location, float amount) {
}

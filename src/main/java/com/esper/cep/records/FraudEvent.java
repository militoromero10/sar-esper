package com.esper.cep.records;

import com.esper.cep.records.events.WithdrawalEvent;

public record FraudEvent(WithdrawalEvent a, WithdrawalEvent b) {
}

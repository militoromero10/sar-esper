package com.esper.cep.records.events;

import com.esper.cep.records.Customer;

public record WithdrawRequest(Customer user, String location, float amount) {
}

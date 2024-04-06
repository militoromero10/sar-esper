package com.esper.cep.components;

public enum EventQuery {
    ALL("@name('all') select * from MarketingDesignEvent", "all"),
    FRAUD("""
            create context PartitionedByUser partition by user from MarketingDesignEvent;
                        
                @name('fraud-projects')
                context PartitionedByUser select * from pattern [
                    every  (
                        i = MarketingDesignEvent ->
                        j = MarketingDesignEvent ( user = i.user, name != i.name) where timer:within(5 minutes)
                    )
                ]
            """, "fraud-projects"),
    REJECTED_DESIGN("@name('rejected-design') select count(approval) as counter from MarketingDesignEvent.win:length(3) where approval = false", "rejected-design");

    private final String query;
    private final String stateName;

    EventQuery(String query, String stateName) {
        this.query = query;
        this.stateName = stateName;
    }

    public String getQuery() {
        return query;
    }

    public String getStateName() {
        return stateName;
    }
}

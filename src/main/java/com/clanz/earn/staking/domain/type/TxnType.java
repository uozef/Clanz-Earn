package com.clanz.earn.staking.domain.type;

public enum TxnType {
    SUBSCRIPTION(1),
    REDEMPTION(2),
    INTEREST(3);

    private final int TxnType;

    private TxnType(int id) {
        this.TxnType = id;
    }

    public int getId() {
        return this.TxnType;
    }

}

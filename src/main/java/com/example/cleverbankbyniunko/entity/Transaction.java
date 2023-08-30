package com.example.cleverbankbyniunko.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class Transaction extends AbstractEntity {
    @Getter
    private LocalDateTime transactionTime;
    @Getter
    @Setter
    private String fromNumber;
    @Getter
    @Setter
    private String toNumber;
    @Getter
    @Setter
    private Double transactionAmount;
    @Getter
    private TypeTransaction typeTransaction;

    public Transaction() {
    }

    private enum TypeTransaction {
        REFILL,
        WITHDRAWALS,
        TRANSLATION,
        DEFAULT_TRANSACTION
    }

    public void setTypeTransaction(int idTransaction) {
        Optional<Transaction.TypeTransaction> optionalCurrency= Arrays.stream(Transaction.TypeTransaction.values())
                .filter(s->(s.ordinal()+1)==idTransaction)
                .findFirst();
        this.typeTransaction = optionalCurrency.orElse(Transaction.TypeTransaction.DEFAULT_TRANSACTION);
    }
    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }
    public void setTransactionTime(String transactionTime) {
        String date,time,year,month,day,hour,minute,second;
        date=transactionTime.split("\\s")[0];
        time=transactionTime.split("\\s")[1];
        year=date.split("-")[0];
        month=date.split("-")[1];
        day=date.split("-")[2];
        hour=time.split(":")[0];
        minute=time.split(":")[1];
        second=time.split(":")[2];
        this.transactionTime = LocalDateTime.of(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute),Integer.parseInt(second));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionTime, that.transactionTime) && Objects.equals(fromNumber, that.fromNumber) && Objects.equals(toNumber, that.toNumber) && Objects.equals(transactionAmount, that.transactionAmount) && typeTransaction == that.typeTransaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), transactionTime, fromNumber, toNumber, transactionAmount, typeTransaction);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Transaction.class.getSimpleName() + "[", "]")
                .add("transactionTime=" + transactionTime)
                .add("fromNumber='" + fromNumber + "'")
                .add("toNumber='" + toNumber + "'")
                .add("transactionAmount=" + transactionAmount)
                .add("typeTransaction=" + typeTransaction)
                .toString();
    }
}

package com.example.cleverbankbyniunko.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;


public class Account extends AbstractEntity{
    @Getter
    @Setter
    private String accountNumber;
    @Getter
    @Setter
    private long idOwner;
    @Getter
    private Bank bank;
    @Getter
    private BigDecimal amount;
    @Getter
    private Currency currency;
    @Getter
    private LocalDate openingDate;

    public Account() {
    }

    public void setBank(long idBank) {
        Optional <Bank> optionalBank= Arrays.stream(Bank.values())
                .filter(s->(s.ordinal()+1)==idBank)
                .findFirst();
        this.bank = optionalBank.orElse(Bank.DEFAULT_BANK);
    }

    public void setAmount(Double value) {
        this.amount = new BigDecimal(value).setScale(2, RoundingMode.DOWN);
    }

    public void setCurrency(long idCurrency) {
        Optional <Currency> optionalCurrency= Arrays.stream(Currency.values())
                .filter(s->(s.ordinal()+1)==idCurrency)
                .findFirst();
        this.currency = optionalCurrency.orElse(Currency.DEFAULT_CURRENCY);
    }

    public void setOpeningDate(String openingDate) {
        int year=Integer.parseInt(openingDate.split("-")[0]);
        int month=Integer.parseInt(openingDate.split("-")[1]);
        int day=Integer.parseInt(openingDate.split("-")[2]);
        this.openingDate = LocalDate.of(year,month,day);
    }

    public enum Bank{
        BELINVESTBANK,
        BELARUSBANK,
        ALFABANK,
        RAFFAISENBANK,
        ZEPTERBANK,
        DEFAULT_BANK;
    }
    public enum Currency{
        BLR,
        USD,
        RUB,
        EUR,
        DEFAULT_CURRENCY;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("accountNumber='" + accountNumber + "'")
                .add("idOwner=" + idOwner)
                .add("bank=" + bank)
                .add("amount=" + amount)
                .add("currency=" + currency)
                .add("openingDate=" + openingDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return idOwner == account.idOwner && Objects.equals(accountNumber, account.accountNumber) && bank == account.bank && Objects.equals(amount, account.amount) && currency == account.currency && Objects.equals(openingDate, account.openingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountNumber, idOwner, bank, amount, currency, openingDate);
    }
}

package service;

import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.Premium;

import util.WithdrawRule;

import static util.StatementFormatter.buildStatement;

public class MiniBank {


public static void main(String[] args) {

    Account[] accounts = {
        new SavingsAccount("Rahul", 10000, 2000),
        new CurrentAccount("Priya", 5000, 3000),
        new FixedDepositAccount("Amit", 20000)
    };

    // Runtime polymorphism and default method
    System.out.println("=== Account Details ===");

    for (Account account : accounts) {
        System.out.println(account);
        System.out.println(
                "Interest Rate: " + account.interestRate() + "%");
        System.out.println(
                "Yearly Interest: Rs. " + account.yearlyInterest());
        System.out.println();
    }

    // 1. WithdrawRule using an anonymous class
    WithdrawRule rule1 = new WithdrawRule() {
        @Override
        public boolean allow(Account account, long amount) {
            return amount > 0
                    && account.getBalance() >= amount;
        }
    };

    System.out.println("=== Anonymous Class ===");
    System.out.println(
            "Can withdraw Rs. 1000: "
            + rule1.allow(accounts[0], 1000));

    // 2. WithdrawRule using a lambda expression
    WithdrawRule rule2 = (account, amount) ->
            amount > 0 && account.getBalance() >= amount;

    System.out.println("\n=== Lambda Expression ===");
    System.out.println(
            "Can withdraw Rs. 12000: "
            + rule2.allow(accounts[0], 12000));

    // Marker interface check
    System.out.println("\n=== Premium Account Check ===");  

    for (Account account : accounts) {
        if (account instanceof Premium) {
            System.out.println(
                    account.getOwnerName() + " has premium status.");
        }
    }

    // Static import demonstration
    System.out.println("\n=== Account Statement ===");
    System.out.println(buildStatement(accounts[0]));
}


}

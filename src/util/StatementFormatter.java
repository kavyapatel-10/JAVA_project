package util;

import model.Account;

public class StatementFormatter {

    public static String buildStatement(Account account) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null.");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("========== ACCOUNT STATEMENT ==========\n");
        sb.append("Account Number: ")
                .append(account.getAccountNumber()).append("\n");
        sb.append("Account Holder: ")
                .append(account.getOwnerName()).append("\n");
        sb.append("Balance: Rs. ")
                .append(account.getBalance()).append("\n");
        sb.append("Interest Rate: ")
                .append(account.interestRate()).append("%\n");
        sb.append("Yearly Interest: Rs. ")
                .append(account.yearlyInterest()).append("\n");
        sb.append("Account Active: ")
                .append(account.isActive()).append("\n");
        sb.append("======================================");

        return sb.toString();
    }
}
package util;
import model.Account;
public final class StatementFormatter {
    private StatementFormatter() {}
    public static String buildStatement(Account account) {
        StringBuilder out = new StringBuilder();
        out.append("----- MiniBank Account Statement -----\n");
        out.append("Account Number: ").append(account.getAccountNumber()).append('\n');
        out.append("Owner: ").append(account.getOwnerName()).append('\n');
        out.append("Type: ").append(account.getClass().getSimpleName()).append('\n');
        out.append("Balance: Rs. ").append(account.getBalance()).append('\n');
        out.append("Interest Rate: ").append(account.interestRate()).append("%\n");
        out.append("Estimated Yearly Interest: Rs. ").append(String.format("%.2f", account.yearlyInterest())).append('\n');
        out.append("Active: ").append(account.isActive()).append('\n');
        return out.toString();
    }
}

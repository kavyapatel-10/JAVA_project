import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
public class BankServiceTest {
    @Test void depositIncreasesBalance() throws InvalidAmountException {
        Account account = new SavingsAccount("Test", 1000, 0);
        account.deposit(250);
        assertEquals(1250, account.getBalance());
    }
    @Test void withdrawalOverLimitThrows() {
        Account account = new SavingsAccount("Test", 1000, 100);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(950));
    }
    @Test void concurrentDepositsKeepCorrectBalance() throws InterruptedException {
        Account account = new SavingsAccount("Thread Test", 0, 0);
        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<10; i++) { Thread thread = new Thread(() -> { for (int j=0; j<1000; j++) { try { account.deposit(1); } catch (InvalidAmountException e) { throw new AssertionError(e); } } }); threads.add(thread); thread.start(); }
        for (Thread thread : threads) thread.join();
        assertEquals(10000, account.getBalance());
    }
}

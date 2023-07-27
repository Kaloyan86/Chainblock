import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChainblockImplTest {

    private Chainblock database;
    private List<Transaction> transactions;

    @Before
    public void setUp() {
        this.database = new ChainblockImpl();
        this.transactions = new ArrayList<>();

        createTransactions();
    }

    private void createTransactions() {
        Transaction transaction = new TransactionImpl(0, TransactionStatus.SUCCESSFUL, "Kaloyan", "Peter", 55.60);
        Transaction transaction1 = new TransactionImpl(1, TransactionStatus.ABORTED, "Ivan", "Pesho", 155.60);
        Transaction transaction2 = new TransactionImpl(2, TransactionStatus.SUCCESSFUL, "Peter", "Alex", 255.60);
        Transaction transaction3 = new TransactionImpl(3, TransactionStatus.SUCCESSFUL, "Kaloyan", "Kriss", 100.60);
        Transaction transaction4 = new TransactionImpl(4, TransactionStatus.FAILED, "Martin", "Ani", 55.60);

        this.transactions.addAll(Arrays.asList(transaction, transaction1, transaction2, transaction3, transaction4));
    }

    private void fillDatabase() {
        this.transactions.forEach(this.database::add);
    }

    @Test
    public void testGetCount() {
        Transaction transaction = this.transactions.get(0);
        this.database.add(transaction);
        Assert.assertEquals(1, this.database.getCount());

        Transaction transaction1 = this.transactions.get(1);
        this.database.add(transaction1);
        Assert.assertEquals(2, this.database.getCount());
    }

    @Test
    public void testGetCountEmptyDatabase() {

        Assert.assertEquals(0, this.database.getCount());
    }

    @Test
    public void testAddTransactionSuccess() {

        this.database.add(this.transactions.get(0));
        Assert.assertEquals(1, this.database.getCount());

        this.database.add(this.transactions.get(1));
        Assert.assertEquals(2, this.database.getCount());
    }

    @Test
    public void testAddDuplicateTransactionFail() {
        this.database.add(this.transactions.get(0));
        this.database.add(this.transactions.get(0));

        Assert.assertEquals(1, this.database.getCount());
    }

    // boolean contains(Transaction transaction);
    @Test
    public void testContainsTransactionExists() {
        Transaction transaction = this.transactions.get(0);
        this.database.add(transaction);

        Assert.assertTrue(this.database.contains(transaction));
    }

    // boolean contains(Transaction transaction);
    @Test
    public void testContainsTransactionNotExists() {
        Transaction transaction = this.transactions.get(0);
        this.database.add(transaction);
        Transaction transaction1 = this.transactions.get(1);

        Assert.assertFalse(this.database.contains(transaction1));
    }

    // boolean contains(Transaction transaction);
    @Test
    public void testContainsEmptyDatabase() {
        Transaction transaction = this.transactions.get(0);

        Assert.assertFalse(this.database.contains(transaction));
    }

    // boolean contains(int id);
    @Test
    public void testContainsByIdTransactionExists() {
        Transaction transaction = this.transactions.get(0);
        this.database.add(transaction);

        Assert.assertTrue(this.database.contains(transaction.getId()));
    }

    // boolean contains(int id);
    @Test
    public void testContainsByIdTransactionNotExists() {
        Transaction transaction = this.transactions.get(0);
        this.database.add(transaction);
        Transaction transaction1 = this.transactions.get(1);

        Assert.assertFalse(this.database.contains(transaction1.getId()));
    }

    // boolean contains(int id);
    @Test
    public void testContainsByIdEmptyDatabase() {
        Transaction transaction = this.transactions.get(0);

        Assert.assertFalse(this.database.contains(transaction.getId()));
    }

    @Test
    public void testChangeTransactionStatusTransactionExists() {
        fillDatabase();

        this.database.changeTransactionStatus(0, TransactionStatus.ABORTED);

        Assert.assertEquals(TransactionStatus.ABORTED, this.database.getById(0).getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeTransactionStatusTransactionNotExists() {
        fillDatabase();

        this.database.changeTransactionStatus(150, TransactionStatus.FAILED);
    }

    // remove Transaction if exists
    @Test
    public void testRemoveExistingTransaction() {
        fillDatabase();

        this.database.removeTransactionById(0);

        Assert.assertEquals(4, this.database.getCount());
        Assert.assertFalse(this.database.contains(0));
    }

    // throw IllegalArgumentException if Transaction does not exist
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingTransaction() {
        fillDatabase();

        this.database.removeTransactionById(150);
    }

    // return Transaction with given Id if such exists
    @Test
    public void testGetByIdExistingTransaction() {
        fillDatabase();

        Transaction transaction = this.database.getById(3);

        Assert.assertEquals(3, transaction.getId());
    }

    // throw IllegalArgumentException if Transaction does not exist
    @Test(expected = IllegalArgumentException.class)
    public void testGetByIdNonExistingTransaction() {
        fillDatabase();

        this.database.getById(150);
    }

    @Test
    public void testGetByTransactionStatusTransactionsExist() {
        fillDatabase();
        List<Transaction> result = new ArrayList<>();

        this.database.getByTransactionStatus(TransactionStatus.SUCCESSFUL)
                     .forEach(result::add);

        Transaction transaction1 = result.get(0);
        Transaction transaction2 = result.get(1);
        Transaction transaction3 = result.get(2);

        Assert.assertEquals(2, transaction1.getId());
        Assert.assertEquals(3, transaction2.getId());
        Assert.assertEquals(0, transaction3.getId());

        result.forEach(transaction -> Assert.assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetByTransactionStatusTransactionsNotExist() {
        fillDatabase();
        this.database.removeTransactionById(4); // remove Transaction with status FAILED

        this.database.getByTransactionStatus(TransactionStatus.FAILED);
    }

    @Test
    public void testGetAllSendersWithTransactionStatusTransactionsExist() {
        fillDatabase();
        List<String> senders = new ArrayList<>();
        this.database.getAllSendersWithTransactionStatus(TransactionStatus.SUCCESSFUL)
                     .forEach(senders::add);

        String sender1 = senders.get(0);
        String sender2 = senders.get(1);
        String sender3 = senders.get(2);

        Assert.assertEquals("Kaloyan", sender1);
        Assert.assertEquals("Kaloyan", sender2);
        Assert.assertEquals("Peter", sender3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllSendersWithTransactionStatusTransactionsNotExist() {
        fillDatabase();
        this.database.removeTransactionById(4); // remove Transaction with status FAILED

        this.database.getAllSendersWithTransactionStatus(TransactionStatus.FAILED);
    }
}

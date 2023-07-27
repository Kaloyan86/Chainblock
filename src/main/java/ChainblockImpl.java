import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChainblockImpl implements Chainblock {

    private Map<Integer, Transaction> transactionMap;

    public ChainblockImpl() {
        this.transactionMap = new LinkedHashMap<>();
    }

    public int getCount() {
        return this.transactionMap.size();
    }

    public void add(Transaction transaction) {
        this.transactionMap.putIfAbsent(transaction.getId(), transaction);
    }

    public boolean contains(Transaction transaction) {
        return this.transactionMap.containsKey(transaction.getId());
    }

    public boolean contains(int id) {
        return this.transactionMap.containsKey(id);
    }

    public void changeTransactionStatus(int id, TransactionStatus newStatus) {
        Transaction transaction = this.getById(id);
        transaction.setStatus(newStatus);
    }

    public void removeTransactionById(int id) {
        Transaction transaction = this.getById(id);
        this.transactionMap.remove(transaction.getId());
    }

    public Transaction getById(int id) {
        if (!this.contains(id)) {
            throw new IllegalArgumentException();
        }
        return this.transactionMap.get(id);
    }

    public Iterable<Transaction> getByTransactionStatus(TransactionStatus status) {
        List<Transaction> transactions = this.transactionMap.values()
                                                            .stream()
                                                            .filter(transaction -> transaction.getStatus().equals(status))
                                                            // .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                                                            .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                                                            .collect(Collectors.toList());
        if (transactions.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return transactions;
    }

    public Iterable<String> getAllSendersWithTransactionStatus(TransactionStatus status) {
        List<String> transactions = this.transactionMap.values()
                                                       .stream()
                                                       .filter(transaction -> transaction.getStatus().equals(status))
                                                       .map(Transaction::getFrom)
                                                       .collect(Collectors.toList());

        if (transactions.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Kaloyan, Peter, Kaloyan -> Kaloyan, Kaloyan, Peter
        List<String> sendersNames = transactions.stream()
                                                .distinct()
                                                .collect(Collectors.toList());

        Map<Integer, String> senders = new TreeMap<>(Comparator.reverseOrder());

        for (String sender : sendersNames) {
            int frequency = Collections.frequency(transactions, sender);
            senders.put(frequency, sender);
        }

        transactions = new LinkedList<>();

        for (Map.Entry<Integer, String> entry : senders.entrySet()) {
            for (int i = 0; i < entry.getKey(); i++) {
                transactions.add(entry.getValue());
            }
        }

        return transactions;
    }

    public Iterable<String> getAllReceiversWithTransactionStatus(TransactionStatus status) {
        return null;
    }

    public Iterable<Transaction> getAllOrderedByAmountDescendingThenById() {
        return null;
    }

    public Iterable<Transaction> getBySenderOrderedByAmountDescending(String sender) {
        return null;
    }

    public Iterable<Transaction> getByReceiverOrderedByAmountThenById(String receiver) {
        return null;
    }

    public Iterable<Transaction> getByTransactionStatusAndMaximumAmount(TransactionStatus status, double amount) {
        return null;
    }

    public Iterable<Transaction> getBySenderAndMinimumAmountDescending(String sender, double amount) {
        return null;
    }

    public Iterable<Transaction> getByReceiverAndAmountRange(String receiver, double lo, double hi) {
        return null;
    }

    public Iterable<Transaction> getAllInAmountRange(double lo, double hi) {
        return null;
    }

    public Iterator<Transaction> iterator() {
        return null;
    }
}

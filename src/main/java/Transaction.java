public interface Transaction {

    int getId();

    TransactionStatus getStatus();

    void setStatus(TransactionStatus status);

    String getFrom();

    String getTo();

    double getAmount();

}

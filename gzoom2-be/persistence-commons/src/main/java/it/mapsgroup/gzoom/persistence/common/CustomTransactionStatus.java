package it.mapsgroup.gzoom.persistence.common;

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author Andrea Fossi.
 */
public class CustomTransactionStatus implements TransactionStatus {
    private final TransactionStatus transactionStatus;
    private final LocalDateTime timestamp;
    private final Instant timestamp2;

    public CustomTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
        this.timestamp2 = Instant.now();
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public boolean isNewTransaction() {
        return transactionStatus.isNewTransaction();
    }

    @Override
    public boolean hasSavepoint() {
        return transactionStatus.hasSavepoint();
    }

    @Override
    public void setRollbackOnly() {
        transactionStatus.setRollbackOnly();
    }

    @Override
    public boolean isRollbackOnly() {
        return transactionStatus.isRollbackOnly();
    }

    @Override
    public void flush() {
        transactionStatus.flush();
    }

    @Override
    public boolean isCompleted() {
        return transactionStatus.isCompleted();
    }

    @Override
    public Object createSavepoint() throws TransactionException {
        return transactionStatus.createSavepoint();
    }

    @Override
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        transactionStatus.rollbackToSavepoint(savepoint);
    }

    @Override
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        transactionStatus.releaseSavepoint(savepoint);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Instant getTimestamp2() {
        return timestamp2;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }
}

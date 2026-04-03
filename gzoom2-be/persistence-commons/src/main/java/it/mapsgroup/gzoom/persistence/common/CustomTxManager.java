package it.mapsgroup.gzoom.persistence.common;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * @author Andrea Fossi.
 */
public class CustomTxManager implements PlatformTransactionManager {
    private PlatformTransactionManager txManager;

    public CustomTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        TransactionStatus transaction = txManager.getTransaction(definition);
        return new CustomTransactionStatus(transaction);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        if (status instanceof CustomTransactionStatus)
            txManager.commit(((CustomTransactionStatus) status).getTransactionStatus());
        else
            txManager.commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        if (status instanceof CustomTransactionStatus)
            txManager.rollback(((CustomTransactionStatus) status).getTransactionStatus());
        else
            txManager.rollback(status);
    }
}

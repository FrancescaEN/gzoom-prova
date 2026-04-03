package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.AbstractIdentity;
import it.mapsgroup.gzoom.persistence.common.CustomTransactionStatus;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.time.LocalDateTime;

public abstract class AbstractDao {

    public TransactionStatus getTxStatus() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return TransactionAspectSupport.currentTransactionStatus();
        } else
            return null;
    }

    public LocalDateTime getTxTimestamp() {
        TransactionStatus txStatus = getTxStatus();
        if (txStatus != null && txStatus instanceof CustomTransactionStatus) {
            return ((CustomTransactionStatus) txStatus).getTimestamp();
        } else {
            return null;
        }
    }

    public Instant getTxTimestamp2() {
        TransactionStatus txStatus = getTxStatus();
        if (txStatus != null && txStatus instanceof CustomTransactionStatus) {
            return ((CustomTransactionStatus) txStatus).getTimestamp2();
        } else {
            return null;
        }
    }

    public void setCreatedTimestamp(AbstractIdentity record) {
        Instant now = Instant.now();
        Instant txTimestamp = getTxTimestamp2();
        record.setCreatedStamp(now);
        record.setCreatedTxStamp(txTimestamp);
        record.setLastUpdatedStamp(now);
        record.setLastUpdatedTxStamp(txTimestamp);
    }

    public void setUpdateTimestamp(AbstractIdentity record) {
        record.setLastUpdatedStamp(Instant.now());
        record.setLastUpdatedTxStamp(getTxTimestamp2());
    }


}
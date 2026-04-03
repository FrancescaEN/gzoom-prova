/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package it.mapsgroup.gzoom.persistence.common;

import org.slf4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Sequence Utility to get unique sequences from named sequence banks
 * Uses a collision detection approach to safely get unique sequenced ids in banks from the database
 */
public class SequenceUtil {
    private static final Logger LOG = getLogger(SequenceUtil.class);


    private final Map<String, SequenceBank> sequences = new Hashtable<>();
    private final long bankSize;
    private final String tableName;
    private final String nameColName;
    private final String idColName;
    private final PlatformTransactionManager txManager;
    private final DataSource dataSource;


    public SequenceUtil(String tableName, String nameFieldName, String idFieldName, Long sequenceBankSize, DataSource dataSource) {
        if (!StringUtils.hasLength(tableName)) {
            throw new IllegalArgumentException("The sequence model entity was null but is required.");
        }
        //this.tableName = seqEntity.getTableName(helperInfo.getHelperBaseName());
        this.tableName = tableName;


        this.nameColName = nameFieldName;
        this.idColName = idFieldName;
        long bankSize = SequenceBank.defaultBankSize;

        if (sequenceBankSize != null && sequenceBankSize > 0) {
            bankSize = sequenceBankSize;
        }
        this.bankSize = bankSize;
        this.txManager = new DataSourceTransactionManager(dataSource);
        this.dataSource = dataSource;
    }

    public Long getNextSeqId(String seqName, long staggerMax) {
        SequenceBank bank = this.getBank(seqName);
        return bank.getNextSeqId(staggerMax);
    }

//    public void forceBankRefresh(String seqName, long staggerMax) {
//        // don't use the get method because we don't want to create if it fails
//        SequenceBank bank = sequences.get(seqName);
//        if (bank == null) {
//            return;
//        }
//
//        bank.refresh(staggerMax);
//    }

    private SequenceBank getBank(String seqName) {
        SequenceBank bank = sequences.get(seqName);

        if (bank == null) {
            synchronized (this) {
                bank = sequences.get(seqName);
                if (bank == null) {
                    bank = new SequenceBank(seqName);
                    sequences.put(seqName, bank);
                }
            }
        }

        return bank;
    }

    private class SequenceBank {
        public static final long defaultBankSize = 10;
        public static final long maxBankSize = 5000;
        public static final long startSeqId = 10000;
        public static final long minWaitMillis = 5;
        public static final long maxWaitMillis = 50;
        public static final int maxTries = 5;

        private long curSeqId;
        private long maxSeqId;
        private final String seqName;

        private SequenceBank(String seqName) {
            this.seqName = seqName;
            curSeqId = 0;
            maxSeqId = 0;
            fillBank(1);
        }

        private synchronized Long getNextSeqId(long staggerMax) {
            long stagger = 1;
            if (staggerMax > 1) {
                stagger = Math.round(Math.random() * staggerMax);
                if (stagger == 0) stagger = 1;
            }

            if ((curSeqId + stagger) <= maxSeqId) {
                Long retSeqId = curSeqId;
                curSeqId += stagger;
                return retSeqId;
            } else {
                fillBank(stagger);
                if ((curSeqId + stagger) <= maxSeqId) {
                    Long retSeqId = curSeqId;
                    curSeqId += stagger;
                    return retSeqId;
                } else {
                    LOG.error("[SequenceUtil.SequenceBank.getNextSeqId] Fill bank failed, returning null");
                    return null;
                }
            }
        }

//        private void refresh(long staggerMax) {
//            this.curSeqId = this.maxSeqId;
//            this.fillBank(staggerMax);
//        }

        private synchronized void fillBank(long stagger) {
            //Debug.logWarning("[SequenceUtil.SequenceBank.fillBank] Starting fillBank Thread Name is: " + Thread.currentThread().getName() + ":" + Thread.currentThread().toString(), module);

            // no need to get a new bank, SeqIds available
            if ((curSeqId + stagger) <= maxSeqId) return;

            long bankSize = SequenceUtil.this.bankSize;
            if (stagger > 1) {
                // NOTE: could use staggerMax for this, but if that is done it would be easier to guess a valid next id without a brute force attack
                bankSize = stagger * defaultBankSize;
            }

            if (bankSize > maxBankSize) bankSize = maxBankSize;

            long val1 = 0;
            long val2 = 0;

            // NOTE: the fancy ethernet type stuff is for the case where transactions not available, or something funny happens with really sensitive timing (between first select and update, for example)
            int numTries = 0;

            while (val1 + bankSize != val2) {
                if (LOG.isTraceEnabled())
                    LOG.trace("[SequenceUtil.SequenceBank.fillBank] Trying to get a bank of sequenced ids for {}; start of loop val1={}, val2={}, bankSize={}", this.seqName, val1, val2, bankSize);

                // not sure if this synchronized block is totally necessary, the method is synchronized, but it does do a wait/sleep
                //outside of this block, and this is the really sensitive block, so making sure it is isolated; there is some overhead
                //to this, but very bad things can happen if we try to do too many of these at once for a single sequencer
                synchronized (this) {
                    try {
                        //if we can suspend the transaction, we'll try to do this in a local manual transaction
                        //suspendedTransaction = TransactionUtil.suspend();

                        TransactionStatus beganTransaction = null;
                        try {
                            // beganTransaction = TransactionUtil.begin();
                            DefaultTransactionDefinition td = new DefaultTransactionDefinition();
                            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                            beganTransaction = txManager.getTransaction(td);

                            Connection connection;
                            Statement stmt = null;
                            ResultSet rs;

                            try {
                                connection = dataSource.getConnection();//ConnectionFactory.getConnection(SequenceUtil.this.helperInfo);
                            } catch (SQLException sqle) {
                                LOG.error("[SequenceUtil.SequenceBank.fillBank]: Unable to esablish a connection with the database... Error was:", sqle);
                                throw sqle;
                            } catch (Exception e) {
                                LOG.error("[SequenceUtil.SequenceBank.fillBank]: Unable to esablish a connection with the database... Error was: ", e);
                                throw e;
                            }

                            if (connection == null) {
                                throw new PersistenceException("[SequenceUtil.SequenceBank.fillBank]: Unable to esablish a connection with the database, connection was null...");
                            }

                            String sql = null;

                            try {
                                // we shouldn't need this, and some TX managers complain about it, so not including it: connection.setAutoCommit(false);

                                stmt = connection.createStatement();

                                sql = "SELECT " + SequenceUtil.this.idColName + " FROM " + SequenceUtil.this.tableName + " WHERE " + SequenceUtil.this.nameColName + "='" + this.seqName + "'";
                                rs = stmt.executeQuery(sql);
                                boolean gotVal1 = false;
                                if (rs.next()) {
                                    val1 = rs.getLong(SequenceUtil.this.idColName);
                                    gotVal1 = true;
                                }
                                rs.close();

                                if (!gotVal1) {
                                    LOG.warn("[SequenceUtil.SequenceBank.fillBank] first select failed: will try to add new row, result set was empty for sequence [{}] \nUsed SQL: {} \n Thread Name is: {}:{}", seqName, sql, Thread.currentThread().getName(), Thread.currentThread());
                                    sql = "INSERT INTO " + SequenceUtil.this.tableName + " (" + SequenceUtil.this.nameColName + ", " + SequenceUtil.this.idColName + ") VALUES ('" + this.seqName + "', " + startSeqId + ")";
                                    if (stmt.executeUpdate(sql) <= 0) {
                                        throw new PersistenceException("No rows changed when trying insert new sequence row with this SQL: " + sql);
                                    }
                                    continue;
                                }

                                sql = "UPDATE " + SequenceUtil.this.tableName + " SET " + SequenceUtil.this.idColName + "=" + SequenceUtil.this.idColName + "+" + bankSize + " WHERE " + SequenceUtil.this.nameColName + "='" + this.seqName + "'";
                                if (stmt.executeUpdate(sql) <= 0) {
                                    throw new PersistenceException("[SequenceUtil.SequenceBank.fillBank] update failed, no rows changes for seqName: " + seqName);
                                }

                                sql = "SELECT " + SequenceUtil.this.idColName + " FROM " + SequenceUtil.this.tableName + " WHERE " + SequenceUtil.this.nameColName + "='" + this.seqName + "'";
                                rs = stmt.executeQuery(sql);
                                boolean gotVal2 = false;
                                if (rs.next()) {
                                    val2 = rs.getLong(SequenceUtil.this.idColName);
                                    gotVal2 = true;
                                }

                                rs.close();

                                if (!gotVal2) {
                                    throw new PersistenceException("[SequenceUtil.SequenceBank.fillBank] second select failed: aborting, result set was empty for sequence: " + seqName);
                                }

                                // got val1 and val2 at this point, if we don't have the right difference between them, force a rollback (with
                                //setRollbackOnly and NOT with an exception because we don't want to break from the loop, just err out and
                                //continue), then flow out to allow the wait and loop thing to happen
                                if (val1 + bankSize != val2) {
                                    beganTransaction.setRollbackOnly();
                                    LOG.warn("Forcing transaction rollback in sequence increment because we didn't get a clean update, ie a conflict was found, so not saving the results");
                                }
                            } catch (SQLException sqle) {
                                LOG.error("[SequenceUtil.SequenceBank.fillBank] SQL Exception while executing the following:\n{}\nError was:{}", sql, sqle.getMessage(), sqle);
                                throw sqle;
                            } finally {
                                try {
                                    if (stmt != null) stmt.close();
                                } catch (SQLException sqle) {
                                    LOG.error("Error closing statement in sequence util", sqle);
                                }
                                try {
                                    connection.close();
                                } catch (SQLException sqle) {
                                    LOG.error("Error closing connection in sequence util", sqle);
                                }
                            }
                        } catch (Exception e) {
                            String errMsg = "General error in getting a sequenced ID";
                            LOG.error(errMsg, e);
                            try {
                                if (beganTransaction != null) {
                                    txManager.rollback(beganTransaction);
                                }
                            } catch (TransactionException gte2) {
                                LOG.error("Unable to rollback transaction", gte2);
                            }

                            // error, break out of the loop to not try to continue forever
                            break;
                        } finally {
                            try {
                                if (beganTransaction != null) {
                                    txManager.commit(beganTransaction);
                                }
                            } catch (TransactionException gte) {
                                LOG.error("Unable to commit sequence increment transaction, continuing anyway though", gte);
                            }
                        }
                    } catch (TransactionException e) {
                        LOG.error("System Error suspending transaction in sequence util", e);
                    }
                }

                if (val1 + bankSize != val2) {
                    if (numTries >= maxTries) {
                        String errMsg = "[SequenceUtil.SequenceBank.fillBank] maxTries (" + maxTries + ") reached for seqName [" + this.seqName + "], giving up.";
                        LOG.error(errMsg);
                        return;
                    }

                    // collision happened, wait a bounded random amount of time then continue
                    long waitTime = (long) (Math.random() * (maxWaitMillis - minWaitMillis) + minWaitMillis);

                    LOG.warn("[SequenceUtil.SequenceBank.fillBank] Collision found for seqName [{}], val1={}, val2={}, val1+bankSize={}, bankSize={}, waitTime={}", seqName, val1, val2, val1 + bankSize, bankSize, waitTime);

                    try {
                        // using the Thread.sleep to more reliably lock this thread: this.wait(waitTime);
                        java.lang.Thread.sleep(waitTime);
                    } catch (Exception e) {
                        LOG.error("Error waiting in sequence util", e);
                        return;
                    }
                }

                numTries++;
            }

            curSeqId = val1;
            maxSeqId = val2;
            if (LOG.isInfoEnabled())
                LOG.info("Got bank of sequenced IDs for [{}]; curSeqId={}, maxSeqId={}, bankSize={}", this.seqName, curSeqId, maxSeqId, bankSize);
            //Debug.logWarning("[SequenceUtil.SequenceBank.fillBank] Ending fillBank Thread Name is: " + Thread.currentThread().getName() + ":" + Thread.currentThread().toString(), module);
        }
    }
}

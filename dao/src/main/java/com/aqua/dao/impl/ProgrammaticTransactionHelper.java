package com.aqua.dao.impl;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Helper for programmatic Spring transactions management.
 * Could be useful in cases when Spring AOP transaction implementation is not suitable.
 */
public class ProgrammaticTransactionHelper {

    private PlatformTransactionManager txManager;

    public void setTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    /**
     * Run txCall with propagation REQUIRES_NEW
     *
     * @param txCall
     * @param <T>
     * @return
     */
    public <T> T txNew(final TransactionCallback<T> txCall) {
        TransactionTemplate txTemplate = newTransactionTemplate();
        txTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        return txTemplate.execute(txCall);
    }

    /**
     * Run txCall with propagation REQUIRED.
     *
     * @param txCall
     * @param <T>
     * @return
     */
    public <T> T txRequired(final TransactionCallback<T> txCall) {
        TransactionTemplate txTemplate = newTransactionTemplate();
        txTemplate.setPropagationBehavior(Propagation.REQUIRED.value());
        return txTemplate.execute(txCall);
    }

    /**
     * Run txCall with propagation REQUIRES_NEW and readOnly true.
     *
     * @param txCall
     * @param <T>
     * @return
     */
    public <T> T txNewReadOnly(final TransactionCallback<T> txCall) {
        TransactionTemplate txTemplate = newTransactionTemplate();
        txTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
        txTemplate.setReadOnly(true);
        return txTemplate.execute(txCall);
    }

    protected TransactionTemplate newTransactionTemplate() {
        return new TransactionTemplate(txManager);
    }

    public boolean isTransactionOpened() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
        try {
            txManager.getTransaction(definition);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
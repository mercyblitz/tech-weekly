/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.alibaba.druid.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Transaction Configuration
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Configuration
public class TransactionConfiguration implements BeanPostProcessor {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ObjectProvider<TransactionSynchronization> transactionSynchronizations;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PlatformTransactionManager) {
            PlatformTransactionManager ptm = (PlatformTransactionManager) bean;
            return new LifecyclePlatformTransactionManager(ptm, applicationEventPublisher, transactionSynchronizations);
        }
        return bean;
    }

    private static class LifecyclePlatformTransactionManager implements CallbackPreferringPlatformTransactionManager {

        private final PlatformTransactionManager delegate;

        private final CallbackPreferringPlatformTransactionManager callbackPreferringDelegate;

        private final ApplicationEventPublisher applicationEventPublisher;

        private final ObjectProvider<TransactionSynchronization> transactionSynchronizations;

        LifecyclePlatformTransactionManager(PlatformTransactionManager delegate,
                                            ApplicationEventPublisher applicationEventPublisher,
                                            ObjectProvider<TransactionSynchronization> transactionSynchronizations) {
            this.delegate = delegate;
            this.callbackPreferringDelegate = delegate instanceof CallbackPreferringPlatformTransactionManager ?
                    (CallbackPreferringPlatformTransactionManager) delegate : null;
            this.applicationEventPublisher = applicationEventPublisher;
            this.transactionSynchronizations = transactionSynchronizations;
        }

        @Override
        public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
            registerSynchronizations(null);
            TransactionStatus status = delegate.getTransaction(definition);
            if (status.isNewTransaction()) {
                registerSynchronizations(status);
            }
            return status;
        }

        private void registerSynchronizations(TransactionStatus status) {
            if (!TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.initSynchronization();
            }
            if (status != null) {
                applicationEventPublisher.publishEvent(status);
            }
            for (TransactionSynchronization transactionSynchronization : transactionSynchronizations) {
                TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
            }
        }

        @Override
        public void commit(TransactionStatus status) throws TransactionException {
            delegate.commit(status);
        }

        @Override
        public void rollback(TransactionStatus status) throws TransactionException {
            delegate.rollback(status);
        }

        @Override
        public <T> T execute(TransactionDefinition definition, TransactionCallback<T> callback) throws TransactionException {
            return callbackPreferringDelegate.execute(definition, callback);
        }
    }

}

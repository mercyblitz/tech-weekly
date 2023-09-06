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
package com.example.alibaba.druid.sample.acid;

import com.example.alibaba.druid.config.TransactionConfiguration;
import com.example.alibaba.druid.service.TransactionMessageService;
import com.example.alibaba.druid.service.TransactionService;
import com.example.alibaba.druid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本地事务示例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@RestController
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@Import({TransactionService.class,
        UserService.class,
        TransactionMessageService.class,
//        TransactionConfiguration.class
})
public class LocalTransactionSample {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @GetMapping("/tx/{sellerId}/{buyerId}/{amount}")
    @Transactional
    public boolean tx(@PathVariable Long sellerId, @PathVariable Long buyerId, @PathVariable Long amount) {
        Long txId = transactionService.addTransaction(sellerId, buyerId, amount);
        userService.updateAmount(txId, sellerId, buyerId, amount);
        return true;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(Object object) {
        System.out.println(object);
        // TODO
    }

    @Bean
    public TransactionSynchronization transactionSynchronization() {
        return new TransactionSynchronization() {
            @Override
            public void suspend() {
                System.out.println("suspend");
            }

            @Override
            public void beforeCommit(boolean readOnly) {
                System.out.println("beforeCommit");
            }

            @Override
            public void beforeCompletion() {
                System.out.println("beforeCompletion");
            }

            @Override
            public void afterCommit() {
                System.out.println("afterCommit");
            }

            @Override
            public void afterCompletion(int status) {
                System.out.println("afterCompletion");
            }
        };
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(LocalTransactionSample.class)
                .run(args);
    }


}

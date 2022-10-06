package br.com.doistech.services

import br.com.doistech.domain.*
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Prototype
import org.hibernate.FetchMode

@Slf4j
@Transactional
@Prototype
class AccountService {

    public Company getAccount(String name){
        Account account

        Account.withTransaction {
            account = Account.createCriteria().get {
                eq('name', name)
            }
        }

        return account
    }

    public Account getAccountByUser(User user){
        Account account

        Account.withTransaction {
            account = Account.createCriteria().get {
                eq('user', user)
                fetchMode("company", FetchMode.JOIN)
                fetchMode("user", FetchMode.JOIN)
            }
        }

        return account
    }
}

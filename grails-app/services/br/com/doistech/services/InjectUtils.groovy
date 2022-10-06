package br.com.doistech.services

import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Prototype

@Slf4j
@Transactional
@Prototype

class InjectUtils {

//    static def getBean(String beanName) {
//        return Holders.grailsApplication.mainContext.getBean(beanName)
//    }
}

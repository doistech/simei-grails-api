package br.com.doistech.services

import br.com.doistech.domain.*

class UserService {

    public User getUser(String email){
        User user

        User.withTransaction {
            user = User.createCriteria().get {
                eq('username', email)
            }
        }

        return user
    }
}

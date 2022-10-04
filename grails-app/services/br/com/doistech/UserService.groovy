package br.com.doistech

import br.com.doistech.User

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

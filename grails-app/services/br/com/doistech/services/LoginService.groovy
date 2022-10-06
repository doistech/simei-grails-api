package br.com.doistech.services

import br.com.doistech.domain.*

class LoginService {

    UserProfileService userProfileService
    SessionService sessionService

    public Boolean login(String username, String password){
        User user = userProfileService.getUser(username)
        String passwordAux = userProfileService.encryptPassword(password)
        if(user){
            if(user.password == passwordAux){
                sessionService.startSessionLogin(user)
                return true
            } else {
                return false
            }
        } else {
//            Messagebox.show("Usuário ou senha incorreto!",
//                    'Simplifica MEI', 0,  Messagebox.ERROR)
            return false
        }
    }
}

package br.com.doistech.services

import br.com.doistech.domain.*
import org.hibernate.FetchMode

class ClienteService {

    List<Cliente> getClienteList(){
        List<Cliente> list = []

        Cliente.withTransaction {
            list = Cliente.createCriteria().list {
                ne('name', 'Cliente Balcão')
                fetchMode('company', FetchMode.JOIN)
            }
        }

        return list
    }

    Cliente getCliente(){
        return Cliente.get(1.toLong())
    }
}

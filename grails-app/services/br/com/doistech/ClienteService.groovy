package br.com.doistech

import br.com.doistech.Cliente
import com.mysql.cj.xdevapi.Client
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

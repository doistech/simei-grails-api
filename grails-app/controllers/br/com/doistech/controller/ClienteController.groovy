package br.com.doistech.controller

import br.com.doistech.domain.Cliente
import br.com.doistech.services.ClientService
import br.com.doistech.services.ClienteService

class ClienteController {
    static responseFormats = ['json', 'xml']

    ClienteService clienteService
    ClientService clientService

    def index() {
        Cliente cliente = clienteService.getCliente()
        [cliente: cliente]
    }

//    localhost:8080/cliente?companyId=2&searchInput=Brenno
    def show() {
        [clienteMap: clientService.searchCliente([companyId:params.companyId,searchInput: params?.searchInput])]
    }

    def upsert(Cliente cliente) {
        [clienteMap: clientService.upsertCliente(cliente)]
    }
}

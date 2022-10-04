package br.com.doistech

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
}

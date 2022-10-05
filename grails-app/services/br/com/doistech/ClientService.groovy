package br.com.doistech



import org.hibernate.FetchMode

class ClientService {

    CompanyService companyService

//    public ErrorUi validationCliente (Cliente cliente){
//        ErrorUi errorUi = new ErrorUi()
//
//        if(!cliente.name){
//            errorUi.addError('ClientNome', 'Por favor preencha o nome do cliente')
//        }
//
//        return errorUi
//    }

    public Boolean save(Cliente cliente, Company company, String username) {
        cliente.dateCreated = new Date()
        cliente.dateUpdated = new Date()

        cliente.createdBy = username
        cliente.updatedBy = username

        cliente.company = company
//        cliente.company = Company.get(7.toLong())

        Cliente.withTransaction {
            try{
                cliente.save(flush:true)
            } catch(Exception e){
                println('Error - ' + e.message)
                return false
            }
        }
        return true
    }

    List<Cliente> getClienteByCompany(Company company){
        List<Cliente> clienteList = []
        Cliente.withTransaction {
            clienteList = Cliente.createCriteria().list {
                eq('company', company)
                order("name", "asc")
                fetchMode('company', FetchMode.JOIN)
            }
        }
        return clienteList
    }

    Cliente getClienteById(Long id){
        return Cliente.createCriteria().get {
            eq('id', id)
            fetchMode("cliente", FetchMode.JOIN)
            fetchMode('company', FetchMode.JOIN)
        }
    }

    List<Cliente> search(String name, Company company){
        List<Cliente> clienteList = []

        Cliente.withTransaction {
            clienteList = Cliente.createCriteria().list {
                if(name) like('name', '%' + name + '%')
                eq('company', company)
                order("name", "asc")
                fetchMode('company', FetchMode.JOIN)
            }
        }
        return clienteList
    }

    Map searchCliente(Map map){
        List<Cliente> clienteList = []
        Map resultService = [:]

        try{
            Company company = companyService.getCompanyById(map.companyId.toLong())
            if(company){
                clienteList = search(map?.searchInput,company)
                resultService.status = 0
                resultService.clienteMapList = clienteList as List<Map>
                resultService.message = "Transaction processed successfully"
                return resultService
            }else{
                resultService.status = 1
                resultService.clienteMapList = []
                resultService.message = "Company not found"
                return resultService
            }
        }catch(Exception e){
            resultService.status = 2
            resultService.clienteMapList = []
            resultService.message = "Error SearchCliente - ${e.message}"
            println "Error SearchCliente - ${e.message}"
            return resultService
        }

    }

    Map upsertCliente(Cliente _cliente){
        Map resultService = [:]

        try {
            if(_cliente.company.id){
                Cliente cliente
                Cliente.withTransaction {
                    cliente = getClienteById(!_cliente?.id ? 0 : _cliente.id)
                    if(!cliente) cliente = new Cliente()

                    cliente.createdBy = _cliente.createdBy
                    cliente.updatedBy = _cliente.updatedBy

                    cliente.dateCreated = new Date()
                    cliente.dateUpdated = new Date()

                    cliente.name  = _cliente.name
                    cliente.taxId = _cliente?.taxId

                    // Contato
                    cliente.email = _cliente?.email
                    cliente.phone = _cliente?.phone
                    cliente.mobilePhone = _cliente?.phone

                    // endereco
                    cliente.postalCode = _cliente?.postalCode
                    cliente.address = _cliente?.address
                    cliente.addressComplement = _cliente?.addressComplement
                    cliente.addressReference = _cliente?.addressReference
                    cliente.addressNumber = _cliente?.addressNumber

                    // Relação
                    cliente.company = companyService.getCompanyById(_cliente.company.id)

                    cliente.save(flush:true)

                    resultService.status = 0
                    resultService.clienteMap = [id:cliente.id, name: cliente.name, companyId: cliente.company.id]
                    resultService.message = "Transaction processed successfully"
                    return resultService
                }
            }else{
                resultService.status = 1
                resultService.clienteMap = [:]
                resultService.message = "Company not found"
                return resultService
            }
        }catch(Exception e){
            resultService.status = 2
            resultService.clienteMap = [:]
            resultService.message = "Error upsertCliente - ${e.message}"
            println resultService.message
            return resultService
        }
    }

}

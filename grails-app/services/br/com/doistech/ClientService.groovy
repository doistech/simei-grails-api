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

    Map upsertCliente(Map map){
        Map resultService = [:]

        try {
            if(map.companyId){
                Cliente cliente
                Cliente.withTransaction {
                    cliente = getClienteById(map?.id)
                    if(!cliente) cliente = new Cliente()

                    cliente.createdBy = map.username
                    cliente.updatedBy = map.username

                    cliente.dateCreated = new Date()
                    cliente.dateUpdated = new Date()

                    cliente.name  = map.name
                    cliente.taxId = map?.taxId

                    // Contato
                    cliente.email = map?.email
                    cliente.phone = map?.phone
                    cliente.mobilePhone = map?.phone

                    // endereco
                    cliente.postalCode = map?.postalCode
                    cliente.address = map?.address
                    cliente.addressComplement = map?.addressComplement
                    cliente.addressReference = map?.addressReference
                    cliente.addressNumber = map?.addressNumber

                    // Relação
                    cliente.company = companyService.getCompanyById(map.companyId)

                    cliente.save(flush:true)

                    resultService.status = 0
                    resultService.clienteMap = [id:cliente.id, name: cliente.name]
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

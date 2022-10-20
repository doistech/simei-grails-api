package br.com.doistech.services

import br.com.doistech.domain.*

class ProductService {

    CompanyService companyService

    List<Product> getProductList(Company company) {
        List<Product> productList = []

        Product.withTransaction {
            productList = RawMaterial.createCriteria().list {
                eq('company', company)
                eq('isDelete', false)
            }
        }

        return productList
    }

    List<RawMaterial> search(Company company, String name = null) {
        List<Product> productList = []
        Product.withTransaction {
            productList = Product.createCriteria().list {
                if (name) like('name', '%' + name + '%')
                eq('company', company)
                eq('isDelete', false)
            }
        }
        return productList
    }

    Map searchProduct(Map map) {
        List<Product> produtoList = []
        Map resultService = [:]

        try {
            Company company = companyService.getCompanyById(map.companyId.toLong())
            if (company) {
                produtoList = search(company, map?.searchInput)
                resultService.status = 0
                resultService.productMapList = produtoList as List<Map>
                resultService.message = "Transaction processed successfully"
                return resultService
            } else {
                resultService.status = 1
                resultService.productMapList = []
                resultService.message = "Company not found"
                return resultService
            }
        } catch (Exception e) {
            resultService.status = 2
            resultService.productMapList = []
            resultService.message = "Error searchProduct - ${e.message}"
            println "Error searchProduct - ${e.message}"
            return resultService
        }
    }

    List<Cliente> getProductByCompany(Company company) {
        List<Product> productList = []

        Product.withTransaction {
            productList = Product.createCriteria().list {
                eq('company', company)
                eq('isDelete', false)
                order("name", "asc")
            }
        }
        return productList
    }

//    public ErrorUi validationProduct(Product product){
//        ErrorUi errorUi = new ErrorUi()
//
//        if(!product.name){
//            errorUi.addError('name', 'Por favor preencha o nome do produto')
//        }
//
//        if(!product.salesPrice){
//            errorUi.addError('price', 'Por favor preencha a % Lucro Varejo')
//        }
//        return errorUi
//    }

    public Boolean save(Product product, Company company, String username) {
        product.dateCreated = new Date()
        product.dateUpdated = new Date()

        product.createdBy = username
        product.updatedBy = username

        product.company = company
        product.codeEan = ''
        product.description = ''

        Product.withTransaction {
            try {
                product.save(flush: true)
            } catch (Exception e) {
                println('Error - ' + e.message)
                return false
            }
        }
        return true
    }

    Map upsertProduct(Product _produto) {
        Map resultService = [:]

        try {
            if (_produto.company.id) {
                Product product
                Product.withTransaction {
                    product.dateCreated = new Date()
                    product.dateUpdated = new Date()

                    product.codeEan = !product.codeEan ? '' : product.codeEan
                    product.description = !product.description ? '' : product.description

                    try {
                        product.save(flush: true)
                        resultService.status = 0
                        resultService.productMap = [id: product.id, name: product.name, companyId: product.company.id]
                        resultService.message = "Transaction processed successfully"
                        return resultService
                    } catch (Exception e) {
                        resultService.status = 0
                        resultService.productMap = []
                        resultService.message = 'Error - ' + e.message
                        return resultService
                        println('Error upsertProduct - ' + e.message)
                    }
                }
            } else {
                resultService.status = 1
                resultService.clienteMap = [:]
                resultService.message = "Company not found"
                return resultService
            }
        } catch (Exception e) {
            resultService.status = 2
            resultService.clienteMap = [:]
            resultService.message = "Error upsertProduct - ${e.message}"
            println resultService.message
            return resultService
        }
    }

    Product getProductById(Long id) {
        Product product

        Product.withTransaction {
            product = Product.createCriteria().get {
                eq('id', id)
                eq('isDelete', false)
            }
        }

        return product
    }
}

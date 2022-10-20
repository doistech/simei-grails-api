package br.com.doistech.services

import br.com.doistech.domain.*
import org.hibernate.FetchMode

class RawMaterialService {

    CompanyService companyService

    List<RawMaterial> getRawMaterialList(Company company){
        List<RawMaterial> rawMaterialList = []

        RawMaterial.withTransaction {
            rawMaterialList = RawMaterial.createCriteria().list {
                eq('company', company)
                order("name", "asc")
            }
        }

        return rawMaterialList
    }

    List<RawMaterial> searchRawMaterial(String name, Company company){
        List<RawMaterial> rawMaterialList = []

        RawMaterial.withTransaction {
            rawMaterialList = RawMaterial.createCriteria().list {
                like('name', '%' + name + '%')
                eq('company', company)
                order("name", "asc")
            }
        }
        return rawMaterialList
    }

    List<Cliente> getRawMaterialByCompany(Company company){
        List<RawMaterial> rawMaterialList = []
        Cliente.withTransaction {
            rawMaterialList = RawMaterial.createCriteria().list {
                eq('company', company)
                order("name", "asc")
            }
        }
        return rawMaterialList
    }

//    public ErrorUi validationRawMaterial(RawMaterial rawMaterial){
//        ErrorUi errorUi = new ErrorUi()
//
//        if(!rawMaterial.name){
//            errorUi.addError('name', 'Por favor preencha o nome da materia prima')
//        }
//
//        if(!rawMaterial.price){
//            errorUi.addError('price', 'Por favor preencha o preço da materia prima')
//        }
//
//        return errorUi
//    }

    Boolean changesPriceRawMaterialProduct(RawMaterial rawMaterial, User user, Company company) {
        try {
            List<ProductRawMaterial> productRawMaterialList = ProductRawMaterial.createCriteria().list {
                eq('rawMaterial', rawMaterial)
                eq('company', company)
                fetchMode("product", FetchMode.JOIN)
                fetchMode("company", FetchMode.JOIN)
                fetchMode("rawMaterial", FetchMode.JOIN)
            }

            if (productRawMaterialList.size() > 0) {
                productRawMaterialList.each { rm ->
                    calculationProductRawMaterial(rm.product, company, user)
                }
            }

            return true
        } catch (Exception e){
            e.printStackTrace()
            return false
        }

    }

    // Vou usar para atualizar os preços
    void calculationProductRawMaterial(Product product, Company company, User user){
        List<ProductRawMaterial> productRawMaterialList = ProductRawMaterial.createCriteria().list {
            eq('product', product)
            eq('company', company)
            fetchMode("product", FetchMode.JOIN)
            fetchMode("company", FetchMode.JOIN)
            fetchMode("rawMaterial", FetchMode.JOIN)
        }

        if(productRawMaterialList.size > 0){
            Double productOrderPrice = 0

            productRawMaterialList.each { productRawMaterial ->
                if(user) productRawMaterial.updatedBy = user.username
                productRawMaterial.dateUpdated = new Date()
                productRawMaterial.product = product
                productRawMaterial.calculationPrice(productRawMaterial.rawMaterial)
                productRawMaterial.save(flush:true)

                productOrderPrice += productRawMaterial.price
            }

            product.updatedBy = user.username
            product.dateUpdated = new Date()
            product.productOrderPrice = productOrderPrice
            product.calculationPriceSales(product.productOrderPrice)
            product.calculationWholesalePrice(product.productOrderPrice)
            product.save(flush:true)
        }
    }

    public Boolean save(RawMaterial rawMaterial, Company company, String username) {
        rawMaterial.dateCreated = new Date()
        rawMaterial.dateUpdated = new Date()

        rawMaterial.createdBy = username
        rawMaterial.updatedBy = username

        rawMaterial.company = company

        RawMaterial.withTransaction {
            try{
                rawMaterial.save(flush:true)
            } catch(Exception e){
                println('Error - ' + e.message)
                return false
            }
        }
        return true
    }

    Map searchRawMaterial(Long companyId, String inputSearch = null){
        Map resultServiceMap = [:]
        List<RawMaterial> rawMaterialList = []

        try{
            if(companyId){
                Company company = companyService.getCompanyById(companyId)

                RawMaterial.withTransaction {
                    rawMaterialList = RawMaterial.createCriteria().list {
                        if(inputSearch) like('name', '%' + inputSearch + '%')
                        eq('company', company)
                        order("name", "asc")
                        fetchMode("company", FetchMode.JOIN)
                    }
                }

                resultServiceMap.status = 0
                resultServiceMap.rawMaterialList = rawMaterialList
                resultServiceMap.message = "Transaction processed successfully"
                return resultServiceMap
            }else{
                resultServiceMap.status = 1
                resultServiceMap.rawMaterialList = []
                resultServiceMap.message = "Company not found"
                return resultServiceMap
            }
        }catch(Exception e){
            resultServiceMap.status = 2
            resultServiceMap.rawMaterialList = []
            resultServiceMap.message = "Error searchRawMaterial - ${e.message}"
            e.printStackTrace()
            return resultServiceMap
        }
    }

    Map upsertRawMaterial(RawMaterial _rawMaterial){
        Map resultServiceMap = [:]

        try {
            if (_rawMaterial.company.id) {
                RawMaterial.withTransaction {
                    if(!_rawMaterial.id){
                        _rawMaterial.dateCreated = new Date()
                    }
                    _rawMaterial.dateUpdated = new Date()

                    try {
                        _rawMaterial.save(flush: true)
                        resultServiceMap.status = 0
                        resultServiceMap.rawMaterialMap = [id: _rawMaterial.id, name: _rawMaterial.name, companyId: _rawMaterial.company.id]
                        resultServiceMap.message = "Transaction processed successfully"
                        return resultServiceMap
                    } catch (Exception e) {
                        resultServiceMap.status = 0
                        resultServiceMap.rawMaterialMap = []
                        resultServiceMap.message = 'Error - ' + e.message
                        return resultServiceMap
                        println('Error upsertRawMaterial - ' + e.message)
                    }
                }
            } else {
                resultServiceMap.status = 1
                resultServiceMap.rawMaterialMap = [:]
                resultServiceMap.message = "Company not found"
                return resultServiceMap
            }
        } catch (Exception e) {
            resultServiceMap.status = 2
            resultServiceMap.rawMaterialMap = [:]
            resultServiceMap.message = "Error upsertRawMaterial - ${e.message}"
            println resultServiceMap.message
            return resultServiceMap
        }
    }
}

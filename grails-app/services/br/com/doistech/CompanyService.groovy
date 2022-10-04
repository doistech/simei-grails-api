package br.com.doistech

import br.com.doistech.Company

class CompanyService {

    public Company getCompanyByTaxId(String taxId){
        Company company

        Company.withTransaction {
            company = Company.createCriteria().get {
                eq('taxid', taxId)
            }
        }

        return company
    }

    public Company getCompanyById(Long id){
        return Company.createCriteria().get {
            eq('id', id)
            maxResults(1)
        }
    }


}

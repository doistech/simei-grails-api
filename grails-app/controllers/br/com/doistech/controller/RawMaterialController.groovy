package br.com.doistech.controller

import br.com.doistech.domain.RawMaterial
import br.com.doistech.services.RawMaterialService
import grails.rest.*
import grails.converters.*

class RawMaterialController {
	static responseFormats = ['json', 'xml']


    RawMaterialService rawMaterialService
	
    def index() { }

    def show() {
        [rawMaterialMap: rawMaterialService.searchRawMaterial(params.companyId.toLong(), params?.searchInput)]
    }

    def upsert(RawMaterial rawMaterial){
        [rawMaterialMap: rawMaterialService.upsertRawMaterial(rawMaterial)]
    }
}

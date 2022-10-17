package br.com.doistech.controller

import br.com.doistech.domain.Cliente
import br.com.doistech.domain.Product
import br.com.doistech.services.ProductService
import grails.rest.*
import grails.converters.*

class ProductController {
    static responseFormats = ['json', 'xml']

    ProductService productService

    def index() {}

    def get() {}

    def show() {
        [resultServiceMap: productService.searchProduct([companyId: params.companyId, searchInput: params?.searchInput])]
    }

    def upsert(Product product) {
        [productMap: productService.upsertProduct(product)]
    }
}
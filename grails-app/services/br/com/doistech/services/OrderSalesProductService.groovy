package br.com.doistech.services

import br.com.doistech.domain.*
import org.hibernate.FetchMode


class OrderSalesProductService {

    List<OrderSalesProduct> getOrderSalesProductList(OrderSales orderSales){
        List<OrderSalesProduct> orderSalesProductList = OrderSalesProduct.createCriteria().list {
            eq('order',orderSales)
            fetchMode("orderSales", FetchMode.JOIN)
            fetchMode("product", FetchMode.JOIN)
            fetchMode("order", FetchMode.JOIN)
            fetchMode("company", FetchMode.JOIN)
        }
        return orderSalesProductList
    }

    List<MethodPaymentOrderSales> getMethodPaymentOrderSalesList(OrderSales orderSales){
        List<MethodPaymentOrderSales> methodPaymentOrderSalesList = MethodPaymentOrderSales.createCriteria().list {
            eq('orderSales',orderSales)
            fetchMode("orderSales", FetchMode.JOIN)
            fetchMode("methodPayment", FetchMode.JOIN)
        }

        return methodPaymentOrderSalesList
    }

    List<MethodPaymentOrderSales> getMethodPaymentOrderSalesList(List<OrderSales> orderSalesList){
        List<MethodPaymentOrderSales> methodPaymentOrderSalesList = MethodPaymentOrderSales.createCriteria().list {
            'in'('orderSales',orderSalesList)
            fetchMode("orderSales", FetchMode.JOIN)
            fetchMode("methodPayment", FetchMode.JOIN)
        }

        return methodPaymentOrderSalesList
    }

    List<OrderSalesProduct> getOrderSalesProductListByOrderSalesList(List<OrderSales> orderSalesList){
        List<OrderSalesProduct> orderSalesProductList = OrderSalesProduct.createCriteria().list {
            'in'('order', orderSalesList)
            fetchMode("order", FetchMode.JOIN)
            fetchMode("product", FetchMode.JOIN)
            fetchMode("cliente", FetchMode.JOIN)
            fetchMode("company", FetchMode.JOIN)
        }
        return orderSalesProductList
    }
}

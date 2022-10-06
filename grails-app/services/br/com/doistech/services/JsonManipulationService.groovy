package br.com.doistech.services

import br.com.doistech.domain.*

class JsonManipulationService {

    OrderSales JSONManipulationHeaderAndFooterOrderSales(OrderSales orderSales){
        orderSales.jsonHeader = "{'orderSalesID':${orderSales.id},\n" +
                "        'orderSalesDate': '${orderSales.orderDate}',\n" +
                "        'orderSalesDeliveryDate':'${orderSales.deliveryDate}',\n" +
                "        'orderSalesProductList':["

        orderSales.jsonFooter = "]}"

        return orderSales
    }

    String JSONManipulationItemProductOrderSales(OrderSalesProduct orderSalesProduct, List<ProductRawMaterial> productRawMaterialList){
        String jsonItemProduct = "{'productID':'${orderSalesProduct.product.id}',\n" +
                "                'nameProduct':'${orderSalesProduct.product.name}',\n" +
                "                'productCompositionList':["

        productRawMaterialList.eachWithIndex { prm, index ->
            String sufix = ','

            if(productRawMaterialList.size() == (index + 1)) sufix = ''

            jsonItemProduct += "{\n" +
                    "                        'name':'${prm.rawMaterial.name}',\n" +
                    "                        'itemValue':${prm.price}\n" +
                    "                    }${sufix}"
        }
        jsonItemProduct += "]"

        return jsonItemProduct
    }

}

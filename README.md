# Spring Challenge

- [ESCENARIO](#escenario)
- [EXTRA BONUS](#extra-bonus)
- [USO DE LA APP](#uso-de-la-app)


### Escenario

Una plataforma de **venta de productos online** desea mejorar las opciones de búsqueda y filtrado de sus productos; para esto, decidió implementar un **search engine** el cual a partir de las opciones que el usuario determine, devuelva el o los productos que coincidan con las mismas.

Para ello, necesita el desarrollo de una API que proporcione:

1) Un listado de todos los productos disponibles.
2. Un listado de los productos filtrados por categoría.
3. Un listado que permita la combinación de cualquiera de los filtros. Por ejemplo: categoría + envío gratis.
   
Por otra parte, dado que se pretende una buena experiencia de usuario con respecto a la forma de presentación de los productos, se necesita que los resultados brindados por la API puedan ser ordenados mediante cualquiera de los siguientes criterios: asda
4. Alfabético (Ascendente y Descendente)
5. Mayor precio
6. Menor precio
   Al mismo tiempo se requiere una API que brinde:
7. La posibilidad de realizar el envío de una solicitud de compra. A partir de ésta, que se pueda recibir como devolución el precio total de la solicitud realizada. 
   
**Nota:** Tener en cuenta para cada una de estas solicitudes, los posibles “status code” que podrían ser devueltos.


### Extra Bonus

La plataforma manifestó que a futuro le gustaría poder llevar a cabo el desarrollo de los siguientes requerimientos como mejora:

8. Para cada solicitud de compra se requiere llevar a cabo el control de stock disponible. Por ejemplo: Si se solicitan 4 unidades de un producto y solo existen dos, colocar las correspondientes restricciones y avisos.
9. Permitirelusodeun“carritodecompras”endondeporcadasolicituddecompra que haya se acumule un monto total y sea devuelto al usuario. Por ejemplo: Si en una solicitud de compra se envío un producto de $9.000 y luego uno de $3.000 en otra, debo recibir como respuesta la sumatoria de los dos ($12.000). 
   
Al mismo tiempo, sugiere el desarrollo de una nueva API que permita lo siguiente:
10. Dar de alta nuevos clientes. Para esto, se deben realizar los controles necesarios, por ejemplo: cliente ya existente, cliente con datos incompletos, etc.
11. Poder obtener un listado completo de todos los clientes.
12. Poder obtener un listado de todos los clientes filtrados por Provincia.


### Uso de la app

#### Obtener listado de todos los productos:

Method: GET

Path: 

```sh
localhost:8080/api/v1/articles
```

#### Obtener listado de todos productos filtrados por categorìa / precio / nombre / marca / prestigio / envío gratis:

Method: GET

Path:

```sh
localhost:8080/api/v1/articles?category={some_category}&price={some_price}&brand={some_brand}&name={some_name}&prestige={some_rpestige}&freeShipping={true_or_false}
```

Example:

```sh
localhost:8080/api/v1/articles?category=herramientas&brand=makita
```

#### Obtener listado de productos ordenados por algún critero:

Method: GET

Criterio:

```sh
0 -> por nombre ascendente
1 -> por nombre descendente
2 -> por precio descendente
default -> por precio ascendente
```

Path:

```sh
localhost:8080/api/v1/articles?order={some_order}
```

#### Realizar el envío de una solicitud de compra

Method: POST

Path:

```sh
localhost:8080/api/v1/purchase-request
```

Body (ejemplo):

```sh
{
    "articles": [
        {
            "productId": 1,
            "name": "Taladro",
            "brand": "Black & Decker",
            "quantity": 1
        },
        {
            "productId": 2,
            "name": "Soldadora",
            "brand": "Black & Decker",
            "quantity": 4
        }
    ]
}
```

Response (ejemplo):
```sh
{
    "ticket": {
        "id": 3,
        "articles": [
            {
                "productId": 1,
                "name": "Taladro",
                "brand": "Black & Decker",
                "quantity": 1
            },
            {
                "productId": 2,
                "name": "Soldadora",
                "brand": "Black & Decker",
                "quantity": 4
            }
        ],
        "total": 41300.0
    },
    "statusCode": {
        "message": "Operation performed successfully",
        "status": "OK"
    }
}
```

#### Agregar producto a un carrito de compras

Method: POST

Path:

```sh
localhost:8080/api/v1/add-bucket/{bucket_id}
```

Body (ejemplo):

```sh
{
    "productId": 0,
    "name": "Desmalezadora",
    "brand": "Makita",
    "quantity": 2
}
```

Response (ejemplo):

```sh
{
    "id": 1,
    "articles": [
        {
            "productId": 7,
            "name": "Smartwatch",
            "brand": "Noga",
            "quantity": 2
        }
    ],
    "total": 3800.0,
    "statusCodeDTO": {
        "message": "Operation performed successfully",
        "status": "OK"
    }
}
```

#### Realizar la compra de un carrito:

Method: POST

Path:

```sh
localhost:8080/api/v1/purchase-bucket/{bucket_id}
```

Response (ejemplo):

```sh
{
    "id": 1,
    "articles": [
        {
            "productId": 7,
            "name": "Smartwatch",
            "brand": "Noga",
            "quantity": 2
        }
    ],
    "total": 3800.0,
    "statusCodeDTO": {
        "message": "Operation performed successfully",
        "status": "OK"
    }
}
```


#### Dar de alta a un nuevo cliente:

Method: POST

Path:

```sh
localhost:8080/api/v1/customers/new
```

Body (ejemplo):

```sh
{
    "name": "Melisa",
    "lastname": "Onofri",
    "dni": "41526789",
    "province": "Buenos Aires"
}
```

Response (ejemplo):

```sh
{
    "statusCodeDTO": {
        "message": "Operation performed successfully",
        "status": "OK"
    },
    "customer": {
       "customerId": 0,
       "dni": "41526789",
       "name": "Melisa",
       "lastname": "Onofri",
       "province": "Buenos Aires"
   }
}
```

#### Obtener listado de clientes:

Method: GET

Path:

```sh
localhost:8080/api/v1/customers
```

Response (example):

```sh
 {
    "list": [
        {
            "customerId": 0,
            "dni": "41236987",
            "name": "Melisa",
            "lastname": "Onofri",
            "province": "Buenos Aires"
        }
    ],
    "total": 1,
    "statusCodeDTO": {
        "message": "Operation performed successfully",
        "status": "OK"
    }
}
```

#### Obtener listado de clientes filtrados por nombre / provincia:

Method: GET

Path:

```sh
localhost:8080/api/v1/customers?name={some_name}&province={some_province}
```

Response (example):

```sh
 {
    "list": [
        {
            "customerId": 0,
            "dni": "41236987",
            "name": "Melisa",
            "lastname": "Onofri",
            "province": "Buenos Aires"
        }
    ],
    "total": 1,
    "statusCodeDTO": {
        "message": "Operation performed successfully",
        "status": "OK"
    }
}
```

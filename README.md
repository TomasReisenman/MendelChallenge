# MendelChallenge

## Crear la imagen de Docker

```bash
docker build -t mendel-challenge .
```

## Levantar el servidor usando la imagen recien creada

```bash
docker run -p 8080:8080 mendel-challenge
```

## Correr los test dentro de la imagen 

```bash
docker build --target test -t test-image .
```
## Levantar el servidor sin docker

```bash
./mvnw spring-boot:run
```

## Correr los test sin docker

```bash
./mvnw test
```

## Comandos para interactuar con la API
### Crear una transacción (PUT /transactions/{transaction_id})

#### Sin parent:
```bash
curl -X PUT http://localhost:8080/transactions/10 \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 5000,
    "type": "cars"
  }'
```

#### Con parent:

```bash
curl -X PUT http://localhost:8080/transactions/11 \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 10000,
    "type": "cars",
    "parent_id": 10
  }'
```

### Obtener transacciones por tipo (GET /transactions/types/{type})

```bash
curl -X GET http://localhost:8080/transactions/types/cars
```

### Obtener la suma de una transacción (GET /transactions/sum/{transaction_id})

```bash
curl -X GET http://localhost:8080/transactions/sum/10
```

## Decisiones de diseño y arquitectura

### Servidor web
Se utiliza **Apache Tomcat** por simplicidad y porque viene integrado por defecto en Spring Boot.  
En un entorno productivo real se podría evaluar el uso de **Jetty**, que ofrece un modelo más liviano y mayor control en escenarios de alta concurrencia.

### Concurrencia y estructuras de datos
Dado que la aplicación corre sobre un web server multi-threaded, se utilizan **ConcurrentHashMap** como estructura base para el almacenamiento en memoria.  
Esto permite accesos concurrentes seguros sin necesidad de sincronización explícita en la mayoría de los casos 

### Modelo de datos y prevención de ciclos
El modelo de transacciones es **append-only**: una vez creada una transacción, no se modifica ni se elimina.  
Al momento de crear una transacción, se valida de forma atómica que:
- El `transactionId` no exista previamente.
- El `parent_id`, en caso de estar presente, ya exista.

Bajo esta regla, **no es posible crear ciclos**, ya que una transacción nunca puede referenciar a una futura ni a sí misma directa o indirectamente.

### Cálculo de sumas
No se cachea el resultado del `sum` de una transacción.  
En su lugar, se mantiene en memoria una relación **parent → children**, lo que permite:
- Evitar problemas de invalidación de cache.
- Mantener el modelo simple y correcto ante escrituras concurrentes.
- Recalcular el valor de forma determinística recorriendo el grafo de relaciones.

Este enfoque prioriza **correctitud y claridad** por sobre optimizaciones prematuras.

### Búsqueda por tipo
Para la búsqueda por tipo se utiliza un índice en memoria del tipo `Map<String, Set<Long>>`.  
Los sets se inicializan de forma thread-safe únicamente cuando es necesario, evitando condiciones de carrera y estados inconsistentes.

### Seguridad
Por simplicidad, no se agregó seguridad a los endpoints.  
En un entorno real se utilizaría **Spring Security** para proteger los endpoints, manejar autenticación y autorización, y limitar accesos indebidos.

### Testing
En la práctica, se recomienda combinar **tests unitarios** con **tests de integración**.  
En este proyecto se implementaron únicamente **tests de integración**, ya que cubren todos los casos de uso del challenge y mantienen el proyecto más simple.

El test `verify_both_sum_and_type_query_return_correct_result` replica explícitamente el ejemplo provisto al final del enunciado del ejercicio.

## Docker

La aplicación está dockerizada utilizando un enfoque **multi-stage build**:
- Una primera imagen con todas las herramientas necesarias para compilar el proyecto.
- Una segunda imagen mínima que contiene únicamente lo necesario para ejecutar la aplicación.

Esto reduce el tamaño final de la imagen y sigue buenas prácticas de despliegue.

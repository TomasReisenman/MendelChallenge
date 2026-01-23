# MendelChallenge

## Crear la imagen de Docker

``` bash
docker build -t mendel-challenge .
```

## Levantar el servidor usando la imagen recien creada

``` bash
docker run -p 8080:8080 mendel-challenge
```

## Correr los test dentro de la imagen 

```
docker build --target test -t test-image .
```
## Levantar el servidor sin docker

```
./mvnw spring-boot:run
```

## Correr los test sin docker

```
./mvnw test
```

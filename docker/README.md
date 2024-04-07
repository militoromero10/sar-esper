# Crear la imagen docker 
> ejectuar dentro de la carpeta `cep-docker` el siguiente comando `docker build -t cep-sar:1.0.0 .`

# Crear el contenedor que estar escuchando en el puerto 8088
> ejecutar el siguiente comando `docker run -d --rm -p 8088:8088 cep-sar:1.0.0`

# Ejemplo de ejecucion
> Para disparar el Procesador de Eventos complejos, deben hacer la una peticion POST como en el ejemplo

```
curl --location 'localhost:8088/v1/sar/cep' \
--header 'Content-Type: application/json' \
--data '{
    "user": "pepe",
    "project":"marketing 122",
    "budget": 5000000.9,
    "approval": true
}'
```

> - para disparar el evento de `fraud-projects` basta con hacer dos veces la misma peticion pero con diferente nombre `user` y mismo `project`

> - para disparar el evento de `rejected-design` se debe hace la peticion con el mismo usuario 3 veces y con el `approval` siempre en `false`

> - para disparar el evento de `rejected-project` se debe hace la peticion con el mismo usuario 3 veces y con el `approval` siempre en `true` y con `budget` de minimo 4000000



### Usuarios
- april.sanchez -- Analista de marketing
- mauro.zetticci -- externo
- favio.riviera -- Gerente comercial
- william.jobs -- Gerente Financiero
- michael.morrison -- externo 2
- favio.riviera -- Gerente comercial
- april.sanchez -- analista de marketing // daniela.angelo -- departamento de ventas
- norio.yamazaki -- prospecto
- daniela.angelo -- departamento de ventas
- walter.bates
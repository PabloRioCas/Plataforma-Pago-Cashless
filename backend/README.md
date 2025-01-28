# nfconsumer

### Primeros pasos

Lo principal para arrancar el proyecto es tener levantado el contenedor de postgres, para ello, podemos lanzar el siguiente código:

`docker compose -f services.yml up -d psql-server`

Una vez tenemos la db arrancada, podemos interactuar con maven, concretamente la dependencia de hikari,
por que si no petará la integración al no poder comunicarse con la db.
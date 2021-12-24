Actualmente el .jar se encuentra ejecutado en una instancia de oracle, en el puerto 8080.

La IP de esta instancia es 129.151.124.68

Si se desea hacer un consulta general de los registros, se usa el servicio GET /all, el cual retorna la relación de registros actuales.
http://129.151.124.68:8080/api/all

Si se desea validar un ADN para identificar si se trata de un humano un mutante, se usa el servicio POST /mutant.
http://129.151.124.68:8080/api/mutant

Si se desea conocer la relación de mutantes y humanos, se usa el servicio GET /stats
http://129.151.124.68:8080/api/mutant

NOTA: Si se presenta alguna dificultad con la instancia, por favor hacermelo saber para ejecutarla nuevamente.

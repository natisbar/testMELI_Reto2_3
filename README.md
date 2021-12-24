Actualmente el .jar se encuentra ejecutado en una instancia de oracle, en el puerto 8080.

La IP de esta instancia es 129.151.124.68

Si se desea hacer un consulta general de los registros, se usa el servicio GET /all, el cual retorna la relación de registros actuales.
http://129.151.124.68:8080/api/all

Si se desea validar un ADN para identificar si se trata de un humano un mutante, se usa el servicio POST /mutant.
http://129.151.124.68:8080/api/mutant
{
“dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}

Si se desea conocer la relación de mutantes y humanos, se usa el servicio GET /stats
http://129.151.124.68:8080/api/stats

NOTA: 
1. Si se presenta alguna dificultad con la instancia, por favor hacermelo saber para ejecutarla nuevamente.
2. Antes de ejecutar cualquier tipo de prueba masivas, por favor hacer unas pequeñas peticiones antes, de modo que la primera no se pierda (dado que siempre tarda mas que las demas, despues de estar un tiempo sin usarse).
3. Si se desea lograr diferentes combinaciones de dna (mas faciles a la vista), puede usar el excel a continuación, solo es necesario que ajuste las letras de la matriz, y se generará el arreglo en la columna J2. https://docs.google.com/spreadsheets/d/1zsPadZQQ2UeprPl-dq9GTFZQWn25QijQTVwYZyjV9w4/edit?usp=sharing

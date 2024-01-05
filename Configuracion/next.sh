#!/bin/bash

# Obtener la lista de archivos ordenados
archivos_ordenados=$(ls -1 | sort)

# Obtener el archivo actual
archivo_actual=$1

# Encontrar el siguiente archivo
siguiente_archivo=$(echo "$archivos_ordenados" | awk -v current="$archivo_actual" '$0 == current {getline; print}')

# Imprimir el resultado
echo $siguiente_archivo

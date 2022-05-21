#include <unistd.h>


void transferir_datos(int source, int target)
{
	char buf[1024];
	int tam, len;
	
	/*Leer desde el archivo de entrada y escribir el archivo de salida*/
	if((tam = read(source, buf, sizeof(buf))) > 0) {
		if (tam < 0) {
			perror("read");
			exit(1);
		}
		if ((len = write(target, buf, tam)) != tam) {
			perror("write");
			exit(1);
		}
	}	
}

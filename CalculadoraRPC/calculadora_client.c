#include "calculadora.h"
float calculadora_1(char *host, float n1, float n2, char opr, CLIENT *clnt){

	float  *resultado_1;
	operadores  suma_1_arg;
	float  *resultado_2;
	operadores  resta_1_arg;
	float  *resultado_3;
	operadores  multiplicacion_1_arg;
	float  *resultado_4;
	operadores  division_1_arg;
	
	if(opr=='+'){
	
	suma_1_arg.a=n1;
	suma_1_arg.b=n2;
	suma_1_arg.operador=opr;
	
	resultado_1 = suma_1(&suma_1_arg, clnt);
	if(resultado_1 ==(float *) NULL) {
	clnt_perror(clnt, "fallo de llamada");
	}
	return * resultado_1;
	}
	
	else if(opr=='-'){
	
	resta_1_arg.a=n1;
	resta_1_arg.b=n2;
	resta_1_arg.operador=opr;
	
	resultado_2 = resta_1(&resta_1_arg,clnt);
	if (resultado_2 == (float *)NULL){
	clnt_perror(clnt,"fallo de llamada");
	}
	return *resultado_2;
	}
	
	else if(opr=='*'){
	
	multiplicacion_1_arg.a=n1;
	multiplicacion_1_arg.b=n2;
	multiplicacion_1_arg.operador=opr;
	
	resultado_3 = multiplicacion_1(&multiplicacion_1_arg,clnt);
	if (resultado_3 == (float *)NULL){
	clnt_perror(clnt,"fallo de llamada");
	}
	return * resultado_3;
	}
	
	else if(opr=='/'){
	
	division_1_arg.a=n1;
	division_1_arg.b=n2;
	division_1_arg.operador=opr;
	
	if(n2==0){
	printf ("La division por cero no es valida.");
	exit (0);
	}else{
	
	resultado_4 = division_1(&division_1_arg,clnt);
	if (resultado_4 == (float *)NULL){
	clnt_perror(clnt,"fallo de llamada");
	}
	return *resultado_4;
	}
	}
	}
	
int main (int argc, char *argv[])
{
	char *host;
	float a,b;
	char op;
	CLIENT * clnt;	

	if (argc < 2) {
		printf ("uso: %s servidor_host\n", argv[0]);
		exit (1);
	}
	printf("Ingresa el primer Numero: ");
	scanf("%f",&a);
	printf("Ingresa el segundo Numero: ");
	scanf("%f",&b);
	printf("Ingresa el operador: ");
	scanf("%s",&op);	
	
	host = argv[1];
	clnt = clnt_create (host, calculadora, calculadora_version, "udp");
	if (clnt == NULL){
	clnt_pcreateerror(host);
	exit(1);
	}
	printf("La respuesta es =%f", calculadora_1 (host,a,b,op,clnt));
	
	clnt_destroy (clnt);
	exit (0);
	}
	

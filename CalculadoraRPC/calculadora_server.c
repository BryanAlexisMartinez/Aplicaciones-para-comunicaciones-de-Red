#include "calculadora.h"

float *
suma_1_svc(operadores *argp, struct svc_req *rqstp)
{
	static float resultado;
	
	resultado = argp->a+argp->b;
	printf("peticion aceptada: agregando numero %f + %f", argp->a, argp->b);
	printf("enviando respuesta: %f",resultado);
	
	return &resultado;
}

float *
resta_1_svc(operadores *argp, struct svc_req *rqstp)
{
	static float resultado;
	
	resultado = argp->a-argp->b;
	printf("peticion aceptada: agregando numero %f - %f ", argp->a, argp->b);
	printf("enviando respuesta: %f", resultado);
	return &resultado;
}

float *
multiplicacion_1_svc(operadores *argp, struct svc_req *rqstp)
{
	static float resultado;
	
	resultado = argp->a*argp->b;
	printf("peticion aceptada: agregando numero %f * %f ", argp->a, argp->b);
	printf("enviando respuesta: %f", resultado);
	return &resultado;
}

float *
division_1_svc(operadores *argp, struct svc_req *rqstp)
{
	static float resultado;
	
	resultado = argp->a/argp->b;
	printf("peticion aceptada: agregando numero %f / %f ", argp->a, argp->b);
	printf("enviando respuesta: %f",resultado);
	return &resultado;
}

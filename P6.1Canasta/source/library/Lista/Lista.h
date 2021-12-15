typedef struct NodoL{
	Elem dato;
	struct NodoL * siguiente;
} * Lista;

Lista Vacia(){
	return NULL;
}

//SE CONSTRUYE UN NODO
Lista Cons(Elem e, Lista l){ 
	Lista auxiliar = (Lista)malloc(sizeof(struct NodoL));
	auxiliar -> dato = e;
	auxiliar -> siguiente = l;
	return auxiliar;
}
// SE VERIFICA SI LA LISTA ESTA VACIA
int esVacia(Lista l){
	return l == Vacia(); // 0 si es falso, 1 distinto de 0
}
// SE OBTIENE LA CABEZA DE LA LISTA
Elem Cabeza(Lista l){
	return l -> dato;
}
//SE OBTIENE EL RESTO DE LA LISTA
Lista Resto(Lista l){
	return l -> siguiente;	
}
// Cuenta el numero de nodos/elementos de la lista
int NumElem(Lista l){
	if(esVacia(l)){
		return 0;
	}
	else{
		return 1 + NumElem(Resto(l));
	}
}
//SE IMPRIME LA LISTA
void ImpLista(Lista l){
	if(!esVacia(l)){
		ImpElem(Cabeza(l));
		ImpLista(Resto(l));
	}
    else
        printf("\n");
}
//SE UNEN DOS LISTAS
Lista PegaListas(Lista x, Lista y){
	if(esVacia(x)){
		return y;
	}
	else{
		return Cons(Cabeza(x),PegaListas(Resto(x),y));
	}
}
// SE INVIERTE UNA LISTA
Lista InvierteLista(Lista l){
	if(esVacia(l)){
		return l;
	}
	else{
		return PegaListas(InvierteLista(Resto(l)),Cons(Cabeza(l),Vacia()));
	}
}
//INSERTA EN ORDEN UN ELEMENTO A LA LISTA
Lista InsOrd(Elem e, Lista l){
	if(esVacia(l)){
		return Cons(e,l);
	}
	else if(esMenor(e,Cabeza(l))){
		return Cons(e,l);
	}
	else{
		return Cons(Cabeza(l),InsOrd(e,Resto(l)));
	}
}
//SE ORDENA LA LISTA DE MANERA ASCENDENTE
Lista OrdLista(Lista l){
	if(esVacia(l)){
		return l;
	}
	else{
		return InsOrd(Cabeza(l),OrdLista(Resto(l)));
	}
}
// Funcion que ordena una lista de manera descendente
Lista OrdListaDes(Lista l){
	return InvierteLista(OrdLista(l));
}
/* SE DEFINE UNA FUNCION QUE RECIBE COMO ARGUMENTO UN ELEMENTO Y UNA LISTA, DEVIELVE VERDADERO SI EL ELEMENTO ESTA EN LA LISTA, UN FALSO EN CASO CONTRARIO*/
int estaEn(Elem e, Lista l){
	if(esVacia(l)){
		return 0;
	}
	else if(sonIguales(e,Cabeza(l))){
		return 1;
	}
	else{
		return estaEn(e,Resto(l));
	}
}
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;
import java.util.*;


public class Arreglo {
	//variables comunes y necesarias para todas las funciones
	private int MAX=100;
	private int[] datos;
	private int tamano;
	private int[] pq;//cola de prioridad para heapsort
	private int t; //tamano de la cola de prioridad
	
	//constructor
	Arreglo(){
		datos=new int[MAX];
		pq=new int[MAX+1];//se declara con un elemento mas
		tamano=0;
		t=0;//posicion donde va el ultimo elemento del heap hasta ese momento
		pq[0]=0; //aunque no lo usemos, debemos darle un valor para evitar errores en insert
	}
	
	
	public void lecturaDatos(String archivo) {
		//System.out.println("El nombre del archivo es: "+archivo);
		
		File file = new File(archivo);
		//hacemos el try-cast pues puede ser que no exista el archivo o que su contenido sea diferente
		try {
			//abrimos el escaner
	        Scanner sc = new Scanner(file);
	        tamano=sc.nextInt();
	        //pedimos elementos del arreglo
	        for(int i=0; i<tamano; i++) {
				datos[i]=sc.nextInt();
			}
	        //cerramos el escaner
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }         
	}
	
	//funcion que imprime los arreglos de uno por uno
	public void imprimeArreglo() {
		System.out.println("El arreglo tiene "+tamano+" elementos, que son:");
		for(int i=0; i<tamano; i++) {
			System.out.print(datos[i]+" ");
		}
		System.out.println();
	}
	
	void insertar(int k){
		pq[t+1]=k;
		int index=t+1; //index donde se encuentra el numero k
		while(pq[index]>pq[index/2]) {
			if(index==1) break;//si ya esta en la raiz, no se hace nada
			//se ejecuta el algoritmo que cambia de lugares
			int aux=pq[index];
			pq[index]=pq[index/2];
			pq[index/2]=aux;
			index=index/2;
		}
		t++; //aumentamos en 1 el tamano del heap
		
//		System.out.print("\nInsertando elemento: ");
//		for(int i=0; i<=t; i++) {
//			System.out.print(pq[i]+" ");
//		}
//		System.out.println();
	}
	int remover() {
		int aux;
		int ans=pq[1];
		pq[1]=pq[t];//cambiamos raiz por el elemento final del heap
		t--; //decrementamos tamano del heap
		int index=1;
		boolean ordenado=false;//haremos sink hasta que este ordenado
		while(!ordenado) {
			//analizamos los casos en los que el nodo no tenga hijos, o solo tenga uno
			if(2*index>t) ordenado=true;
			else if(2*index==t) {
				if(pq[index]>pq[2*index]) ordenado=true;
				else {
					aux=pq[index];
					pq[index]=pq[2*index];
					pq[2*index]=aux;
					ordenado=true;
				}
			}else {//este es el caso en el que el nodo tiene a sus dos hijos, y se sigue el algoritmo sink
				if(pq[index]>pq[2*index]&&pq[index]>pq[2*index+1]) ordenado=true;
				else {
					int iaux;
					if(pq[2*index]>pq[2*index+1]) {
						iaux=2*index;
					} else iaux=2*index+1;
					aux=pq[index];
					pq[index]=pq[iaux];
					pq[iaux]=aux;
					index=iaux;
				}
			}
		}
		//se regresa el valor inicial que quitamos
		return ans;
	}
	
	public void heapSort() {
		//heapsort solo es una mezcla de insertar y remover
		System.out.println("\nOrdenando arreglo con Heap Sort...");
		for(int i=0; i<tamano; i++) {
			//System.out.print("Se inserta el "+datos[i]+": ");
			insertar(datos[i]);
		}
		for(int i=0; i<tamano; i++) {
			datos[tamano-i-1]=remover();
		}
		
	}
	
	
	public void radixSort() {
		System.out.println("\nOrdenando arreglo con Radix Sort...");
		//creamos un arreglo de listas ligadas 
		LinkedList<Integer>[] listas=new LinkedList[10];
		//inicializamos arreglo
		for(int i=0; i<10; i++) {
			if(listas[i]==null) listas[i]=new LinkedList<Integer>();
		}
		
		//definimos al mayor de los elementos del arreglo y a su cantidad de digitos
		int mayor=0;
		int numdigitos=0;
		
		//encontramos el elemento mayor
		for(int i=0; i<tamano; i++) {
			if(mayor<datos[i]) mayor=datos[i];
		}
		
		//contamos la cantidad de digitos del mayor
		//el metodo solo funciona para enteros no negativos
		while(mayor>0) {
			numdigitos++;
			mayor/=10;		
		}
		
		int divisor=1;//queremos obtener datos[i]%divisor, que nos indica el digito para ubicar la lista ligada donde va el numero
		for(int i=0; i<numdigitos; i++) {
			//ingresamos los numeros a la lista ligada que le corresponde
			for(int j=0; j<tamano; j++) {
				listas[(datos[j]/divisor)%10].add(datos[j]);
			}
			
			int cont=0; //es el index que pone los numeros de regreso en el arreglo
			//ponemos a los numeros de nuevo en el arreglo
			for(int j=0; j<10; j++) {
				while(listas[j].size()>0) {
					datos[cont]=listas[j].getFirst();
					listas[j].removeFirst();
					cont++;
				}
			}
			divisor*=10;
		}
		
	}
	
	

//	
//	public static void main(String[] args) {
//		Arreglo ar=new Arreglo();
//		
//		
//		ar.lecturaDatos("prueba.txt");
//		ar.imprimeArreglo();
//		ar.heapSort();
//		ar.imprimeArreglo();
//		
//		System.out.println();
//		ar.lecturaDatos("otraprueba.txt");
//		ar.imprimeArreglo();
//		ar.radixSort();
//		ar.imprimeArreglo();
//		
//	}

	
	
}

//Karol Gutierrez A01024536
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class TSP {
	static int N;//tamaño del grafo
	static ArrayList<Integer> v=new ArrayList<Integer>();//aqui se guardan las permutaciones
	static boolean[] chosen;//este arreglo booleano nos ayud a mantener control de los elementos que llevamos elegidos para las permutaciones
	static float[] xs;//arreglo con coordenadas x
	static float[] ys;//arreglo con coordenadas y
	static ArrayList<Integer> ans=new ArrayList<Integer>();//aqui almacenamos la mejor de las permutaciones hasta el momento en el algoritmo de fuerza bruta
	static float minsum;//suma minima
	static float currentSum;
	static ArrayList<Integer> minimo=new ArrayList<Integer>();//arreglo con la respuesta final

	
	static void leerDatos(String archivo) {
		File file = new File(archivo);
		//hacemos el try-cast pues puede ser que no exista el archivo o que su contenido sea diferente
		try {
			//abrimos el escaner
	        Scanner sc = new Scanner(file);
	        //pedimos variables iniciales
	        N=sc.nextInt();
	        xs=new float[N];//declaramos los arreglos
    		ys=new float[N];
	        for(int i=0; i<N; i++) {//llenamos los arreglos
	        	float r, s;
	        	r=sc.nextFloat();
	        	s=sc.nextFloat();
	        	xs[i]=(float)r;
	        	ys[i]=(float)s;
	        }
	        //cerramos el escaner
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	static float dist(int i, int j) {//funcion para calcuylar distancia
		return (float)Math.sqrt( (xs[i]-xs[j])*(xs[i]-xs[j]) + (ys[i]-ys[j])*(ys[i]-ys[j]) );
	}
	
	static float sum() {//usa como parametro al arreglo v que tiene size==N-1 y calcula la suma del recorrido
		float ans=0;
		ans+=dist(0,v.get(0));
		for(int i=0;i<N-2;i++) {
			ans+=dist(v.get(i), v.get(i+1));
		}
		ans+=dist(v.get(N-2), 0);
		return ans;	
	}
	
	static void search(int N) {//funcion recursiva que genera todas las iteraciones
		if(v.size()==N-1) {
			currentSum=sum();
			if(currentSum<minsum) {
				minsum=currentSum;
				ans= (ArrayList<Integer>) v.clone();			
			}	
		} else {
			for(Integer i=1; i<N; i++) {//crea el arbol con todas las permutaciones y se llama a si misma
				if(chosen[i]) continue;
				chosen[i]=true;
				v.add(i);
				search(N);
				chosen[i]=false;
				v.remove(v.size()-1);
			}
		}
        }
        
	public static void fuerzaBruta(String archivo) {//todo el algoritmo
            minsum=Float.MAX_VALUE;//se inicializa el costo minimo
		long startTime=System.currentTimeMillis();//inicio de ejecucion
		leerDatos(archivo);
		chosen=new boolean[N];//al inicio todos estan en false
		search(N); //despues de esto, ans contiene la mejor permutacion
		minimo.add(0);
		minimo.addAll(ans);
                minimo.add(0);
		System.out.println("El orden en el que se deben visitar los nodos es: " + minimo);
		System.out.println("La suma minima es " + minsum);
		
		long endTime=System.currentTimeMillis();//fin de ejecucion
		long totalTime=endTime-startTime;
		System.out.println("Tiempo de ejecucion: "+totalTime+" milisegundos");
	}
	
	static int numerodeunos(int n) {//funcion que nos dice el numero de unos en la representacion binaria de un numero
		int count = 0;
        while (n > 0)
        {
            count += n & 1;
            n >>= 1;
        }
        return count;
	}
        static boolean tieneuno(int n, int pos){//funcion que nos dice si un numero tiene un uno en su representacion bianria en determinada posicion
            
            if((n/(1<<pos))%2==1) return true;
            return false;
        }
        static int sineluno(int n, int pos){//funcion que quita un uno de determinada posicion en la representacion binaria de un numero
            return n-(1<<pos);
        }
        public static class estado{//clase que contiene el costo y el papa de un nodo para un determinado subconjunto y nodo final
            float costo;
            short papa;
            public estado(){
                costo=Float.MAX_VALUE;
                papa=-1;
            }
        }
	public static void DP(String archivo) {
		long startTime=System.currentTimeMillis();//inicio de ejecucion
		leerDatos(archivo);
                
                estado f[][]=new estado[1<<(N-1)][N-1];//arreglo para hacer programacion dinamica, la primera entrada es un numero cuya representacion en binario muestra el subconjunto
                
                for(int i=0; i<N-1; i++){//caso base
                    f[0][i]=new estado();
                    f[0][i].papa=-1;
                    f[0][i].costo=dist(0, i+1);
                } 
                for(int i=1; i<1<<(N-1); i++){ //llamada con programacion dinamica                    
                    for(int j=0; j<N-1; j++){           
                       if(!tieneuno(i, j)){//si j no esta en el subconjunto
                           f[i][j]=new estado();
                           float mini=Float.MAX_VALUE;
                           short p=-1;
                           for(int k=0; k<N-1; k++){//para todo k...
                               
                               if(tieneuno(i, k)){//... que sí está en el subconjunto
                                   float temp=f[sineluno(i, k)][k].costo+dist(j+1, k+1);//j+1 y k+1 son las posiciones en el arreglo xs y ys, se usa de esta forma porque se toma al vertice cero como -1
                                   if(mini>temp){//mantenemos al menor
                                       mini=temp;
                                       p=(short)k;
                                   }
                               }
                           }
                           f[i][j].costo=mini;//actualizamos al papa y al costo
                           f[i][j].papa=p;
                       }
                    }
                }
                //paso final: conectar el ultimo nodo con cero
                float minans=Float.MAX_VALUE;
                short anterior=-1;
                
                int i=(1<<N-1)-1;
                for(int pos=0; pos<N-1; pos++){
                    if(tieneuno(i, pos)){//elegimos el que nos genere una suma final menor
                        float temp=f[sineluno(i, pos)][pos].costo+dist(0, pos+1);
                        if(minans>temp){
                            minans=temp;
                            anterior=(short)pos;
                        }
                    }
                }
                System.out.println("La respuesta es: "+minans);
                
                System.out.print("El orden de los nodos a seguir es: [ 0 "+(anterior+1)+" ");
                //para imprimir el recorrido...
                i=(1<<N-1)-1;
                i=sineluno(i, anterior);
                //avanzamos hacia atrás obteniendo los papás de los estados, hasta llegar al estado tal que su pap+a es cero
                while(anterior!=-1){
                    short previo=f[i][anterior].papa;
                    i=sineluno(i, previo);
                    anterior=previo;
                    System.out.print((anterior+1)+" ");
                }
                
                System.out.print("]\n");
                long endTime=System.currentTimeMillis();//fin de ejecucion
		long totalTime=endTime-startTime;
		System.out.println("Tiempo de ejecucion: "+totalTime+" milisegundos");
	}
}
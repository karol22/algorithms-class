import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;
import java.util.*;


public class MST{
	
	public static class arista implements Comparable<arista> {
		public int w;//peso
	    public int x;//primer nodo
	    public int y;//segundo nodo
	    
	    public arista(int a, int b, int c) {
	        x=a;
	        y=b;
	        w=c;
	    }
	    public int compareTo(arista a) {//comparador necesario
	        return this.w-a.w;
	    }
	    public void imprime() {//funcion para imprimir aristas
	    	System.out.println("("+x+" "+y+" "+w+")");
	    }
	}
	
	
	public class Grafo{
		LinkedList<arista>G[];//un grafo es una matriz de listas ligadas de aristas
		
		public Grafo() {
			
		}
		public void setGrafo(int n) {//crea grafo
			G=new LinkedList[n];
			for(int i = 0;i < G.length;i++) {
				G[i]=new LinkedList<arista>(); 
			}
		}
		
		void addarista(int x,int y,int z) {//añade aristas dos veces
			G[x].add(new arista(x, y, z));
			G[y].add(new arista(y, x, z));
		}	
		void imprime() {//imprime el grafo
			for(int i = 0;i < G.length;i++) {
				for(int j=0; j<G[i].size(); j++) {
					System.out.print(G[i].get(j).x+" "+G[i].get(j).y+" "+G[i].get(j).w+" -> ");
				}
				System.out.println();
			}
		}
		int getSize(int n) {//regresa el tamaño de un determinado renglon
			return G[n].size();
		}
		arista getarista(int r, int c) {//regresa una arista dado un renglon y una columna
			return G[r].get(c);
		}
		
	}
	Grafo g=new Grafo();
	
	
	// Prim  
	public float prim(String archivo) {
		long startTime=System.currentTimeMillis();//inicio de ejecucion
		g.setGrafo(0);
		int i;
		int n=0, m=0;
		File file = new File(archivo);
		//hacemos el try-cast pues puede ser que no exista el archivo o que su contenido sea diferente
		try {
			//abrimos el escaner
	        Scanner sc = new Scanner(file);
	        //pedimos variables iniciales
	        n=sc.nextInt();
	        m=sc.nextInt();
	        g.setGrafo(n+1);//asigna tamaño del grafo (numero de renglones)
	        for(i=0; i<m; i++) {
	        	int r, s, t;
	        	r=sc.nextInt();
	        	s=sc.nextInt();
	        	t=sc.nextInt();
	        	g.addarista(r,  s,  t);//llena el grafo
	        }
	        //cerramos el escaner
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
		
		boolean[] agregados= new boolean[n+1];
		int tam=1;//se agrega el nodo 1
		agregados[1]=true;
		float costo=0;
		System.out.println("Las aristas son: ");
		
		PriorityQueue<arista> pq =new PriorityQueue<arista>();//heap
		//iniciamos en el vertice 1, por lo tanto agregamos a todas las aristas que tengan al 1
		for(i=0; i<g.getSize(1); i++) {
			pq.add(g.getarista(1, i));
		}
		//mientras haya menos de n vertices 
		while (tam<n) {
			int n1=pq.peek().x;
			int n2=pq.peek().y;
			
			while (agregados[n1]&&agregados[n2]) {//quitamos aristas mientras esten compuestas por nodos ya utilizados
				pq.remove();
				n1=pq.peek().x;
				n2=pq.peek().y;
			}
			int c=pq.peek().w;
			pq.peek().imprime();//imprimimos las aristas que vamos usando
			costo+=c;
			tam++;
			
			int nn=n1;
			if(agregados[n1]) nn=n2;
			agregados[nn]=true;//se agrego un nuevo vertice, el cual es nn
			pq.remove(); //quitamos el primer elemento de la pq
			
			for(i=0; i<g.getSize(nn); i++) {//agregamos a los hijos del elemento quitado
				if( !(agregados[g.getarista(nn, i).x] &&  agregados[g.getarista(nn, i).y]) ) {
					pq.add(g.getarista(nn, i));
				}
			}
		}
		long endTime=System.currentTimeMillis();//fin de ejecucion
		long totalTime=endTime-startTime;
		System.out.println("Tiempo de ejecucion: "+totalTime+" milisegundos");
		return costo;
	}
	
	
	// Kruskal DFS
	public float kruskalDFS(String archivo) {
		long startTime=System.currentTimeMillis();//inicio de ejecucion
		g.setGrafo(0);//inicializamos al grafo
		int n=0, m=0;
		PriorityQueue<arista> pq =new PriorityQueue<arista>();//heap
		
		File file = new File(archivo);
		//hacemos el try-cast pues puede ser que no exista el archivo o que su contenido sea diferente
		try {
			//abrimos el escaner
	        Scanner sc = new Scanner(file);
	        //pedimos variables iniciales
	        n=sc.nextInt();
	        m=sc.nextInt();
	        g.setGrafo(n+1);//asigna tamaño del grafo (numero de renglones)
	        for(int i=0; i<m; i++) {
	        	int r, s, t;
	        	r=sc.nextInt();
	        	s=sc.nextInt();
	        	t=sc.nextInt();
	        	pq.add(new arista(r, s, t));//lenamos el heap con las aristas en orden 
	        }
	        //cerramos el escaner
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }

		boolean[] agregados=new boolean[n+1];//almacena los vertices ya usados
		float costo=0;
		int n1, n2, c;
		int tam=0; //es la cantidad de aristas en el MST
		
		//agregamos la primer arista
		tam=1; 
		n1=pq.peek().x;
		n2=pq.peek().y;
		costo=pq.peek().w;
		agregados[n1]=true;//agregamos sus vertices
		agregados[n2]=true;
		g.addarista(pq.peek().x, pq.peek().y, pq.peek().w);//se agrega la arista al arbol
		System.out.println("Las aristas son: ");
		pq.remove().imprime();
	
		
		while(tam<n-1) {//continua mientras haya menos de n-1 aristas
			n1=pq.peek().x;
			n2=pq.peek().y;
			c=pq.peek().w;
			if(agregados[n1]&&agregados[n2]) {
				//se aplica DFS usando una pila para saber si un vertice es alcanzable desde otro vertice y asi saber si hay ciclos
				boolean reachedn2=false;
				
				boolean[] visited=new boolean[n+1];
				Stack<arista> pila=new Stack<arista>();
				//se agregan los primeros elementos a la pila
				for(int i=0; i<g.getSize(n1); i++) {
					pila.push(g.getarista(n1, i));
				}
				//mientras la pila no esta vacia, repite el proceso
				while(!pila.empty()||reachedn2) {
					visited[pila.peek().x]=true;//marca los vertices donde se visitaron todos su hijos
					int k=pila.peek().y;
					pila.pop();
					if(k==n2) {
						reachedn2=true;//variable por si encuentra al segundo vertice (caso en el que se forma un ciclo al agregar la nueva arista)
						break;
					}else {
						if(g.getSize(k)==0) {//tambien se marcan como visitados cuando no tienen hijos
							visited[k]=true;
						}
						for(int i=0; i<g.getSize(k); i++) {
							if(!visited[g.getarista(k, i).y]) {//agrega los hijos de los elementos visitados
								pila.push(g.getarista(k, i));
							}
						}
					}
				}
				
				if(reachedn2) {//caso en el que hubo ciclo, se ignora
					pq.remove();
				}else {//caso en el que no se forma ciclo: se agrega la nueva arista
					costo+=c;
					g.addarista(pq.peek().x, pq.peek().y, pq.peek().w);
					pq.remove().imprime();
					tam++;
				}
				
			}else {
				//caso en el que es imposible que haya ciclos pues los vertices ni siquiera han sido marcados
				agregados[n1]=true;
				agregados[n2]=true;
				costo+=c;
				g.addarista(pq.peek().x, pq.peek().y, pq.peek().w);
				pq.remove().imprime();
				tam++;
			}
		}
		
		long endTime=System.currentTimeMillis();//fin de ejecucion
		long totalTime=endTime-startTime;
		System.out.println("Tiempo de ejecucion: "+totalTime+" milisegundos");
		return costo;
	}
	
	//KruskalUF
	static int[] lider;//arreglos globales en la funcion
	static int[] numUF;
	public static int find(int p) {//regresa el lider de cada nodo
		while(p!=lider[p]) {
			p=lider[p];
		}
		return p;
	}
	
	public static void union(int p, int q) {
		if(numUF[p]>numUF[q]) {//une y asigna nuevos lideres
			numUF[p]+=numUF[q];
			lider[q]=p;
		}else {
			numUF[q]+=numUF[p];
			lider[p]=q;
		}
	}
	
	public float kruskalUF(String archivo) {
		long startTime=System.currentTimeMillis();//inicio de ejecucion
		int p, q;
		int n=0, m=0;
		PriorityQueue<arista> pq =new PriorityQueue<arista>();//heap
		File file = new File(archivo);
		//hacemos el try-cast pues puede ser que no exista el archivo o que su contenido sea diferente
		try {
			//abrimos el escaner
	        Scanner sc = new Scanner(file);
	        //pedimos variables iniciales
	        n=sc.nextInt();
	        m=sc.nextInt();
	        g.setGrafo(n+1);//asigna tamaño del grafo (numero de renglones)
	        for(int i=0; i<m; i++) {
	        	int r, s, t;
	        	r=sc.nextInt();
	        	s=sc.nextInt();
	        	t=sc.nextInt();
	        	pq.add(new arista(r, s, t));//lenamos el heap con las aristas en orden 
	        }
	        //cerramos el escaner
	        sc.close();
	    } 
		
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		
		lider=new int[n+1];//se inicializan con un tamano correspondiente al numero de nodos
		numUF=new int[n+1];
		
		for(int i=1; i<=n; i++) {//lideres distintos al inicio
			lider[i]=i;
			numUF[i]=1;
		}
		LinkedList<arista> mst=new LinkedList<arista>();
		int tam=0;//cantidad de aristas
		float costo=0;
		System.out.println("Las aristas son: ");
		while(tam<n-1) {//el algoritmo termina cuando el numero de aristas sea n-1
			p=find(pq.peek().x);
			q=find(pq.peek().y);
			if(p!=q) {
				tam++;
				union(p, q);
				costo+=pq.peek().w;
				pq.peek().imprime();
				mst.add(pq.peek());
			}
			pq.remove();
		}
	
		long endTime=System.currentTimeMillis();//fin de ejecucion
		long totalTime=endTime-startTime;
		System.out.println("Tiempo de ejecucion: "+totalTime+" milisegundos");
		return costo;
	}

	
	public static void main(String[] args) {
		MST a=new MST();
		System.out.println(a.prim("arbol.txt"));
		System.out.println(a.kruskalDFS("arbol.txt"));
		System.out.println(a.kruskalUF("arbol.txt"));
	}
	
}

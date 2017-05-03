import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class Main {

  private static ArrayList<Integer> refSequence = new ArrayList<Integer>();

  public static void fifo(int numFrames) {
    int pageMiss = 0;
    int i = 0;
    ArrayList<Integer> frames = new ArrayList<Integer>(); //quadros disponiveis na memoria
    for (int j = 0; j < numFrames; j++) {frames.add(-1);}

    for (Integer ref : refSequence) {
      if (!frames.contains(ref)) { // Falta de pagina!!
        frames.set(i, ref);        // Adiciona o elemento no indice correto
        i = (i+1) % frames.size(); // Atualiza Circular
        pageMiss++;                // Atualiza contador de falta de paginas
        //System.out.println(ref+" -> "+frames);
      }
    }

    System.out.println("FIFO " + pageMiss);
  }

  public static void otm(int numFrames) {
    int pageMiss = 0;
    int i = 0;
    int firstRefCount = numFrames;
    int distance[] = new int[numFrames];
    int target = 0;
    int index = 0;
    ArrayList<Integer> frames = new ArrayList<Integer>(); //quadros disponiveis na memoria
    for (int j = 0; j < numFrames; j++) {frames.add(-1);}

    for (Integer ref : refSequence) {
      if (!frames.contains(ref)) { // Falta de pagina!!
        pageMiss++;

        if (firstRefCount != 0) {   // Primeira referencia
          frames.set(i, ref);
          firstRefCount--;
        }
        else { // Verifica qual quadro vitima
          target = 0;
          distance = new int[numFrames];
          for (Integer f : frames) {
            for (int k = i; k < refSequence.size(); k++) {
              if (refSequence.get(k) == f){
                break;
              }
              distance[target]++;
            }
            target++;
          }
          // Verifica maior distancia
          target = -1;
          index = 0;
          for (int k = 0; k < numFrames; k++) {
            if (distance[k] > target){
              target = distance[k];
              index = k;
            }
          }
          frames.set(index, ref);
          //System.out.println(ref+" -> "+frames);
        }
      }
      i++;
    }
    System.out.println("OTM " + pageMiss);
  }

  public static void lru(int numFrames){
    int pageMiss = 0;
    int i = 0;
    int firstRefCount = numFrames;
    int systemTime[] = new int[numFrames];
    int countTime = 0;
    ArrayList<Integer> frames = new ArrayList<Integer>();
    for (int j = 0; j < numFrames; j++) {frames.add(-1);}

    for (Integer ref : refSequence) {

      if (!frames.contains(ref)) { // Falta de pagina!!
        pageMiss++;
        if (i < numFrames) {
          frames.set(i, ref);
          systemTime[i] = i;
        }
        else {
          //Busca o menor indice do contador
          int less = Integer.MAX_VALUE;
          int index = 0;
          for (int k = 0; k < numFrames; k++) {
            if (systemTime[k] < less) {
              less = systemTime[k];
              index = k;
            }
          }
          systemTime[index] = i;
          frames.set(index, ref);
        }
        //System.out.println(ref+" -> "+frames);
      }
      else { //nao ocorreu falta de pagina
        //busca indice da referencia e atualiza contador de ref
        for (int k = 0; k < numFrames; k++) {
          if (frames.get(k) == ref){
            systemTime[k] = i;
            break;
          }
        }
      }
      i++;
    }
    System.out.println("LRU " + pageMiss);
  }

  public static void main(String[] args) {

     try {
       String filePath = "../assets/in.txt";
       String line = "";

       BufferedReader readFile =
	                  new BufferedReader(new FileReader(filePath));

       int numFrames = Integer.parseInt(readFile.readLine());

       while ((line = readFile.readLine()) != null) {
         refSequence.add(Integer.parseInt(line));
       }

      fifo(numFrames);
      otm(numFrames);
      lru(numFrames);

     }
     catch (Exception e) {
       e.printStackTrace();
     }

  }
}

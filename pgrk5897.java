//Qiong Qin 5897
import java.io.*;
import java.util.*;
import java.lang.Math;


public class pgrk5897 {
    public static double d = 0.85;
    public static double bored = 0.15;

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Please follow the right input order: iterations initialvalue filename ");
            System.exit(0);
        }

        // validate and initialize iterations and errorate
        int iterations = Integer.parseInt(args[0]);
        double errorate = 0.0;
        if (iterations < 0) {
            errorate = Math.pow(10, iterations);
        }

        //validate and initialize initial value
        int initial_Value = Integer.parseInt(args[1]);

        //create and initialize values of vertices and edges
        String sample_file = args[2];
        Scanner obj = new Scanner(new File(sample_file));
        int vertices = obj.nextInt();
        int edges = obj.nextInt();

        if (vertices > 10) {
            iterations = 0;
            initial_Value = -1;
            errorate = 0.00001;  //default value
        }
        //create and initialize adjenct matrix by 2d arrays
        double graph[][] = new double[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                graph[i][j] = 0.0;
            }
        }
        //connect vertice and edges
        while (obj.hasNextInt()) {
            int row = obj.nextInt();
            int col = obj.nextInt();
            graph[row][col] = 1.0;
        }

        double[] pre_pgrk = new double[vertices];
        for (int i = 0; i < vertices; i++) {
            pre_pgrk[i] = 0.0;
        }
        double[] cur_pgrk = new double[vertices];
        for (int i = 0; i < vertices; i++) {
            cur_pgrk[i] = 0.0;
        }

        intitiaze_pgrk(pre_pgrk, initial_Value, vertices);


        double[] out = new double[vertices];
        for (int i = 0; i < vertices; i++) {
            out[i] = 0.0;
        }

        for (int i = 0; i < vertices; i++)
            for (int j = 0; j < vertices; j++) {
                out[i] += graph[i][j];
            }

        if (iterations >0||vertices<=10) {
            System.out.print("Base : " + " 0 :");
            for(int i =0;i<pre_pgrk.length;i++){
                System.out.print("P[ " + i +"]=" + String.format("%.7f", pre_pgrk[i]) +" ");
            }
            System.out.println();

            iterations(pre_pgrk, cur_pgrk, graph, out, iterations, vertices,errorate);
        } else {

            check_errorbound(pre_pgrk, cur_pgrk, graph,out,errorate,vertices);
        }

    }


    public static void intitiaze_pgrk(double[] pre_pgrk, int initial_Value, int vertices) {
        //int size = authority[0].length;

        if (vertices < 10) {
            if (initial_Value == 0) {
                for (int i = 0; i < vertices; i++) {
                    pre_pgrk[i] = 0.0;
                }

            } else if (initial_Value == 1) {
                for (int i = 0; i < vertices; i++) {
                    pre_pgrk[i] = 1.0;
                }

            } else if (initial_Value == -1) {

                for (int i = 0; i < vertices; i++) {
                    pre_pgrk[i] = 1.0 / vertices;
                }
            } else if (initial_Value == -2) {
                for (int i = 0; i < vertices; i++) {
                    pre_pgrk[i] = 1.0 / Math.sqrt(vertices);
                }
            }
        } else {
            for (int i = 0; i < vertices; i++) {
                pre_pgrk[i] = 1.0 / vertices;
            }

        }


    }

    public static void iterations(double[] pre, double[] cur, double[][] graph, double[] out, int iterations, int vertices,double errorate) {

        int count = 1;
        boolean flag = false;
        while (flag==false){

            for (int i = 0; i < cur.length; i++) {
                cur[i] = 0.0;
            }
            for (int i = 0; i < cur.length; i++) {
                double temp = 0.0;
                for (int j = 0; j < graph.length; j++) {
                    if (graph[j][i] == 1) {
                        temp += (pre[j] / out[j]);
                    }
                }
                cur[i] += (d * temp)+(bored / vertices);
            }

            if (count<10) System.out.print("Iter :  "+(count)+" :");
            else System.out.print("Iter : "+(count)+" :");
            flag = helper(pre, cur, errorate);

            for (int i = 0; i < graph.length; i++) {
               System.out.print("P[ " + i +"]=" + String.format("%.7f", cur[i]) +" ");
                pre[i] = cur[i];
            }
            System.out.println();
            count += 1;
            if(count==iterations+1) System.exit(0);

        }

    }

    public static void check_errorbound(double[] pre, double[] cur, double[][] graph, double[] out, double errorate, int vertices) {
        int count =1;
        boolean flag = false;
        while (flag == false) {

            for (int i = 0; i < vertices; i++) {
                cur[i] = 0.0;
            }

            for (int i = 0; i < cur.length; i++) {
                double temp = 0.0;
                for (int j = 0; j < graph.length; j++) {
                    if (graph[j][i] == 1) {
                        temp += (pre[j] / out[j]);
                    }
                }
                cur[i] += (d * temp)+(bored / vertices);
            }

            flag = helper(pre, cur, errorate);
            System.out.println("Iter :"+(count));
            for (int i = 0; i < graph.length; i++) {
                if (i<10)
                    System.out.print("P[ " + i +"]=" + String.format("%.7f", cur[i]) +" ");
                else
                    System.out.print("P["+ i +"]=" + String.format("%.7f", cur[i]) +" ");

                System.out.println();
                pre[i] = cur[i];
            }
            count++;
        }
    }

    public static boolean helper(double[] pre, double[]cur, double errorate) {
        for (int i = 0; i < pre.length; i++) {
            if (Math.abs(pre[i] - cur[i]) > errorate) {
                return false;
            }
        }
        return true;
    }
}
//Qiong Qin 5897
import java.io.*;
import java.util.*;
import java.lang.Math;


public class hits5897 {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Please follow the right input order: iterations initialvalue filename ");
            System.exit(0);
        }

        // validate and initialize iterations and errorate
        int iterations = Integer.parseInt(args[0]);
        double errorate = 0.00001;
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

        double[][] ini_authority = new double[vertices][1];//[[0.0], [0.0], [0.0], [0.0]]
        double[][] ini_hub = new double[vertices][1];


        if (vertices > 10) {
            iterations = 0;
            initial_Value = -1;
            errorate = 0.00001;  //default value
        }

        hub_athority(ini_authority, ini_hub, initial_Value, vertices);

        int row = graph.length;
        int col = graph[0].length;
        double[][] transpose = new double[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                transpose[i][j] = graph[j][i];
            }
        }
        //calculate authority and hub, a = L'*h,h = L*a
        double[][] pre_authority = new double[vertices][1];
        double[][] pre_hub = new double[vertices][1];
        double[][] authority = new double[vertices][1];
        double[][] hub = new double[vertices][1];

        double scale_authority = 0.0;
        double scale_hub = 0.0;

        double temp = 0.0;
        for (int i = 0; i < transpose.length; i++) {
            for (int j = 0; j < ini_hub[0].length; j++)
                {
                for (int k = 0; k < transpose[0].length; k++) {
                    temp += transpose[i][k] * ini_hub[k][j];
                }
                authority[i][j] = temp;
                temp = 0.0;
                scale_authority += authority[i][j] * authority[i][j];
            }
        }

        temp = 0.0;

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < authority[0].length; j++) {
                for (int k = 0; k < graph[0].length; k++) {
                    temp += graph[i][k] * authority[k][j];
                }
                hub[i][j] = temp;
                temp = 0.0;
                scale_hub += hub[i][j] * hub[i][j];
            }
        }

        if (iterations > 0) {
            System.out.print("Base :  " + 0 + " :");
            for (int i = 0; i < authority.length; i++) {
                for (int j = 0; j < 1; j++) {
                    System.out.print("A/H[ " + i + "]=" + String.format("%.7f", ini_authority[i][0]) + "/" + String.format("%.7f", ini_hub[i][0]) + " ");
                }
            }
            System.out.println();
            iteration(authority, hub, graph, transpose, scale_authority, scale_hub, iterations);
        }
        else {
            System.out.println("Base   :" + 0);
            for (int i = 0; i < authority.length; i++) {
                for (int j = 0; j < 1; j++) {
                    System.out.println("A/H[ " + i + "]=" + String.format("%.7f", ini_authority[i][0]) + "/" + String.format("%.7f", ini_hub[i][0]) + " ");
                }
            }
            check_errorbound(pre_authority, pre_hub, authority, hub, transpose, graph,
                    scale_authority, scale_hub, errorate);
        }

    }

    //public static void hub_athority(double[][] authority, double[][] hub, int val, int vertices)
    public static void hub_athority(double[][]authority, double[][]hub, int val, int vertices){
        int size = authority[0].length;

        if (vertices < 10) {
            if (val == 0) {
                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j < size; j++) {
                        authority[i][j] = 0.0;
                        hub[i][j] = 0.0;
                    }
                }

            } else if (val == 1) {
                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j < size; j++) {
                        authority[i][j] = 1.0;
                        hub[i][j]= 1.0;
                    }
                }

            } else if (val == -1) {

                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j < size; j++) {
                        authority[i][j] = 1.0 / vertices;
                        hub[i][j] = 1.0 / vertices;
                    }

                }
            } else if (val == -2) {
                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j < size; j++) {
                        authority[i][j]= 1.0 / Math.sqrt(vertices);
                        hub[i][j]= 1.0 / Math.sqrt(vertices);
                    }

                }

            }

            // If the graph has N GREATER than 10, iterations = 0 and initialvalue = -1
        } else {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < size; j++) {
                    authority[i][j] = 1.0 / vertices;
                    hub[i][j]= 1.0 / vertices;
                }
            }
        }
    }

    public static void iteration(double[][] authority, double[][] hub, double[][] graph, double[][] transpose, double scale_authority, double scale_hub, int iterations) {

        int count = 1;
        while (count <= iterations) {
            if (count<10) System.out.print("Iter :  "+(count)+" :");
            else System.out.print("Iter : "+(count)+" :");
            for (int i = 0; i < authority.length; i++) {
                for (int j = 0; j < authority[0].length; j++) {
                    authority[i][j]=authority[i][j]/Math.sqrt(scale_authority);
                    hub[i][j]=hub[i][j]/Math.sqrt(scale_hub);
                    System.out.print("A/H[ " + (i) + "]=" + String.format("%.7f", authority[i][j]) + "/" + String.format("%.7f", hub[i][j]) + " ");

                }
            }


            System.out.println();
            count++;
            scale_authority = 0.0;
            scale_hub = 0.0;
            double temp = 0.0;

            for (int i = 0; i < transpose.length; i++) {
                for (int j = 0; j < hub[0].length; j++) {
                    for (int k = 0; k < transpose[0].length; k++) {
                        temp += transpose[i][k] * hub[k][j];
                        //authority[i][j]+=transpose[i][k] * hub[k][j];

                    }
                    authority[i][j] = temp;
                    temp = 0.0;
                    scale_authority += authority[i][j] * authority[i][j];
                }
            }
            temp = 0.0;

            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < authority[0].length; j++) {
                    for (int k = 0; k < graph[0].length; k++) {
                        temp += graph[i][k] * authority[k][j];
                    }
                    hub[i][j] = temp;
                    temp = 0.0;
                    scale_hub += hub[i][j] * hub[i][j];

                }
            }

        }
    }

    public static void check_errorbound(double[][]pre_authority,double[][]pre_hub,double[][]authority,double[][]hub,double [][]transpose,double [][]graph,
                                        double scale_authority,double scale_hub, double errorate) {
        boolean flag = false;
        int count =1;
        while (flag == false) {

            for(int i =0;i<authority.length;i++){
                for (int j =0;j<authority[i].length;j++){
                    pre_authority[i][j]=authority[i][j];
                }
            }
            for(int i =0;i<hub.length;i++){
                for (int j =0;j<hub[i].length;j++){
                    pre_hub[i][j]=hub[i][j];
                }
            }

            System.out.println("Iter   :"+(count));
            for (int i = 0; i < authority.length; i++) {

                for (int j = 0; j < 1; j++) {
                    authority[i][j]=authority[i][j]/Math.sqrt(scale_authority);
                    hub[i][j]=hub[i][j]/Math.sqrt(scale_hub);
                    if(i<10)
                        System.out.println("A/H[  " + i + "]=" + String.format("%.7f", authority[i][j])+"/"+String.format("%.7f", hub[i][j]));
                    else
                        System.out.println("A/H[ " + i + "]=" + String.format("%.7f", authority[i][j])+"/"+String.format("%.7f", hub[i][j]));


                }
            }

            System.out.println();
            scale_authority = 0.0;
            scale_hub = 0.0;
            count+=1;
            double temp = 0.0;

            for (int i = 0; i < transpose.length; i++) {
                for (int j = 0; j < hub[0].length; j++) {
                    for (int k = 0; k < transpose[0].length; k++) {
                        temp += transpose[i][k] * hub[k][j];

                    }
                    authority[i][j] = temp;
                    temp = 0.0;
                    scale_authority += authority[i][j] * authority[i][j];
                }
            }

            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < authority[0].length; j++) {
                    for (int k = 0; k < graph[0].length; k++) {
                        temp += graph[i][k] * authority[k][j];
                    }
                    hub[i][j] = temp;
                    temp = 0.0;
                    scale_hub += hub[i][j] * hub[i][j];

                }
            }

            boolean a =helper(pre_authority, authority, errorate);
            boolean b =helper(pre_hub, hub, errorate);

            if(a&&b){flag = true;}

        }
    }
    public static boolean helper(double[][]pre,double[][]current,double errorate){
        for (int i =0;i<pre.length;i++){
            for (int j = 0;j<1;j++){
                if (Math.abs(pre[i][j]-current[i][j]) >errorate){
                    return false;
                }
            }
        }
        return true;
    }
}


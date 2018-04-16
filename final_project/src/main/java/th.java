import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class th {
    private static Random random = null; //Random Generator


    private static int len; // The length of chromosome

    private static int capacity; //The capacity of bag
    private static int scale; //The scale of the population
    private static int maxgen; //The maximum of descendants
    private static float irate; //The rate of crossover
    private static float arate1; //The rate of mutation (each individuals)
    private static float arate2; //The rate of mutation (The possibility of variation in each of the individuals that determines the mutation)


    static class fit {
        private int num = 0;
        private int position = 0;

        public void setnum(int num)
        {
            this.num=num;
        }

        public int getnum()
        {
            return num;
        }

        public void setposition(int position)
        {
            this.position=position;
        }

        public int getposition()
        {
            return position;
        }

    }

    public static void sort(fit[] arr){
        /*
         *  The first step: heap the array
         *  beginIndex = The first non-leaf node
         *  Starting from the first non-leaf node. There is no need to start from the last leaf node.
         *  A leaf node can be thought of as a node that already meets the heap requirements. The root node is its own and its own value is the highest.
         */
        int len = arr.length - 1;
        int beginIndex = (len - 1) >> 1;
        for(int i = beginIndex; i >= 0; i--){
            maxHeapify(arr, i, len);
        }

        /*
         * Step 2: Sort the heap data
         * Each time the root node A[0] is removed from the topmost layer, the position of the tail node is swapped, and the length-1 is traversed at the same time.
         * Then reorganize the last element that is swapped to the root node to make it conform to the properties of the heap.
         * Until the unsorted heap length is 0.
         */
        for(int i = len; i > 0; i--){
            swap(arr, 0, i);
            maxHeapify(arr, 0, i - 1);
        }
    }

    private static void swap(fit[] arr, int i, int j){
        int tempN = arr[i].getnum();
        int tempP = arr[i].getposition();
        arr[i].setnum(arr[j].getnum());
        arr[i].setposition(arr[j].getposition());
        arr[j].setnum(tempN);
        arr[j].setposition(tempP);
    }

    /**
     * Adjust the index to the data at index so that it conforms to the properties of the heap.
     *
     * @param index   The index of the data that needs to be heaped
     * @param len     The length of the unsorted heap (array)
     */
    private static void maxHeapify(fit[] arr, int index, int len){
        int li = (index << 1) + 1; // Left child index
        int ri = li + 1;           // Right child index
        int cMax = li;             // The maximum index of the child node value, the default left child node.

        if(li > len) return;       // Left child index exceeds the calculation range and returns directly.
        if(ri <= len && arr[ri].getnum() > arr[li].getnum()) // First determine the left and right children, which is larger.
            cMax = ri;
        if(arr[cMax].getnum() > arr[index].getnum()){
            swap(arr, cMax, index);      // If the parent node is replaced by a child node,
            maxHeapify(arr, cMax, len);  // Needed to continue to determine whether the replaced parent node meets the characteristics of the heap.
        }
    }

    public static void sort(int[] arr){
        /*
         *  The first step: heap the array
         *  beginIndex = The first non-leaf node
         *  Starting from the first non-leaf node. There is no need to start from the last leaf node.
         *  A leaf node can be thought of as a node that already meets the heap requirements. The root node is its own and its own value is the highest.
         */
        int len = arr.length - 1;
        int beginIndex = (len - 1) >> 1;
        for(int i = beginIndex; i >= 0; i--){
            maxHeapify(arr, i, len);
        }

        /*
         * Step 2: Sort the heap data
         * Each time the root node A[0] is removed from the topmost layer, the position of the tail node is swapped, and the length-1 is traversed at the same time.
         * Then reorganize the last element that is swapped to the root node to make it conform to the properties of the heap.
         * Until the unsorted heap length is 0.
         */
        for(int i = len; i > 0; i--){
            swap(arr, 0, i);
            maxHeapify(arr, 0, i - 1);
        }
    }

    private static void swap(int[] arr, int i, int j){
        int tempN = arr[i];

        arr[i]=arr[j];
        arr[j]=tempN;
    }

    /**
     * Adjust the index to the data at index so that it conforms to the properties of the heap.
     *
     * @param index   The index of the data that needs to be heaped
     * @param len     The length of the unsorted heap (array)
     */
    private static void maxHeapify(int[] arr, int index, int len){
        int li = (index << 1) + 1; // Left child index
        int ri = li + 1;           // Right child index
        int cMax = li;             // The maximum index of the child node value, the default left child node.

        if(li > len) return;       // Left child index exceeds the calculation range and returns directly.
        if(ri <= len && arr[ri]> arr[li]) // First determine the left and right children, which is larger.
            cMax = ri;
        if(arr[cMax]> arr[index]){
            swap(arr, cMax, index);      // If the parent node is replaced by a child node,
            maxHeapify(arr, cMax, len);  // Needed to continue to determine whether the replaced parent node meets the characteristics of the heap.
        }
    }

    public static void build(int capacity1, int scale1, int maxgen1, float irate1, float arate11, float arate21){

        capacity = capacity1;
        scale = scale1;
        maxgen = maxgen1;
        irate = irate1;
        arate1 = arate11;
        arate2 = arate21;
        random = new Random(System.currentTimeMillis());

        int[] profit = { 220,208,198,192,180,180,165,162,160,158,
                155,130,125,122,120,118,115,110,105,101,
                100,100,98,96,95,90,88,82,80,77,
                75,73,72,70,69,66,65,63,60,58,
                56,50,30,20,15,10,8,5,3,1};// Items value
        int[] weight = {80,82,85,70,72,70,66,50,55,25,
                50,55,40,48,50,32,22,60,30,32,
                40,38,35,32,25,28,30,22,50,30,
                45,30,60,50,20,65,20,25,30,10,
                20,25,15,10,10,10,4,4,2,1};// Items weight
        len = weight.length;

        int divide=10;
        int from=0;
        boolean[][] last=new boolean[scale/(divide*2)][len];
        int resultnum=0;
        int tnum=0;

        while(from<scale) {
            tnum++;

            CompletableFuture<boolean[]> parsort1 = solve(from, from+divide, weight, profit, tnum);
            from=from+divide;
            CompletableFuture<boolean[]> parsort2 = solve(from, from+divide, weight, profit, tnum);
            from=from+divide;


            CompletableFuture<boolean[]> parsort = parsort1.
                    thenCombine(parsort2, (a1, a2) -> {
                        boolean[] result = new boolean[len];
                        // TODO implement me333

                        int p1=0, p2=0;
                        for(int i=0;i<len;i++)
                        {
                            if(a1[i])
                            {
                                p1=p1+profit[i];
                            }
                            if(a2[i])
                            {
                                p2=p2+profit[i];
                            }
                        }
                        if(p1>p2)
                        {
                            return a1;
                        }
                        else
                        {
                            return a2;
                        }
                    });

            int finalResultnum = resultnum;
            parsort.whenComplete((result, throwable) -> {
                last[finalResultnum]=result;

                System.out.println("The best coding：");
                int tv=0;
                for (int i = 0; i < len; i++) {
                    if(result[i])
                    {
                        System.out.print(1 + ",");
                        tv=tv+profit[i];
                    }
                    else
                    {
                        System.out.print(0 + ",");
                    }
                }
                System.out.println();
                System.out.println("No."+(finalResultnum+1)+" of depth best coding value：");
                System.out.println(tv+"\n");
            });
            parsort.join();
            resultnum++;
        }
        int[] tv=new int[last.length];
        for(int i=0;i<last.length;i++)
        {
            for (int j=0;j<len;j++)
            {
                if(last[i][j])
                {
                    tv[i]=tv[i]+profit[j];
                }
            }
        }
        System.out.println("Each sub-species best coding value：");
        for(int i=0;i<tv.length;i++)
        {
            System.out.println(tv[i]);
        }
        sort(tv);
        System.out.println("The final best result: "+tv[tv.length-1]);




    }

    private static CompletableFuture<boolean[]> solve(int from, int to, int[] weight, int[] profit, int tnum) {

        boolean[][] population = new boolean[to-from][len]; //Previous generation
        fit[] fitness = new fit[to-from]; //Population fitness
        for(int i=0;i<to-from;i++)
        {
            fitness[i]=new fit();
        }
        double[] round = new double[to-from]; //Roulette
        int bestT=0;// The best generation
        int bestFitness=0; //The best fitness value
        boolean[] bestUnit = new boolean[len]; //Optimal individual item selection strategy

        return CompletableFuture.supplyAsync(
                () -> {

                    initPopulation(fitness, population);
                    /*System.out.println("初始种群...");
                    for (int k = 0; k < to-from; k++) {
                        for (int i = 0; i < len; i++) {
                            if(population[k][i])
                            {
                                System.out.print(1 + ",");
                            }
                            else
                            {
                                System.out.print(0 + ",");
                            }
                        }
                    }*/
                    for(int i = 0; i < maxgen; i++) {
                        //Calculate population fitness value
                        calcFitness(round, fitness, population, weight, profit);
                        //Record the best individual
                        recBest(i, fitness,bestFitness, bestUnit, bestT, population);
                        //Population selection
                        select(round, population);
                        //Crossover
                        intersect(population);
                        //Mutation
                        aberra(population);
                    }

                    int totalWeight = 0;
                    for(int i = 0; i < bestUnit.length; i++) {
                        if(bestUnit[i]){
                            totalWeight += weight[i];
                        }
                    }
                    /*System.out.println("最后种群...");
                    for (int k = 0; k < to-from; k++) {
                        for (int i = 0; i < len; i++) {
                            if(bestUnit[i])
                            {
                                System.out.print(1 + ",");
                            }
                            else
                            {
                                System.out.print(0 + ",");
                            }
                        }
                        System.out.println();
                        System.out.println("---" + bestFitness);
                    }*/
                    System.out.print("No."+tnum+" times:");
                    int tv=0;
                    for (int i = 0; i < len; i++) {
                        if(bestUnit[i])
                        {
                            tv=tv+profit[i];
                        }
                    }
                    System.out.print(tv+"\n");
                    return bestUnit;
                }
        );

    }

    //Initialize initial population
    private static boolean[][] initPopulation(fit[] fitness, boolean[][] population) {
        for(int i = 0; i < population.length; i++) {
            for(int j=0;j<len;j++)
            {
                int i2 = random.nextInt(2);
                if(i2==1)
                {
                    population[i][j]=true;
                }
                else
                {
                    population[i][j]=false;
                }
            }
        }
        return population;
    }

    //calculate the weight and the value of the selecting strategy
    private static int evaluate(boolean[] unit, int[] weight, int[] profit) {
        int profitSum = 0;
        int weightSum = 0;
        for (int i = 0; i < unit.length; i++) {
            if (unit[i]) {
                weightSum += weight[i];
                profitSum += profit[i];
            }
        }
        if (weightSum > capacity) {
            //The weight of the individual's corresponding all items exceeds the capacity of the bag
            return 0;
        } else {
            return profitSum;
        }
    }

    //Calculate the fitness of all individuals in the population
    private static void calcFitness(double[] round, fit[] fitness, boolean[][] population, int[] weight, int[] profit) {
        int wholefitness=0;

        for(int i = 0; i < population.length; i++) {
            fitness[i].setnum(evaluate(population[i], weight, profit));
            fitness[i].setposition(i);
            wholefitness=wholefitness+fitness[i].getnum();
        }
        if(wholefitness!=0) {
            round[0] = (double)fitness[0].getnum() / wholefitness;
            for(int i = 1; i < population.length; i++)
            {
                round[i] = round[i-1]+(double)fitness[i].getnum()/wholefitness;
            }
        }
    }

    //Record the best individual
    private static void recBest(int gen, fit[] fitness, int bestFitness, boolean[] bestUnit, int bestT, boolean[][] population) {
        /*
        for(int i = 0; i < population.length; i++) {
            if(fitness[i].getnum() > bestFitness) {
                bestFitness = fitness[i].getnum();
                bestT = gen;// 最好的染色体出现的代数;
                for(int j = 0; j < len; j++) {
                    bestUnit[j] = population[i][j];
                }
            }
        }
        */
        sort(fitness);

        bestFitness = fitness[population.length-1].getnum();
        bestT = gen;// The best chromosomes
        for(int j = 0; j < len; j++) {
            bestUnit[j] = population[population.length-1][j];
        }

    }

    //Selection strategy: Select the surviving individual from the roulette plate, randomly generated by the remaining individuals
    private static void select(double[] round, boolean[][] population) {

        boolean[][] tmpPopulation = new boolean[population.length][len];
        int k=0;
        for(int i=0;i< population.length-1;i++)
        {
            double i2=Math.random();
            if(i2>round[i] && i2<round[i+1])
            {
                tmpPopulation[k]=population[i];
                k++;
            }
        }
        double i2=Math.random();
        if(i2>round[population.length-2] && i2<round[population.length-1] )
        {
            tmpPopulation[k]=population[population.length-1];
            k++;
        }

        //Generate the remaining individuals randomly
        for(int i = k; i < population.length; i++) {

            for(int j = 0; j < len; j++) {
                i2=Math.random();
                if(i2>0.5)
                {
                    tmpPopulation[i][j]=true;
                }
                else
                {
                    tmpPopulation[i][j]=false;
                }
            }
        }
        population = tmpPopulation;
    }

    //Crossover
    private static void intersect(boolean[][] population) {
        for(int i = 0; i < population.length; i = i + 2)
            for(int j = 0; j < len; j++) {
                double i2=Math.random();
                if(i2< irate) {
                    boolean tmp = population[i][j];
                    population[i][j] = population[i + 1][j];
                    population[i + 1][j] = tmp;
                }
            }
    }

    //Mutation
    private static void aberra(boolean[][] population) {
        for(int i = 0; i < population.length; i++) {
            if(Math.random() < arate1) {
                continue;
            }
            for(int j = 0; j < len; j++) {
                if(Math.random() < arate2) {
                    population[i][j] = !population[i][j];
                }
            }
        }
    }

}

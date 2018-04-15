import java.io.File;
import java.util.Random;

public class GAK {

    private Random random = null; // Random Generator

    private int[] weight = null; // Items weight
    private int[] profit = null; // Items value
    private int len; // The length of chromosome

    private int capacity; // The capacity of bag
    private int scale; // The scale of the population
    private int maxgen; // The maximum of descendants
    private float irate; // The rate of crossover
    private float arate1; // The rate of mutation (each individuals)
    private float arate2; // The rate of mutation (The possibility of variation in each of the individuals that determines the mutation)
    private File data = null;

    private boolean[][] population = null; // Previous generation
    private fit[] fitness = null; // Population fitness
    private double[] round = null; // Roulette
    private int bestT;// The best generation
    private int bestFitness; // The best fitness
    private boolean[] bestUnit = null; // Optimal individual item selection strategy

    class fit {
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

    public void sort(fit[] arr){
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

    private void swap(fit[] arr, int i,int j){
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
    private void maxHeapify(fit[] arr, int index,int len){
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

    /**
     * @param capacity : The capacity of bag
     * @param scale ：The scale of the population
     * @param maxgen ：The maximum of descendants
     * @param irate ：The rate of crossover
     * @param arate1 ：The rate of mutation (each individuals)
     * @param arate2 : The rate of mutation (The possibility of variation in each of the individuals that determines the mutation)

     */
    public GAK(int capacity, int scale, int maxgen, float irate, float arate1, float arate2, File data) {
        this.capacity = capacity;
        this.scale = scale;
        this.maxgen = maxgen;
        this.irate = irate;
        this.arate1 = arate1;
        this.arate2 = arate2;
        this.data = data;
        random = new Random(System.currentTimeMillis());
    }

    //Reading item weight and item value data
    private void readData() {
         int[] v = { 220,208,198,192,180,180,165,162,160,158,
                155,130,125,122,120,118,115,110,105,101,
                100,100,98,96,95,90,88,82,80,77,
                75,73,72,70,69,66,65,63,60,58,
                56,50,30,20,15,10,8,5,3,1};// Items value
         int[] b = {80,82,85,70,72,70,66,50,55,25,
                50,55,40,48,50,32,22,60,30,32,
                40,38,35,32,25,28,30,22,50,30,
                45,30,60,50,20,65,20,25,30,10,
                20,25,15,10,10,10,4,4,2,1};// Items weight
        weight = b;
        profit = v;
        len = weight.length;
    }

    //Initialize initial population
    private void initPopulation() {
        fitness = new fit[scale];
        for(int i=0;i<scale;i++)
        {
            fitness[i]=new fit();
        }
        population = new boolean[scale][len];

        for(int i = 0; i < scale; i++) {
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
    }

    //calculate the weight and the value of the selecting strategy
    private int evaluate(boolean[] unit) {
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
    private void calcFitness() {
        int wholefitness=0;
        round=new double[scale];
        for(int i = 0; i < scale; i++) {
            fitness[i].setnum(evaluate(population[i]));
            fitness[i].setposition(i);
            wholefitness=wholefitness+fitness[i].getnum();
        }
        if(wholefitness!=0) {
            round[0] = (double)fitness[0].getnum() / wholefitness;
            for(int i = 1; i < scale; i++)
            {
                round[i] = round[i-1]+fitness[i].getnum()/wholefitness;
            }
        }
    }

    //Record the best individual
    private void recBest(int gen) {
        int q=-1;
        for(int i = 0; i < scale; i++) {
            if(fitness[i].getnum() > bestFitness) {
                bestFitness = fitness[i].getnum();
                q=i;
                bestT = gen;// 最好的染色体出现的代数;
                bestUnit = new boolean[len];
                for(int j = 0; j < len; j++) {
                    bestUnit[j] = population[i][j];
                }
            }
        }
        //System.out.println("实际"+q);

        //sort(fitness);
//        for(int i = 0; i < scale; i++) {
//            System.out.print(fitness[i].getposition()+",");
//        }

        //bestFitness = fitness[scale-1].getnum();
        //bestT = gen+1;// The best chromosomes
        //bestUnit = new boolean[len];
        //bestUnit= population[fitness[scale-1].getposition()];

//        System.out.println("theoretical "+fitness[scale-1].getposition());

    }

    //Selection strategy: Select the surviving individual from the roulette plate, randomly generated by the remaining individuals
    private void select() {

        boolean[][] tmpPopulation = new boolean[scale][len];
        int k=0;
        for(int i=0;i< scale-1;i++)
        {
            double i2=Math.random();
            if(i2>round[i] && i2<round[i+1])
            {
                tmpPopulation[k]=population[i];
                k++;
            }
        }
        double i2=Math.random();
        if(i2>round[scale-2] && i2<round[scale-1] )
        {
            tmpPopulation[k]=population[scale-1];
            k++;
        }

        //Generate the remaining individuals randomly
        for(int i = k; i < scale; i++) {

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
    private void intersect() {
        for(int i = 0; i < scale; i = i + 2)
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
    private void aberra() {
        for(int i = 0; i < scale; i++) {
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

    //Using Genetic algorithm to solve the problem
    public void solve() {
        readData();
        initPopulation();
//        System.out.println("Initial population...");
//        for (int k = 0; k < scale; k++) {
//            for (int i = 0; i < len; i++) {
//                if(population[k][i])
//                {
//                    System.out.print(1 + ",");
//                }
//                else
//                {
//                    System.out.print(0 + ",");
//                }
//            }
//            System.out.print("----bestFitness: " + bestFitness+"\n");
//        }
        for(int i = 0; i < maxgen; i++) {
            //Calculate population fitness value
            calcFitness();
            //Record the best individual
            recBest(i);
            //Population selection
            select();
            //Crossover
            intersect();
            //Mutation
            aberra();
        }

        int totalWeight = 0;
        for(int i = 0; i < bestUnit.length; i++) {
            if(bestUnit[i]){
                totalWeight += weight[i];
            }
        }
//        System.out.println("Last population...");
//        for (int k = 0; k < scale; k++) {
//            for (int i = 0; i < len; i++) {
//                if(bestUnit[i])
//                {
//                    System.out.print(1 + ",");
//                }
//                else
//                {
//                    System.out.print(0 + ",");
//                }
//            }
//
//            System.out.print("----bestFitness: " + bestFitness+"\n");
//        }

        System.out.println("The number of best coding generation：");
        System.out.println(bestT);
        System.out.println("The best coding weight");
        System.out.println("total weight:" + totalWeight);
        System.out.println("The best coding value");
        System.out.println("total value:" + bestFitness);
        System.out.println("The best coding is：");
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
        System.out.println("");

    }

    public static void main(String[] args) {
        File data = new File(".//data//data1.txt");

        //The capacity of bag
        //The scale of the population
        //The maximum of descendants
        //The rate of crossover
        //The rate of mutation (each individuals)
        //The rate of mutation (The possibility of variation in each of the individuals that determines the mutation)
        th.build(1000, 200, 500, 0.8f, 0.9f, 0.5f);

        //GAK gaKnapsack = new GAK(1000, 20, 500, 0.8f, 0.9f, 0.5f, data);
        //gaKnapsack.solve();
    }
}
/*
int[] b= new int[]{1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1,};
        int t=0;
        for(int i = 0; i < b.length; i++) {
            if(b[i]==1){
                t += weight[i];
            }
        }
 */
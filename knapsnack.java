import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class knapsnack {
    int generation; // Current Generation 
    int population_size; // size of population 
    int term_gen; // Termination on reaching 'term_gen' population
    float mutation_rate; 
    float crossover_rate;
    String best;
    Integer best_value;
    int best_gen;
    int[] helper1;
    int[] helper2;

    Vector<String> individuals_cur; // "String": len of each is 'number of items' ; Individuals of current generation 
    Vector<String> individuals_next; //  Individuals of next generation
    Vector<Integer> values; //  values of the items corresponding to the index
    Vector<Integer> weights; //  weights of the items corresponding to the index
    Vector<String> parents;
    Vector<Integer> ind_fitness ; // sorted on fitness
    Vector<Integer> ind_elite;

    Vector<Integer> fitness;
    Vector<Float> norm_fitness;
    Vector<Float> cumulation_fitness;
    float parent_margin;
    float elite_margin;

    Vector<String>parent1;
    Vector<String>parent2;

    int n_items,capacity; // capacity of the knapsnack
     

    knapsnack(){
        this.generation = 0;
        this.mutation_rate = 0.05f;
        this.crossover_rate = 0.95f;
        this.term_gen = 0;
        this.values = new Vector<Integer>();
        this.weights = new Vector<Integer>();
        this.individuals_cur = new Vector<String>();
        this.individuals_next = new Vector<String>();
        this.parent1 = new Vector<String>();
        this.parent2 = new Vector<String>();

        this.best = "";
        this.best_gen = 0;
        this.best_value = -1;
        
        this.parents = new Vector<String>();
        // this.parent_margin = 0.2f;
        this.elite_margin = 0.2f;

        this.fitness = new Vector<Integer>();
        this.norm_fitness = new Vector<Float>();
        this.cumulation_fitness = new Vector<Float>();
        this.ind_fitness = new Vector<Integer>();
        this.ind_elite = new Vector<Integer>();
    }

    void fitness_sort(){
        this.helper1 = new int[population_size];
        for(int i =0;i<population_size;i++) ind_fitness.set(i,i);
        this.helper2 = new int[population_size];
        fit_sort_helper(0, population_size -1);
    }
    
    void fit_sort_helper(int low, int high) {
        if (low < high) {
          int middle = low + (high - low) / 2;
          fit_sort_helper(low, middle);
          fit_sort_helper(middle + 1, high);
          merge(low, middle, high);
        }
    }

    void merge(int low, int middle, int high) {
        
        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
          helper1[i] = fitness.get(i);
          helper2[i] = ind_fitness.get(i);
        }
    
        int i = low;
        int j = middle + 1;
        int k = low;
        // Copy the smallest values from either the left or the right side back to the original array
        while (i <= middle && j <= high) {
          if (helper1[i] >= helper1[j]) {
        //    numbers[k] = helper[i];
            fitness.set(k, helper1[i]);
            ind_fitness.set(k,helper2[i]);
            i++;
          
        } else {
            // numbers[k] = helper[j];
            fitness.set(k, helper1[j]);
            ind_fitness.set(k,helper2[j]);
            j++;
          }
          k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
        //   numbers[k] = helper[i];
            fitness.set(k, helper1[i]);
            ind_fitness.set(k,helper2[i]);
          k++;
          i++;
        }
         //Add
         // Copy the rest of the right side of the array into the target array
        while (j <= high) {
        //   numbers[k] = helper[j];
            fitness.set(k, helper1[j]);
            ind_fitness.set(k,helper2[j]);
          k++;
          j++;
        }
    }

    void next_gen(){
        generation++;
        for(int i = 0;i<population_size;i++){
            individuals_cur.set(i, individuals_next.get(i));
        }
        // individuals_cur = individuals_next;
        population_size = individuals_next.size();
    }
    // boolean function for termination
    boolean isterminate(){
        if(generation  == term_gen){
            return true;
        }
        return false;
    }
    int weight_calc(String s){
        int ans = 0;
        for(int j = 0; j < n_items;j++){
            // sum weights and values for each individual
            if(s.charAt(j) == '1' ){
                ans += weights.get(j);
            }
        }
        return ans;
     }
    int value_calc(String s){
        int ans = 0;
        for(int j = 0; j < n_items;j++){
            // sum weights and values for each individual
            if(s.charAt(j) == '1' ){
                ans += values.get(j);
            }
        }
        return ans;
     }
    
    void fitness(){
        float tot_fitness = 0.0f;
        for(int i = 0;i< population_size; i++){
            
            if(weight_calc(individuals_cur.get(i)) > capacity) fitness.set(i, 0);
            else fitness.set(i, value_calc(individuals_cur.get(i)));

            // Storing in HashMap for sorting and indexes also needed ( for selection )
            // fitness_ind.put(fitness.get(i),i);
            tot_fitness += fitness.get(i).floatValue();
        }

        norm_fitness.set(0, fitness.get(0).floatValue()/tot_fitness );
        for(int i = 1; i < population_size;i++){
            norm_fitness.set(i, fitness.get(i).floatValue()/tot_fitness );
            cumulation_fitness.set(i, cumulation_fitness.get(i-1) + norm_fitness.get(i));
        }
    }


    // Using single point crossover
    void crossover(){
        individuals_next.clear();
        float rand_pb ; 
        String temp = "";
        int cross_bit = 0;
        
        for(int i = 0;i< population_size-1;i+=2){
            rand_pb = ThreadLocalRandom.current().nextFloat(); 
            if(rand_pb > crossover_rate) {
                individuals_next.add(parents.get(i));
                individuals_next.add(parents.get(i));
            }else{
            rand_pb = ThreadLocalRandom.current().nextFloat(); 
            // Bit to cross over 
            cross_bit =  Math.min(Math.round( rand_pb*n_items),n_items -2) ;
            temp = parents.get(i).substring(0,cross_bit) + parents.get(i+1).substring(cross_bit,n_items);
            individuals_next.add(temp);
            temp = parents.get(i+1).substring(0,cross_bit) + parents.get(i).substring(cross_bit,n_items);
            individuals_next.add(temp);
            }
        }
        if(population_size % 2 == 1){
            individuals_next.add(parents.get(population_size -1));
        }
        //  Odd population
        // if(population_size % 2 == 1){
        //     individuals_next.add(parent1.get(population_size/2));
        // }else{
        //     int i = population_size/2 -1;
        //     rand_pb = ThreadLocalRandom.current().nextFloat(); 
        //     // Bit to cross over 
        //     cross_bit =  Math.min(Math.round( rand_pb*n_items),n_items -2) ;
        //     temp = parent1.get(i).substring(0,cross_bit-1) + parent2.get(i).substring(cross_bit,n_items);
        //     individuals_next.add(temp);
        //     temp = parent2.get(i).substring(0,cross_bit-1) + parent1.get(i).substring(cross_bit,n_items);
        //     individuals_next.add(temp);
        // }+
    }

// mutation , by testing justing chaning one bit is better than multiple bits in this case
    void mutation(){
        float rand_pb ; 
        String temp = "";
        int mut_bit = 0;
        for(int i = 0;i<individuals_next.size();i++){
            rand_pb = ThreadLocalRandom.current().nextFloat(); 
            if(rand_pb > mutation_rate) continue;
            rand_pb = ThreadLocalRandom.current().nextFloat(); 
            mut_bit =  Math.min(Math.round( rand_pb*n_items),n_items -1) ;
            
            temp = individuals_next.get(i).substring(0,mut_bit);
            if(individuals_next.get(i).charAt(mut_bit) == '1') temp+= '0';
            else temp += '1';
            if(mut_bit !=n_items)
                 temp += individuals_next.get(i).substring(mut_bit+1,n_items);
            individuals_next.set(i, temp);
        }
        
    }

    
    // assuming  fitness is sorted
    void selection(){
        parents.clear();
        ind_elite.clear();

        float rand_pb;
        //Elitism 
        int i = 0,j=0,temp = 0;
        int n_elite = Math.round( elite_margin*population_size) ;
        
        // Best value;
        
        if(fitness.get(0) > best_value){
            // System.out.println("Best found " + best + " value "+ fitness.get(i));
            best = individuals_cur.get(ind_fitness.get(0));
            best_value = fitness.get(0);
            best_gen = generation;
        }
        

        for(i = 0;i<n_elite;i++){
            parents.add(individuals_cur.get(ind_fitness.get(i)));
            // add in elite index
            ind_elite.add(ind_fitness.get(i));
        }
/** 
        Choosing parents 
        int n_parent = Math.round( parent_margin*population_size) ;
        for(i = 0;i<n_parent;i++){
            rand_pb =  ThreadLocalRandom.current().nextFloat(); 
            if(rand_pb < cumulation_fitness.get(1)){
                
            }else{
                j = 1;
                while(cumulation_fitness.get(j-1) <= rand_pb && rand_pb <= cumulation_fitness.get(j) ){
                    // Got the individual
                    // add to parent 
                }
            }

        }
*/       
        // Can change the no.of parents generated 
        i = n_elite;
        while(i<population_size){
            // if(iselite(i)) continue;
            rand_pb =  ThreadLocalRandom.current().nextFloat(); 
            
            // Roulette Selection
            j = 0; 
            // using the cumulative normalised f(x) for individuals probability
            
            while( cumulation_fitness.get(j).compareTo(rand_pb) < 1 && j!= population_size -1){ 
                j++;
             }
             
            //if(ind_elite.contains(j)){
                // continue;
            // }else{
                i++;
                // System.out.println("Rand"  + rand_pb +" j " + j + " F " + cumulation_fitness.get(j).floatValue());
                parents.add(individuals_cur.get(j));  
            // }

        }
    }



    void init_population(){

        for(int i = 0;i<population_size;i++){
            individuals_cur.add("");
            individuals_next.add("");
            fitness.add(0);
            ind_fitness.add(0);
            norm_fitness.add(0.0f);
            cumulation_fitness.add(0.0f);
        }
        String indi;
        int temp;

        // Creating population 
        for(int i = 0;i<population_size;i++){
           do{
            temp = ThreadLocalRandom.current().nextInt(); 
            indi = int_to_string(temp);
           }
           while(weight_calc(indi) > capacity); 
           individuals_cur.set(i,indi);
           //    parent1.add("");
        //    parent2.add("");
            // Generate 64 bit and take the 'nof items' Most significate bits
        } 
    }

    String int_to_string(int a){
        // StringBuilder s = new StringBuilder();
        String s = "",s1="";
        for(int i = 0; i < n_items; i++)
        {
            if(a == 0)
                s+='0';
            else
            {
                if((a % 2) == 0)
                    s+='0';
                else
                    s+='1';
                a=a/2;
            }
        }
        return s;
    }

    void print_vec(Vector<String> v){
        for(int i = 0;i< v.size();i++){
            System.out.println( i + " " + v.get(i));
        }
    }
    void print_vecf(Vector<Float> v){
        for(int i = 0;i< v.size();i++){
            System.out.println( i + " " + v.get(i));
        }
    }
    void print_veci(Vector<Integer> v){
        for(int i = 0;i< v.size();i++){
            System.out.println( i + " " + v.get(i));
        }
    }

    // prints the best solution
    void bestsolution(){
        fitness();
       
        System.out.print("The best Solution is : Weight " + weight_calc(best) + " Value " +  value_calc(best) + "\n");
        System.out.print("Knapsnack items: ");
        for(int i = 0;i<n_items;i++){
            if(best.charAt(i) == '1'){
                System.out.print(""+ (i  )+ " ");
            }
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);   
        knapsnack ks = new knapsnack();
        int i,j;
        int temp;
        System.out.print("Number of Items : ");
        ks.n_items = sc.nextInt();
        System.out.print("Knapsnack capacity : ");
        ks.capacity = sc.nextInt();
        
        System.out.print("Largest Object Weight : ");
        temp = sc.nextInt();

        for( i = 0; i < ks.n_items; i++){
            ks.weights.add( Math.round(ThreadLocalRandom.current().nextFloat()*temp) );
        }
        
        System.out.print("Largest Object Value : ");
        temp = sc.nextInt();

        for( i = 0; i < ks.n_items; i++){
            ks.values.add( Math.round(ThreadLocalRandom.current().nextFloat()*temp) );
        }

        for(i = 0;i<ks.n_items;i++){
            System.out.print(""+ i + " ");
        }
        System.out.println("\n");
        for(i = 0;i<ks.n_items;i++){
            System.out.print(""+ ks.values.get(i) + " ");
        }
            System.out.println("\n");
        for(i = 0;i<ks.n_items;i++){
            System.out.print(""+ ks.weights.get(i) + " ");
        }
        System.out.println("\n");
        System.out.print("Population size : ");
        ks.population_size = sc.nextInt();

        System.out.print("Crossover rate : ");
        ks.crossover_rate = sc.nextFloat();

        System.out.print("Mutation : ");
        ks.mutation_rate = sc.nextFloat();

        
        System.out.print("Stop after generations : ");
        ks.term_gen = sc.nextInt();

        // System.out.println(ks.int_to_string(ks.n_items));
        
        
        

        while(!ks.isterminate()){
            if(ks.generation == 0){
                ks.init_population();
                // System.out.print();
            }
            // ks.print_vec(ks.individuals_cur);
            ks.fitness(); // evaluate the fitness

            // ks.print_veci(ks.fitness);
            ks.fitness_sort();
            // ks.print_veci(ks.fitness);
            // System.out.println("Sorted");
            ks.selection();
            // ks.print_vec(ks.parents);
            // System.out.println("Selected");
            ks.crossover();
            // System.out.println("Crossed");
            // ks.print_vec(ks.individuals_next);
            ks.mutation();
            // System.out.println("Mutated");
            // ks.print_vec(ks.individuals_next);
            ks.next_gen();
            // ks.print_vecf(ks.cumulation_fitness);
            // System.out.println("Gen " +ks.generation +" Pop size : " +  ks.population_size);
            // break;
        }

        ks.bestsolution();
        
    }


    
}








/**
 * int l = 1;
            int r = l+1;
            if (rand_pb <= cumulation_fitness.get(l))
            {
                // Pairing individuals
                if (i<=(population_size/2))
                {
                    parent1.set(i, individuals_cur.get(l));
                }
                else
                {
                    parent2.set(i - Math.toIntExact(population_size/2), individuals_cur.get(l) );
                }
                continue;
            }
            else
            {
                while(l < population_size-1)
                {
                    if(cumulation_fitness.get(l) <= rand_pb && rand_pb <= cumulation_fitness.get(r))
                    {
                        if (i<=(population_size/2))
                        {
                            parent1.set(i, individuals_cur.get(l));
                        }
                        else
                        {
                            parent2.set(i - Math.toIntExact(population_size/2) , individuals_cur.get(l) );
                        }
                        break;
                    }
                    l++;
                    r++;
                }
            } 
 * 
 */
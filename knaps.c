

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define PI 3.14159265
#define bits 32 // number of bits to represent individual x
#define fx_max 2147483648 // The function e.g 2147483648 for f(x) = sin(x* π/2147483648)

int p_size; // Population size
int term_cond; // Terminal condition: an input number of generations
float cross_rt; // Crossover Rate
float mut_rt; // Mutation Rate

char **individual_curr; // Individuals of current generation
char **individual_next; // Individuals of next generation
int *x; // x of current generation
double *fitness,*normed,*cumulative_normed; // Fitness fx, Normed fx, Cumulative normed fx probability
int bstsol; // Best Solution in last generation

// Function to convert decimal number into its binary
char* int_to_bin(int no){
    int rem,i;
    char *binaryNumber=malloc(bits);
    
    for(i = bits-1; i >=0; i--)
    {
        if(no == 0)
            binaryNumber[i]='0';
        else
        {
            rem = no % 2;
            if(rem == 0)
                binaryNumber[i]='0';
            else
                binaryNumber[i]='1';
            no=no/2;
        }
    }
    
    return binaryNumber;
}

// Function to convert binary number into its decimal
int bin_to_dec(char *bin){
    int dec = 0;
    int i=0;
    while (bin[i] != '\0')
    {
        if (bin[i] == '1') dec = dec * 2 + 1;
        else if (bin[i] == '0') dec *= 2;
        i++;
        
    }
    
    return dec;
    
}

// Genetic Algorithm
int genetic_algo()
{
    int g=0; // generation number
    double rand_pb[p_size];// Random probability for selecting individual
    double total_fitness=0.0; // Total Fitness for calculating normed fx
    int l,r; // local variable
    int parent1[p_size],parent2[p_size];
    int cross_pt; // Cross over point
    int mut_bit;  // Mutation bit
    float cross_it; // Crossover flag
    float mut_it; // Mutation flag
    int bitno; // bit number
    int c;
    double bstfit=0.0; // Best fit (greates fx) in last generation
    
    // Allocate Memory
    individual_curr = (char**)malloc(sizeof(char*) * p_size);
    individual_next = (char**)malloc(sizeof(char*) * p_size);
    
    for(c=1; c<=p_size; ++c)
    {
        individual_curr[c] = (char*)malloc(sizeof(char) * (bits+1));
        individual_next[c] = (char*)malloc(sizeof(char) * (bits+1));
        
    }
    
    x = (int*)malloc(sizeof(int) * p_size);
    fitness=(double*)malloc(sizeof(double) * p_size);
    normed=(double*)malloc(sizeof(double) * p_size);
    cumulative_normed=(double*)malloc(sizeof(double) * p_size);
    
    
    cumulative_normed[0]=0.0; // initialize cumulative_normed
    
    // Initialize population0
    
    for (c = 1; c <= p_size; c++)
    {
        x[c] = rand()%(fx_max-1) + 1;
        individual_curr[c]=int_to_bin(x[c]);
        
    }
    
    while(g<term_cond)
    {
        total_fitness=0.0;
        
        // Evaluating fitness of each individual in Population g
        for (c = 1; c <= p_size; c++)
        {
            // Function f(x) as the fitness of the individual x
            fitness[c] = sin(x[c] * PI/fx_max);
            
            // Total of all individuals fitness
            total_fitness=total_fitness+fitness[c];
            
        }
        
        // Creating Population g+1
        
        // Selecting individual for reproduction
        
        //Calculate the normed f(x) and cumulative normed f(x) for each individual x
        for (c = 1; c <= p_size; c++)
        {
            // Normed f(x) of the individual x
            normed[c] = fitness[c]/total_fitness;
            cumulative_normed[c]=cumulative_normed[c-1] + normed[c];
        }
        
        
        // Select individual randomly based on Cumulative normed f(x)
        
        /* Pairing individuals for reproduction- e.g. for 10 population size, generate 10 random numbers,
         based on cumulative fx, select 10 corresponding individuals and pair them like 1st and 6th, 2nd and 7th etc.
         
         Thus there will be 5 pairs of parents
         */
        
        for (c = 1; c <= p_size; c++)
        {
            // Generate random numbers as many as population size
            rand_pb[c] = (double)rand() / (double)(RAND_MAX-1);
            
            // Select individuals based on these random probabilities for reproduction
            l = 1;
            r = l+1;
            if (rand_pb[c] <= cumulative_normed[l])
            {
                // Pairing individuals
                if (c<=(p_size/2))
                {
                    parent1[c]=l;
                }
                else
                {
                    parent2[c-(p_size/2)]=l;
                }
                continue;
            }
            else
            {
                while(l < p_size)
                {
                    if(cumulative_normed[l] <=rand_pb[c]<= cumulative_normed[r])
                    {
                        if (c<=(p_size/2))
                        {
                            parent1[c]=r;
                        }
                        else
                        {
                            parent2[c-(p_size/2)]=r;
                        }
                        break;
                    }
                    l++;
                    r++;
                }
            }
            
         }
        
        
        // Perform One Point Crossover
        
        for (c = 1; c <= p_size/2; c++)
        {
            //Random number for cross-over probability
            cross_it = (float)rand() / (float)(RAND_MAX-1);
            
            // If random number is less than or equal to cross over rate, perform cross over
            if(cross_it<=cross_rt)
            {
                // Random number to select cross over bit
                cross_pt=rand()%bits;
                
                for(bitno=0;bitno<bits;bitno++)
                {
                    if (bitno < cross_pt)
                    {
                        individual_next[c][bitno] = individual_curr[parent1[c]][bitno];
                        individual_next[(c+(p_size/2))][bitno] = individual_curr[parent2[c]][bitno];
                    }
                    else
                    {
                        individual_next[c][bitno] = individual_curr[parent2[c]][bitno];
                        individual_next[(c+(p_size/2))][bitno] = individual_curr[parent1[c]][bitno];
                    }
                }
            }
         
            // If not performing cross-over, simply copy the current generation individuals to next generation individuals
            else
            {
                for(bitno=0;bitno<bits;bitno++)
                {
                    individual_next[c][bitno] = individual_curr[parent1[c]][bitno];
                    individual_next[(c+(p_size/2))][bitno] = individual_curr[parent2[c]][bitno];
                }
            }
        }
        
        // Perform Mutation
        
        for (c = 1; c <= p_size; c++)
        {
            // Random number to generate mutation probability
            mut_it = (float)rand() / (float)(RAND_MAX-1);
            
            // If random number is less than mutation rate, then perform mutation
            if(mut_it<=mut_rt)
            {
                // Random number to select mutation bit
                mut_bit=rand()%bits;
   
                if(individual_next[c][mut_bit]=='1') individual_next[c][mut_bit]='0';
                else individual_next[c][mut_bit] ='1';
            }
            
        }
        
        // Copy to current generation
        for (c = 1; c <= p_size; c++)
        {
            individual_curr[c]=individual_next[c];
            x[c]=bin_to_dec(individual_next[c]);
            
            // If its last generation, track the best fit x
            if(g==term_cond-1)
            {
                fitness[c] = sin(x[c] * PI/fx_max);
                if(fitness[c]>bstfit)
                {
                    bstfit=fitness[c];
                    bstsol=x[c];
                }
            }
        }
        
        g++;
    }
    
    return bstsol;
}


// Main Function
int main(int argc, const char * argv[])
{
    
    printf("Population size: ");
    scanf("%d",&p_size);
    printf("Crossover rate: ");
    scanf("%f",&cross_rt);
    printf("Mutation rate: ");
    scanf("%f",&mut_rt);
    printf("Stop after generations: ");
    scanf("%d",&term_cond);
    
    bstsol= genetic_algo();
    
    double fx = sin(bstsol * PI/fx_max);
    
    printf("The best x is %d, f(%d) = %lf\n",bstsol,bstsol,fx);
    
    return 0;
}



/** 
int l = 1;
            int r = l+1;
            if (rand_pb <= cumulation_fitness.get(l))
            {
                // Pairing individuals
                if (c<=(p_size/2))
                {
                    parent1[c]=l;
                }
                else
                {
                    parent2[c-(p_size/2)]=l;
                }
                continue;
            }
            else
            {
                while(l < population_size.intValue())
                {
                    if(cumulation_fitness.get(l) <= rand_pb && rand_pb <= cumulation_fitness.get(r))
                    {
                        if (c<=(p_size/2))
                        {
                            parent1[c]=r;
                        }
                        else
                        {
                            parent2[c-(p_size/2)]=r;
                        }
                        break;
                    }
                    l++;
                    r++;
                }
            }

**/
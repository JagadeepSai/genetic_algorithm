// Aneri Vadera
// CSC206 - Fall 2014, Square root by power series expansion
// Sqrt.c
// Created by ANERI VADERA on 11/20/14.
//Implemented using the degree-4 Taylor polynomial:
//sqrt(x) = 1 + 1/2 (x-1) - 1/4 (x-1)^2 / 2! + 3/8 (x-1)^3/3! - 5/16 (x-1)^4 / 4! + ...
/* Reference: http://vhcc2.vhcc.edu/dsmith/geninfo/labrynth_created_fall_05/levl1_17/levl2_14/07-31-2008_____Assignment_15_and_16.htm*/

#include <stdio.h>
#include <math.h> // For checking result of this program with math function square root

// Square root function by power series expansion
double my_sqrt(double x)
{
    int i;
    double f=1.0; // Multiply result by f
    double mysqrt=1.0;  // Sqrt of x, first term '1' is initialized
    double mysqrt_prev; // Previous Sqrt
    double xi=1.0; // xi terms
    double precision = 0.0000000001; // Precision required = 0.0000000001
    double difference=0.0;
    int n=0;
    
    if(x>1)
    {
        // Divide by 4 so that 0<x<1
        while(x>1.5)
        {
            x = x/4.0;
            f=f*2.0; // Each time we divide x by 4, multiply result by 2
        }
    }
    else // 0<x<1
    {
        // Multiply by 4 so that it comes closer to 1
        while(x<=0.25)
        {
            x = x*4.0;
            f=f/2.0; // Each time we multiply x by 4, divide result by 2
        }
    }
    
    printf("x=%f\t",x);
    
    // Contimue till difference between successive terms >= precision
    i=1;
    do
    {
        xi = xi * ((3-(2 * i)) * (x-1)) / (2 * i); // Calculating xi terms using previous terms
        mysqrt+= xi; // Adding each term dx to get the result
        difference = mysqrt-mysqrt_prev; // Difference between successive terms
        if(difference<0)
            difference*=-1; // If difference<0, then change sign to make it positive
        i++;
        n++; // tracking number of terms
        mysqrt_prev = mysqrt; // For finding difference between successive terms
    }
    while(difference>=precision);
    
    printf("n=%d\t",n);
    
    printf("mysqrt=%f\t",mysqrt);
    
    printf("f=%f\n",f);
    
    return f*mysqrt; // Multiply result by f
}


int main()
{
    float number;
    double answer=0.0; // Answer: square root(number)
    
    do
    {
        printf("\nEnter Positive Number (0 to exit) : ");
        scanf("%f",&number);
        
        if(number>0.0)
        {
            answer = my_sqrt(number);
            printf("My square root of %1.06lf : %1.10lf\n",number,answer);
            printf("Lib. square root of %1.06lf : %1.10lf\n",number,sqrt(number));
        }
        
    } while(number>0.0);
    
    
    return 0;
}


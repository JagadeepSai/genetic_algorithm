#include<stdio.h>
#include<stdlib.h>
#include<math.h>


#define f(x) 4 / ( 1 + x*x )                                                                    
#define MIN 0                                                                                   
#define MAX 1                                                                                   

#define COUNT 100000                                                                             
#define MINX 0.0                                            
#define MAXX 1.0
#define MINY 0.0
#define MAXY 4.0

float randfloat();                                                                              
float randfloatmax(float,float);                                                
void count_interval();                                                          

int no_of_interval;                                                             
float trapezodial_result;                                                       
float mid_point_result;                                                         
float simpson_result;                                                           



void count_interval()
{
        int i = 1, j, flag = 1;
        float x, y, inc, sub_total = 0, total;
        while ( flag )
       {
                inc = (float)(MAX-MIN)/i;
                x = MIN;
                y = x + inc;
                total = 0.0;
                for ( j = 1; j <= i; j++ )
                {
                        total += (y-x)*(f(y)+f(x))/2;
                        x = y;
                        y = y + inc;
                }                                                                                                                                                                                                                                                              
                if ( total < sub_total )
                {
                        flag = 0;
                        no_of_interval = i-1;
                        trapezodial_result = sub_total;
                }
                        i++;
                        sub_total = total;
                }
}

int mid_point_area()
{
        int i = 1;
        float x, y, inc, total = 0;
        inc = (float)(MAX-MIN)/no_of_interval;
        x = MIN;
        y = x + inc;
        for ( i = 1; i <= no_of_interval; i++ )
        {
                total += inc * f( (x + y)/2 );                                                                                  
                x = y;
                y += inc;
        }
        mid_point_result = total;
        return(0);
}

int simpson_area()
{
        int i = 1;
        float x, y, mid, inc, total = 0;
        inc = (float)(MAX-MIN)/no_of_interval;
        x = MIN;
        y = x + inc;
        mid = (x + y)/2;
        for ( i = 1; i <= no_of_interval; i++ )
        {
                total += inc * (f(x)+f(y)+4*f(mid))/6;                                                                                                
                x = y;
                y += inc;
                mid = (x + y)/2;
        }
        simpson_result = total; 
        return (0);
}

int monte_carlo()
{
        float x, y, result, area;
        int i, in = 0;
        for ( i = 1; i <= COUNT; i++ )
        {
                x = randfloat(MINX,MAXX);
                y = randfloatmax(MINY,MAXY);
        if( y <= f(x) ) {
                in++;
        }
}
        result = (float)in/(float)COUNT*4;
        printf("\nValue of Pie according to monte carlo rule with %d random points is : %g\n",COUNT,result);
        return 0;
}

float randfloat()
{
        return (float)rand()/((float)(RAND_MAX)+(float)1);
}

float randfloatmax(float min,float max)
{
        if ( min > max ) {
                        return randfloat()*(min-max)+max;
                                } else {
                        return randfloat()*(max-min)+min;
                                                        }
}

int main()
{
        count_interval();                               
        printf("\nValue of Pie according to Trapezoidal Rule is : %f\n\n",trapezodial_result);
        printf("\n Number of intervals used is %d\n",no_of_interval);
        mid_point_area();       
        printf("\nValue of Pie according to MidPiont Rule is : %f",mid_point_result);   
        simpson_area();                                                  
        printf("\nValue of Pie according to Simpson rule is : %f\n",simpson_result);                                                                                                                                                                                
        monte_carlo();                                                                                                            
                                                                                                                
}


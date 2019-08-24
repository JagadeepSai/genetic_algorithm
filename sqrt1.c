#include<stdio.h>
#include<math.h>
int main(void)
{
    while(1)
    {
        int count = 0, n=1,j;
        double number, square_root=1.0;
        int forever = 1;
        double x, sum = 1;
        printf("\nEnter a number (0 to Exit):");
        scanf("%lf", &number);
        if(number == 0) break;
        x = number;
        while(x>2)
        {
            x = x/4;
            count++;
        }
        while(forever)
        {
            sum = (-1)*((x - 1.0)*(2*n - 3.0) / (double)(2*n))*sum;
            n++;
            square_root += sum;
            if(sum>0)
            {
                if(sum<=0.000000010) break; 
            }
            else
            {
                if((-1*sum)<=0.000000010) break;}
        }
        printf("x=%lf ,n=%d \n", x,n);
        for( j=1; j <= count; j++)
         {
              square_root *= 2;
               }
        printf("mysqrt(%lf):%lf, Lib.sqrt(%lf):%lf \n",number,square_root,number,sqrt(number));
    }//End While
}


import java.util.Scanner;
import java.text.DecimalFormat;

public class sqrt{
    double precision;

    sqrt(double precision){
        this.precision = precision;
    }

    double sqrt_f(double n){
        double ans = 1.0;
        double t = 1.0;
        double ans_pre = n;
        double i_term = 1.0;

        if(n>1){
            while(n>1.5){
                n = n / 4.0;
                t = t * 2.0;
            }
        }else{
            while(n<0.25){
                n = n * 4.0;
                t = t / 2.0;
            }
        }

        long i = 1;
        while(Math.abs(ans_pre - ans) > precision){
            ans_pre = ans;
            i_term = i_term * ((3-(2 * i)) * (n-1)) / (2 * i);
            ans+= i_term;
            i += 1;
        }
        return ans*t;
    }

    public static void main(String[] args) {

        sqrt s = new sqrt(0.0000000001);
        Scanner sc = new Scanner(System.in);   
         
        // setting stdout according to precision 
        double E = Math.log10(s.precision);
        if(E<0) E*=-1;
        String precision_s = "#.";
        while(E>0){precision_s+="0"; E--;}
        DecimalFormat numberFormat = new DecimalFormat(precision_s);
        //

        double n;
        while(true){

            System.out.println("Enter a positive number or 0 to exit: ");
            n = sc.nextDouble();
            if(n == 0.0) break;
            if(n<0)
            { 
                System.out.println("Please enter a positive number");
                continue;
            }
            System.out.println("Square root of " + n + " is " + numberFormat.format(s.sqrt_f(n)) + " with precision " + s.precision + "") ;
            System.out.println("Library square root : " + numberFormat.format(Math.sqrt(n)) + "\n");
        }
    }
    
}
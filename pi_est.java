import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

class point{
        float x;
        float y;

        public point(float x,float y){
            this.x = x;
            this.y = y;
        }
    }
    
class integral {
        float min_x ;
        float max_x ;
        BigInteger nof_itervals ;
        float incre;
        function f;
        inD inD_f;
        points_Domain_function points_Domain_f;
        points_Domain_area points_Domain_area;

        public integral(){
            min_x           = 0.0f;
            max_x           = 0.0f;
            nof_itervals    = BigInteger.ONE;
        }
        public integral(float min_x,float max_x,BigInteger nof_itervals,function f){
            this.min_x           = min_x           ;
            this.max_x           = max_x           ;
            this.nof_itervals    = nof_itervals    ;
            this.f               = f               ;
            this.incre           = (max_x - min_x)/nof_itervals.floatValue(); 
        }

        
        public interface function{
            float func(float x);
        }

        // Functions for monte carlo method of Integration : inD, points_Domain
        // function to check in D
        public interface inD{
            boolean func(float x,float y);
        }

        // function to get points from D'
        public interface points_Domain_function{
            point func();
        }
        // function to get area 
        public interface points_Domain_area{
            float func();
        }

        public void set_nof_itervals(BigInteger nof_itervals){
            this.nof_itervals = nof_itervals;
        }
        // Integration using midpoint rule
        public float midpoint_rule(){
            float x1,x2,ans = 0.0f;
            BigInteger i = BigInteger.ZERO;
            x1 = min_x;
            x2 = x1 + incre;
            
            while(i.compareTo(nof_itervals) == -1)
                {
                    ans += (x2-x1)*f.func((x1 + x2)/2);         
                    x1 = x2;
                    x2 = x1 + incre;
                    i = i.add(BigInteger.ONE);
                }
            return ans;

        }

        // Integration using trapezoidal rule
        public float trapezoidal_rule(){
            float x1,x2,ans = 0.0f;
            BigInteger i = BigInteger.ZERO;
            x1 = min_x;
            x2 = x1 + incre;
            
            while(i.compareTo(nof_itervals) == -1)
                {
                    ans += (x2-x1)*(f.func(x1)+f.func(x2))/2;      
                    x1 = x2;
                    x2 = x1 + incre;
                    i = i.add(BigInteger.ONE);
                }
            return ans;

        }

        // Integration using simpson rule
        public float simpson_rule(){
            float x1,x2,ans = 0.0f;
            BigInteger i = BigInteger.ZERO;
            x1 = min_x;
            x2 = x1 + incre;
            
            while(i.compareTo(nof_itervals) == -1)
                {
                    ans += (x2-x1)*(f.func(x1)+ 4*f.func( (x1+x2)/2 ) +f.func(x2))/6;                                                                                  
                    x1 = x2;
                    x2 = x1 + incre;
                    i = i.add(BigInteger.ONE);
                }
            return ans;
        }

        void set_D(inD inD_f,points_Domain_function points_Domain_f,points_Domain_area points_Domain_area){
            this.inD_f= inD_f;
            this.points_Domain_f = points_Domain_f;
            this.points_Domain_area = points_Domain_area;
        }
        // set the domains before using monte_carlo rule;
        public float monte_carlo_rule(BigInteger N){
            float ans = 0.0f;
            BigInteger i =  BigInteger.ZERO,count = BigInteger.ZERO;
            while(i.compareTo(N) < 1){
                point p = points_Domain_f.func();

                if(inD_f.func(p.x,p.y)){
                    count = count.add(BigInteger.ONE);

                }
                
                i = i.add(BigInteger.ONE);
                
            }
            ans = points_Domain_area.func() * (count.floatValue()/N.floatValue());
            return ans;
        }  
    }

    public class pi_est{
        final float min_x = 0;
        final float max_x = 1;
        final float min_y = 0;
        final float max_y = 4;
        BigInteger N = new BigInteger("1000000");
        BigInteger nof_itervals;

        
        public float f_pi(float x){
            return 4 / (1+ x*x);
        }

        public boolean D(float x,float y){
            boolean isIn = true;
            if(x < 0 || x > 1) isIn = false;
            else if(y < 0 || y > f_pi(x)  ) isIn = false;
            return isIn;
        }

        // Domain is quadrilateral { (0,0) (0,4) (1,4)  (1,0)} 
        public point points_Domain_func(){
            float x = ThreadLocalRandom.current().nextFloat() * (max_x - min_x) + min_x;
            if (x >= max_x)  x = Float.intBitsToFloat(Float.floatToIntBits(max_x) - 1); // rounding off (issue with using floats in ThreadLocalRandom)

            float y = ThreadLocalRandom.current().nextFloat() * (max_y - min_y) + min_y;
            if (y >= max_y)  y = Float.intBitsToFloat(Float.floatToIntBits(max_y) - 1); // rounding off (issue with using floats in ThreadLocalRandom)

            return new point(x,y);
        }
        public float points_Domain_area(){
            return (max_y - min_y) * (max_x - min_x);
        }



        public void set_intervals(){
            float ans = 0.0f;
            nof_itervals = new BigInteger("2");
            float pre_ans = 0.0f;
            integral i = new integral(min_x,max_x,nof_itervals,this::f_pi);
            do{
                pre_ans = ans;
                nof_itervals = nof_itervals.add(BigInteger.ONE);
                i.set_nof_itervals(nof_itervals);
                ans = i.trapezoidal_rule();
            }
            while(ans - pre_ans > 0.001); //TODO: Change this for setting the Maximum nof Intervals 
            nof_itervals = nof_itervals.subtract(BigInteger.ONE);
            // nof_itervals = new BigInteger("59");
        }


        public static void main(String[] args) {
            pi_est pi = new pi_est();
            pi.set_intervals();
            integral i = new integral(pi.min_x,pi.max_x,pi.nof_itervals,pi::f_pi);

            System.out.println("No.of Intervals used is " + pi.nof_itervals);
            System.out.println("Pi value using Midpoint rule: " + i.midpoint_rule());
            System.out.println("Pi value using Trapezoidal rule: " + i.trapezoidal_rule());
            System.out.println("Pi value using Simpson rule: " + i.simpson_rule());
            
            // Set the D and D' first before using monto carlo rule;
            i.set_D(pi::D, pi::points_Domain_func, pi::points_Domain_area);
            System.out.println("Pi value using Monto Carlo rule: " + i.monte_carlo_rule(pi.N) + " with " + pi.N + " random points ");
        }
       
    }
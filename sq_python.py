from decimal import getcontext,Decimal
import math

def my_sqrt(n,precision):
    ans = 1.0
    t = 1.0
    ans_pre = n
    i_term = 1.0

    if (n > 1) :
        while (n>1.5):
            n = n / 4.0
            t = t * 2.0 
            
    else :
        while (n<0.25):
            n = n * 4.0
            t = t / 2.0
    
    i = 1
    while(abs(ans_pre - ans) > precision):
        ans_pre = ans
        i_term = i_term * ((3-(2 * i)) * (n-1)) / (2 * i)
        ans+= i_term
        i += 1
    
    return ans*t



n = 0.0  # Taking square root of n
precision = 0.0000000001

while True :
    n = (raw_input('Enter a positive number or Zero to exit: '))
    if(n != ''):    
        try:
            n = float(n)
        except :
            print('Please enter a valid positive value.')
            continue
    else :
        if(n < 0): 
            print('Negative value entered. Please enter a positive value.')
        continue

    if(n == 0) :
        break
        
    # print(getcontext())
    ans = Decimal(my_sqrt(n,precision)).quantize(Decimal(str(precision)))
    pre = Decimal(precision).quantize(Decimal(str(precision)))
    nnew = Decimal(n).quantize(Decimal(str(precision)))

    # print('Square root of %f is %f ,precision: %f' %(Decimal.from_float((n),my_sqrt(n,precision),precision) )
    print('Square root of ' + str(nnew) + ' is ' + str(ans) + ' with precision: '+  str(pre) )

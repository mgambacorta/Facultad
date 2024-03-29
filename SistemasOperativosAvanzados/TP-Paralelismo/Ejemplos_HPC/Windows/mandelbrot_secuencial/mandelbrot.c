/**************************************************************************************************
 *                              Fractal MandelBrot
 *  
 *  https://es.wikipedia.org/wiki/Fractal
 * 
 *************************************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>


#include <complex>
 
using namespace std;

extern "C" {
#include "comun.h"
}
	
typedef complex<double> ccomplex;
 
//--------------------------------------------------------------------------------------------------

int MandelbrotCalculate(ccomplex c, int maxiter)
{
   // iterates z = z + c until |z| >= 2 or maxiter is reached,
   // returns the number of iterations.
   // + INFO https://es.wikipedia.org/wiki/Fractal
   ccomplex z = c;
   int n=0;
   for(; n<maxiter; ++n)
   {
       if( abs(z) >= 2.0) break;
       z = z*z*z + (c*c);
   }
   return n;
}

//--------------------------------------------------------------------------------------------------

int main()
{
   const int width = 80, height = 80, num_pixels = width*height;
   
   //const ccomplex center(-0.7, 0), span(2.7, -(4/3.0)*2.7*height/width);
   const ccomplex center(-0.7, 0), span(2.7, -(7/3.0)*2.7*height/width);
   const ccomplex begin = center-span/2.0;//, end = center+span/2.0;
   const int maxiter = 100000;
   
   tiempoInicial( "MANDELBROT (Secuencial)" );
   
   for(int pix=0; pix<num_pixels; ++pix)
   {
       const int x = pix%width, y = pix/width;
       
       ccomplex c = begin + ccomplex(x * span.real() / (width +1.0),
                                     y * span.imag() / (height+1.0));
       
       int n = MandelbrotCalculate(c, maxiter);
       if(n == maxiter) n = 0;
       
       {
         char c = ' ';
         if(n > 0)
         {
             static const char charset[] = ".,c8M@jawrpogOQEPGJ";
             c = charset[n % (sizeof(charset)-1)];
         }
         putchar(c);
         if(x+1 == width) puts("|");
      }
   }
     
  tiempoFinal();
}
 
//--------------------------------------------------------------------------------------------------

package com.programacion.paralela;

public class FractalCpu {

    public static int[] pixelBuffer;

    public FractalCpu() {
        pixelBuffer = new int[FractalParams.ancho*FractalParams.alto];
    }

    static int acotado_2(double x, double y){
    /*
    dados: c, z0
    Zn+1 = Zn^2 + c
    */

        int iter = 1;

        double zr = x;
        double zi = y;

        while (iter < FractalParams.max_iteraciones && (zr*zr+zi*zi) < 4.0)
        {
            double dr = zr*zr-zi*zi + FractalParams.real;
            double di =  2.0*zr*zi + FractalParams.imag;

            zr = dr;
            zi = di;

            iter ++;
        }

        if(iter < FractalParams.max_iteraciones){
            // nomras > 2
            // return 0xFF0000FF; // rojo
            int index = iter % FractalParams.PALETTE_SIZE;
            return FractalParams.color_ramp[index];
        }
        // los bits esta alreves en cuanto a los colores
        return 0xFF000000; // negro
    }

    public static void julia_serial_2(double x_min, double y_min, double x_max, double y_max, int width, int height){

        double dx = (x_max - x_min) / width;
        double dy = (y_max - y_min) / height;

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){

                // z = x+yi = (x,y)
                double x = x_min + i * dx;
                double y = y_max - j * dy;

                //std::complex<double> z(x, y);

                // similar al var
                var color = acotado_2(x, y);

                //index j*w + i
                pixelBuffer[j * width + i] = color;
            }
        }

    }


}

package paralela;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FractalHilos {

    public int[] pixel_buffer;

    public FractalHilos() {
        pixel_buffer = new int[FractalParams.WIDTH * FractalParams.HEIGHT];
    }

    int acotado_2(double x, double y) {

        int iter = 1;

        double zr = x;
        double zi = y;

        while (iter < FractalParams.max_iteraciones &&
               (zr * zr + zi * zi) < 4.0) {

            double dr = zr * zr - zi * zi + FractalParams.c_real;
            double di = 2.0 * zr * zi + FractalParams.c_imag;

            zr = dr;
            zi = di;

            iter++;
        }

        if (iter < FractalParams.max_iteraciones) {
            int index = iter % FractalParams.PALETTE_SIZE;
            return FractalParams.color_ramp2[index];
        }

        return 0xFF000000;
    }

    public void julia_paralela(
            double x_min,
            double y_min,
            double x_max,
            double y_max,
            int width,
            int height) {

        double dx = (x_max - x_min) / width;
        double dy = (y_max - y_min) / height;

        // Número de núcleos del CPU
        int numHilos = Runtime.getRuntime().availableProcessors();
        int limHilos = numHilos;
        System.out.println("NUMHILOS "+limHilos);

        ExecutorService executor =
                Executors.newFixedThreadPool(limHilos);

        // División del trabajo por filas
        int bloqueFilas = height / limHilos;
        System.out.println(bloqueFilas);

        // Recorrer los bloques
        for (int h = 0; h < limHilos; h++) {

            int inicio = h * bloqueFilas;
            System.out.println("HILO - Bloque:" + h);

            int fin;

            // Último hilo toma el resto
            if (h == limHilos - 1) {
                fin = height;
            } else {
                fin = inicio + bloqueFilas;
            }

            executor.execute(() -> {

                for (int j = inicio; j < fin; j++) {

                    for (int i = 0; i < width; i++) {

                        double x = x_min + i * dx;
                        double y = y_max - j * dy;

                        int color = acotado_2(x, y);

                        pixel_buffer[j * width + i] = color;
                    }
                }

            });
        }

        executor.shutdown(); //esperar q terminen las tareas

        try {
            //
            executor.awaitTermination(1, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package com.programacion.paralela;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class FractalMain {
    private int textureId;
    // The window handle
    private long window;

    FractalCpu fractal;
    FPSCounter fpsCounter;

    public FractalMain() {
        fractal = new FractalCpu();
        fpsCounter = new FPSCounter();
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();


        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(FractalParams.ancho, FractalParams.alto, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            if ( key == GLFW_KEY_UP && action == GLFW_RELEASE ){
                FractalParams.max_iteraciones += 10;
            }

            if(key==GLFW_KEY_DOWN && action == GLFW_RELEASE ){
                FractalParams.max_iteraciones -= 10;
                if(FractalParams.max_iteraciones<0)
                    FractalParams.max_iteraciones = 10;
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,
                (vidmode.width()-FractalParams.ancho)/2,
                (vidmode.height()-FractalParams.alto)/2);



        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        GL.createCapabilities();
        GL.createCapabilitiesWGL();

        //--VERSION DE OPNEGL
        String version = GL11.glGetString(GL11.GL_VERSION);
        String vendor = GL11.glGetString(GL11.GL_VENDOR);
        String renderer = GL11.glGetString(GL11.GL_RENDERER);

        System.out.println("OpenGL version: " + version);
        System.out.println("OpenGL vendor: " + vendor);
        System.out.println("OpenGL renderer: " + renderer);

        // conf. proyecion
        GL11.glEnable(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-1,1,-1,1,-1,1);

        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glLoadIdentity();

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        setupTexture();
    }

    private void setupTexture() {
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGBA,
                FractalParams.ancho, FractalParams.alto, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, NULL
        );


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void loop() {

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            paint();

            glfwSwapBuffers(window);


            glfwPollEvents();
        }
    }

    private void paint() {

        FPSCounter.update();
        System.out.println("FPS: " + FPSCounter.update());
        FractalCpu.julia_serial_2(FractalParams.x_min, FractalParams.y_min, FractalParams.x_max, FractalParams.y_max, FractalParams.ancho, FractalParams.alto);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);

        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_RGBA,
                FractalParams.ancho, FractalParams.alto, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, FractalCpu.pixelBuffer
        );

        glBegin(GL_QUADS);
        {
            glTexCoord2d(0.0f, 0.0f);
            glVertex2d(-1, -1);

            glTexCoord2d(0.0f, 1.0f);
            glVertex2d(-1, 1);

            glTexCoord2d(1.0f, 1.0f);
            glVertex2d(1, 1);

            glTexCoord2d(1.0f, 0.0f);
            glVertex2d(1, -1);
        }
        glEnd();
    }

    public static void main(String[] args) {
        new FractalMain().run();
    }

}

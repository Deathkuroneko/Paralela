#include <iostream>
#include <fmt/core.h>

int main(){
    int valor = 10; 
    std::printf("Hola mundo %f\n", valor);

    //fmt infiere y reconoce el tipo de dato
    fmt::println("Hola mundo con fmt: {}", valor);

    return 0;
}


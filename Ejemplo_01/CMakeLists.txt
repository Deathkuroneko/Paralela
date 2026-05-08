cmake_minimum_required(VERSION 3.16)

project(Ejemplo_01)

find_package(fmt CONFIG REQUIRED)

find_package(SFML COMPONENTS System Graphics Window CONFIG REQUIRED)

add_executable(ejemplo_01 main.cpp)

target_link_libraries(ejemplo_01 PRIVATE fmt::fmt SFML::System SFML::Graphics SFML::Window )
cmake_minimum_required(VERSION 3.16)

project(07cuda)

#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
enable_language(CXX CUDA)

find_package(fmt CONFIG REQUIRED)

find_package(SFML COMPONENTS Audio Network System Graphics Window CONFIG REQUIRED)

add_executable(07cuda main.cpp kernel.cu)

TARGET_INCLUDE_DIRECTORIES(07cuda PRIVATE ${CMAKE_CUDA_TOOLKIT_INCLUDE_DIRECTORIES})

target_link_libraries(07cuda PRIVATE 
fmt::fmt 
)
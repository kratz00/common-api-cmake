cmake_minimum_required(VERSION 2.6)

project(CommonAPI-CMake)

set(CMAKE_MODULES_DESTINATION_DIR lib/cmake)

add_subdirectory(common)
add_subdirectory(dbus)
add_subdirectory(someip)
add_subdirectory(qt)

cmake_minimum_required(VERSION 2.6)

project(CommonAPI-CMake)

set(CMAKE_MODULES_DESTINATION_DIR lib/cmake)

# Dependencies
FIND_PACKAGE(PkgConfig REQUIRED)

set(JAR_DESTINATION_PATH share/CommonAPICodeGen)

add_subdirectory(common)
add_subdirectory(qt)
add_subdirectory(dbus)
add_subdirectory(someip)

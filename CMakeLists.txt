cmake_minimum_required(VERSION 2.6)

project(CommonAPI-CMake)

set(CMAKE_MODULES_DESTINATION_DIR lib/cmake)

# Dependencies
FIND_PACKAGE(PkgConfig REQUIRED)

set(JAR_DESTINATION_PATH share/CommonAPICodeGen)

add_subdirectory(common)

pkg_check_modules(COMMON_API_DBUS CommonAPI-DBus)
if(COMMON_API_DBUS_FOUND)
endif()

add_subdirectory(dbus)

add_subdirectory(someip)
add_subdirectory(qt)

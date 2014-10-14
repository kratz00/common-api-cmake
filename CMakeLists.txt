cmake_minimum_required(VERSION 2.6)

project(CommonAPI-CMake)

set(CMAKE_MODULES_DESTINATION_DIR lib/cmake)

# Dependencies
FIND_PACKAGE(PkgConfig REQUIRED)

set(JAR_DESTINATION_PATH share/CommonAPICodeGen)

add_subdirectory(common)
add_subdirectory(qt)

pkg_check_modules(COMMON_API_DBUS CommonAPI-DBus)
if(COMMON_API_DBUS_FOUND)
    add_subdirectory(dbus)
endif()

pkg_check_modules(COMMON_API_SOMEIP CommonAPI-SomeIP)
if(COMMON_API_SOMEIP_FOUND)
    add_subdirectory(someip)
endif()

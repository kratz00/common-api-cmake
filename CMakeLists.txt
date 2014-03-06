cmake_minimum_required(VERSION 2.6)

set(PACKAGE CommonAPICodeGen)
PROJECT(${PACKAGE})

SET( ${PROJECT_NAME}_MAJOR_VERSION 0 )
SET( ${PROJECT_NAME}_MINOR_VERSION 9 )
SET( ${PROJECT_NAME}_PATCH_LEVEL 1 )

set(JAR_DESTINATION_PATH share/commonAPI)

set(VERSION ${${PROJECT_NAME}_MAJOR_VERSION}.${${PROJECT_NAME}_MINOR_VERSION}.${${PROJECT_NAME}_PATCH_LEVEL})

set(CMAKE_MODULES_DESTINATION_DIR lib/cmake)

message("CMake modules directory :" ${CMAKE_MODULES_DESTINATION_DIR})

install(FILES CommonAPICodeGenConfig.cmake DESTINATION ${CMAKE_MODULES_DESTINATION_DIR}/CommonAPICodeGen)

install(FILES codegen.jar DESTINATION ${JAR_DESTINATION_PATH})
install(FILES eclipse.jar DESTINATION ${JAR_DESTINATION_PATH})

configure_file(commonAPICodeGen.in commonAPICodeGen @ONLY)
install(FILES ${CMAKE_CURRENT_BINARY_DIR}/commonAPICodeGen DESTINATION bin
	PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE
)

add_subdirectory(src/commonapi/cmdline/dbus)

if (NOT DISABLE_SOMEIP)
	add_subdirectory(src/commonapi/cmdline/someip)
endif()

if (NOT DISABLE_QT)
	add_subdirectory(src/commonapi/cmdline/qt)
endif()


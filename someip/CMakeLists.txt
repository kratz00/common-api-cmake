
pkg_check_modules(COMMON_API_SOMEIP CommonAPI-SomeIP)
if(COMMON_API_SOMEIP_FOUND)
endif()

install(FILES CommonAPISomeIPCodeGenConfig.cmake DESTINATION ${CMAKE_MODULES_DESTINATION_DIR}/CommonAPISomeIPCodeGen)

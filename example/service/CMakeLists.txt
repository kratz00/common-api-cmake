cmake_minimum_required(VERSION 2.6)

PROJECT(test-commonapi-cmake)

# Dependencies
FIND_PACKAGE(CommonAPISomeIPCodeGen REQUIRED)

# Generate and install a library containing SomeIP stubs and proxies for our service
add_commonapi_someip_service(COMMONAPI_GENERATED ${CMAKE_CURRENT_SOURCE_DIR}/Climate-someip.fdepl ${CMAKE_CURRENT_SOURCE_DIR}/Climate.fidl climate/Climate)

add_executable(service service.cpp)
install(TARGETS service DESTINATION bin)
target_link_libraries(service ${COMMONAPI_GENERATED_LIBRARIES})

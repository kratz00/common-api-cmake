
pkg_check_modules(COMMON_API_SOMEIP CommonAPI-SomeIP)
if(COMMON_API_SOMEIP_FOUND)
	add_definitions(${COMMON_API_SOMEIP_CFLAGS})
	link_directories(${COMMON_API_SOMEIP_LIBRARY_DIRS})
endif()

# Generates and installs a library containing a SomeIP stub and a proxy for the given interface
macro(install_commonapi_someip_backend LIBRARY_NAME variableName deploymentFile idlFile interface)

	set(GENERATED_FILES
		${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION}/${interface}SomeIPStubAdapter.cpp
		${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION}/${interface}SomeIPProxy.cpp
		${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION}/${interface}.cpp
		${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION}/${interface}StubDefault.cpp
	)

	add_library(${LIBRARY_NAME}_someip SHARED
		${GENERATED_FILES}
	)

	set_target_properties(${LIBRARY_NAME}_someip PROPERTIES VERSION 1 SOVERSION 1)
	
	# Once installed, the library will be called ${LIBRARY_NAME}_Backend
	set_target_properties(${LIBRARY_NAME}_someip PROPERTIES OUTPUT_NAME ${LIBRARY_NAME}_Backend )

	TARGET_LINK_LIBRARIES( ${LIBRARY_NAME}_someip
		${COMMON_API_SOMEIP_LIBRARIES}
	)

	set(GENERATORS core someip)
	add_generated_files_command("${GENERATED_FILES}" ${deploymentFile} ${idlFile} "${GENERATORS}")

	include_directories(${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION})

	get_library_name(BASE___ ${interface})
	install( TARGETS ${BASE___}_someip DESTINATION lib)
	install( DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION} DESTINATION include/CommonAPIServices)

endmacro()

# Generates and installs a library containing a Some/IP CommonAPI stub and a proxy for the given interface
#macro(add_commonapi_someip_service variableName deploymentFile idlFile interface)

#	get_library_name(BASE___ ${interface})
#	set(${variableName}_LIBRARIES ${${variableName}_LIBRARIES} ${BASE___}_someip)
#	install_commonapi_someip_backend(${BASE___} ${deploymentFile} ${idlFile} ${interface})
#	install_franca_idl(${interface} ${deploymentFile} ${deploymentFile} ${idlFile})

#endmacro()

macro(add_commonapi_someip_service variableName deploymentFile idlFile interface)

	get_library_name(BASE___ ${interface})
	set(BACKEND someip)
	set(${variableName}_LIBRARIES ${${variableName}_LIBRARIES} ${BASE___}_${BACKEND})
	install_commonapi_someip_backend(${BASE___} ${variableName} ${deploymentFile} ${idlFile} ${interface})
	install_franca_idl(${interface} ${deploymentFile} ${deploymentFile} ${idlFile})

endmacro()



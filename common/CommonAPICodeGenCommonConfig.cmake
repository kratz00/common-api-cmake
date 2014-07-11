
set(COMMONAPI_CODEGEN_COMMAND_LINE commonAPICodeGen)
set(COMMONAPI_GENERATED_FILES_LOCATION FrancaGen)

find_package(PkgConfig REQUIRED)

pkg_check_modules(COMMON_API REQUIRED CommonAPI)
add_definitions(${COMMON_API_CFLAGS})
link_directories(${COMMON_API_LIBRARY_DIRS})

set(FRANCA_IDLS_LOCATION ${CMAKE_INSTALL_PREFIX}/include/franca_idls)
set(SERVICE_HEADERS_INSTALLATION_DESTINATION include/CommonAPIServices)
set(SERVICE_HEADERS_INSTALLED_LOCATION ${CMAKE_INSTALL_PREFIX}/${SERVICE_HEADERS_INSTALLATION_DESTINATION})


macro(get_library_name variableName interface)
	set(LIBRARY_NAME ${interface}_CommonAPIGenerated)
	STRING(REGEX REPLACE "/" "_" LIBRARY_NAME ${LIBRARY_NAME})
	set ( ${variableName} ${LIBRARY_NAME})
	message ("Library name : ${${variableName}} ")
endmacro()


macro(add_generated_files_command GENERATED_FILES deploymentFile idlFile codegenerators)
	message("Command : ${COMMONAPI_CODEGEN_COMMAND_LINE} -f ${deploymentFile} -o ${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION} ${codegenerators}")
	add_custom_command(
		OUTPUT ${GENERATED_FILES}
		COMMAND ${COMMONAPI_CODEGEN_COMMAND_LINE} -f ${deploymentFile} -o ${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION} ${codegenerators}
		DEPENDS ${deploymentFile} ${idlFile}
	)
	include_directories(${CMAKE_CURRENT_BINARY_DIR}/${COMMONAPI_GENERATED_FILES_LOCATION})
endmacro()


macro(install_franca_idl interfaceName deploymentFile deploymentFileDestinationName idlFile)
	install(FILES ${idlFile} DESTINATION ${FRANCA_IDLS_LOCATION}/${interfaceName}/.. )
	message("configure file ${deploymentFile} ${CMAKE_CURRENT_BINARY_DIR}/${deploymentFileDestinationName}")
	configure_file(${deploymentFile} ${CMAKE_CURRENT_BINARY_DIR}/${deploymentFileDestinationName} @ONLY)
	install(FILES ${CMAKE_CURRENT_BINARY_DIR}/${deploymentFileDestinationName} DESTINATION ${FRANCA_IDLS_LOCATION}/${interfaceName}/.. )
endmacro()


# Use a previously generated CommonAPI proxy/stub library
macro(use_commonapi_service variableName interface)

	get_library_name(BASE___ ${interface})
	set(${variableName}_LIBRARIES ${BASE___}_Backend)
	include_directories(${SERVICE_HEADERS_INSTALLED_LOCATION})

endmacro()

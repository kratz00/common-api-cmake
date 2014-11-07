
pkg_check_modules(COMMON_API_DBUS CommonAPI-DBus)

set(BUILD_CODE_GEN ${ENABLE_DBUS_CODE_GEN})

if(NOT BUILD_CODE_GEN)
	set(BUILD_CODE_GEN ${COMMON_API_DBUS_FOUND})
endif()

if(BUILD_CODE_GEN)

	message("DBus CommonAPI code generator is going to be installed. The DBus CommonAPI must have been built and installed in Maven's repository")

    add_custom_target(
    	dbus-codegen ALL
    	COMMAND mvn ${MAVEN_OPTIONS} -f ${CMAKE_CURRENT_SOURCE_DIR}/pom.xml package
    )

    # Install dependencies
    install(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/target/dependencies/ DESTINATION ${JAR_DESTINATION_PATH})

endif()

install(FILES CommonAPIDBusCodeGenConfig.cmake DESTINATION ${CMAKE_MODULES_DESTINATION_DIR}/CommonAPIDBusCodeGen)

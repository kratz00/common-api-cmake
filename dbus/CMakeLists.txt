
if(NOT DISABLE_CODE_GEN)
    add_custom_target(
    	dbus-codegen ALL
    	COMMAND mvn ${MAVEN_OPTIONS} -f ${CMAKE_CURRENT_SOURCE_DIR}/pom.xml package
    )

    # Install dependencies
    install(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/target/dependencies/ DESTINATION ${JAR_DESTINATION_PATH})

endif()

install(FILES CommonAPIDBusCodeGenConfig.cmake DESTINATION ${CMAKE_MODULES_DESTINATION_DIR}/CommonAPIDBusCodeGen)


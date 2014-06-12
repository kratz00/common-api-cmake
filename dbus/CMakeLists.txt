
add_custom_target(
	dbus-codegen ALL
	COMMAND mvn ${MAVEN_OPTIONS} -f ${CMAKE_CURRENT_SOURCE_DIR}/pom.xml package
)

install(FILES CommonAPIDBusCodeGenConfig.cmake DESTINATION ${CMAKE_MODULES_DESTINATION_DIR}/CommonAPIDBusCodeGen)

# Install dependencies
install(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/target/dependencies/ DESTINATION ${JAR_DESTINATION_PATH})

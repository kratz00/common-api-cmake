find_package(CommonAPICodeGenCommon REQUIRED)

find_package(CommonAPISomeIPCodeGen)
find_package(CommonAPIDBusCodeGen)
find_package(PkgConfig)

# Generates and installs a library containing a CommonAPI stub and a proxy for the given interface
# By default, the DBUS backend is used unless the "DEFAULT_COMMONAPI_BACKEND" variable is set to "someip"
macro(add_commonapi_service variableName deploymentFilePrefix idlFile interface)

	get_library_name(BASE___ ${interface})

	set(BACKEND dbus)

	if(${DEFAULT_COMMONAPI_BACKEND} MATCHES "someip")
		set(BACKEND someip)
	endif()

	message("Using ${BACKEND} backend")
	set(deploymentFile ${deploymentFilePrefix}-${BACKEND}.fdepl)
	set(${variableName}_LIBRARIES ${${variableName}_LIBRARIES} ${BASE___}_${BACKEND})

	if(${BACKEND} MATCHES "someip")
		install_commonapi_someip_backend(${BASE___} ${variableName} ${deploymentFile} ${idlFile} ${interface})
	else()
		install_commonapi_dbus_backend(${BASE___} ${variableName} ${deploymentFile} ${idlFile} ${interface})
	endif()

	install_franca_idl(${interface} ${deploymentFile} ${deploymentFilePrefix}.fdepl ${idlFile})

endmacro()

# Generates a CommonAPI proxy for the given interface
#macro(add_commonapi_proxy variableName interface)
#    use_commonapi_proxy(${variableName} ${interface})
#endmacro()


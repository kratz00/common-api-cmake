#include <memory>
#include "climate/ClimateStubDefault.h"
#include <CommonAPI/CommonAPI.h>

int main(int argc, const char** argv) {

	auto runtime = CommonAPI::Runtime::load();
	auto m_mainLoopContext = runtime->getNewMainLoopContext();

	assert(runtime.get() != nullptr);

	auto factory = runtime->createFactory();
	auto m_servicePublisher = runtime->getServicePublisher();
	auto service = std::make_shared<climate::ClimateStubDefault>();
	auto returnCode = m_servicePublisher->registerService(service, "", "", "", factory);

	if (returnCode) {
		printf("Registration successful\n");
		sleep(200);
	}
	else {
		printf("Registration failed\n");
		return -1;
	}

	return 0;
}

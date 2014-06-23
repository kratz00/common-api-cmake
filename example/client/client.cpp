#include <memory>
#include "climate/ClimateProxy.h"

int main(int argc, const char** argv) {
	std::make_shared<climate::ClimateProxy>();
	return 0;
}

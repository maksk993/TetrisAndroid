#pragma once

#include <cstdint>

class FlexibleSizes {
public:
	static size_t getSize(size_t originalSize, size_t divider);
};
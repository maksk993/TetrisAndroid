#pragma once

#include <jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include "stb_image.h"
#include <utility>
#include <string>
#include <vector>

class MyAssetManager {
public:
    static MyAssetManager& getAssetManager();
    void init(AAssetManager* mgr);
    std::string readTXT(const std::string& path);
    std::pair<unsigned char*, std::vector<int>> readPNG(const std::string& path);

private:
    AAssetManager* m_mgr = nullptr;

    MyAssetManager() = default;
    MyAssetManager(const MyAssetManager&) = delete;
    MyAssetManager& operator=(const MyAssetManager&) = delete;
};
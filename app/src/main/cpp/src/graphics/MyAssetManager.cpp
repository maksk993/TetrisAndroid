#include "MyAssetManager.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_example_tetrisandroid_GameActivity_cppAssetManagerInit(JNIEnv *env, jobject thiz,
                                                             jobject asset_manager) {
    MyAssetManager& myAssetManager = MyAssetManager::getAssetManager();
    AAssetManager* mgr = AAssetManager_fromJava(env, asset_manager);
    myAssetManager.init(mgr);
}

void MyAssetManager::init(AAssetManager* mgr) {
    m_mgr = mgr;
}

MyAssetManager &MyAssetManager::getAssetManager() {
    static MyAssetManager myAssetManager;
    return myAssetManager;
}

std::string MyAssetManager::readTXT(const std::string &path) {
    AAsset* asset = AAssetManager_open(m_mgr, path.c_str(), AASSET_MODE_BUFFER);
    std::string contentStr = "";
    if (asset != nullptr) {
        off_t sz = AAsset_getLength(asset);
        char* content = new char [sz + 1];
        AAsset_read(asset, content,sz);
        content[sz] = '\0';
        contentStr = std::string(content);
        delete[] content;
        AAsset_close(asset);
    }
    return contentStr;
}

std::pair<unsigned char*, std::vector<int>> MyAssetManager::readPNG(const std::string &path) {
    AAsset* asset = AAssetManager_open(m_mgr, path.c_str(), AASSET_MODE_BUFFER);
    std::vector<int> numbers(3);
    if (asset != nullptr) {
        off_t sz = AAsset_getLength(asset);
        unsigned char* content = new unsigned char [sz];
        AAsset_read(asset, content,sz);

        int channels = 0;
        int width = 0;
        int height = 0;
        stbi_set_flip_vertically_on_load(true);
        unsigned char* pixels = stbi_load_from_memory(content, sz, &width, &height, &channels, 0);

        delete[] content;
        AAsset_close(asset);

        numbers[0] = channels;
        numbers[1] = width;
        numbers[2] = height;
        return std::make_pair(pixels, numbers);
    }
    return std::make_pair(nullptr, numbers);
}

#pragma once

#include <jni.h>
#include <string>
#include <android/sharedmem.h>

class DataManager {
public:
    static DataManager& getDataManager();
    void setHighScore(int highScore);
    int getHighScore() { return m_highScore; }

private:
    DataManager() = default;
    DataManager(const DataManager&) = delete;
    DataManager& operator=(const DataManager&) = delete;

    int m_highScore;
};
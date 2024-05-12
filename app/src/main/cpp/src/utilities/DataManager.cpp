#include "DataManager.h"

DataManager &DataManager::getDataManager() {
    static DataManager dataManager;
    return dataManager;
}

void DataManager::setHighScore(int highScore) {
    m_highScore = highScore;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_tetrisandroid_data_DataRepository_cppSetStartHighScore(JNIEnv *env, jobject thiz,
                                                                        jint high_score) {
    DataManager& mgr = DataManager::getDataManager();
    mgr.setHighScore(high_score);
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_tetrisandroid_data_DataRepository_cppGetCurrentHighScore(JNIEnv *env, jobject thiz) {
    DataManager& mgr = DataManager::getDataManager();
    return mgr.getHighScore();
}
#include <jni.h>

#define STB_IMAGE_IMPLEMENTATION
#define STBI_ONLY_PNG

#include "game/Game.hpp"

std::unique_ptr<Game> pGame;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_tetrisandroid_MyRenderer_startGame(JNIEnv *env, jobject thiz,
                                                      jint screen_width, jint screen_height) {
    pGame = std::make_unique<Game>(screen_width, screen_height);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_tetrisandroid_MyRenderer_loadGame(JNIEnv *env, jobject thiz) {
    pGame->prepareToRender();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_tetrisandroid_MyRenderer_run(JNIEnv *env, jobject thiz) {
    pGame->run();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_tetrisandroid_MainActivity_handleTouch(JNIEnv *env, jobject thiz, jint code) {
    pGame->handleTouch(code);
}
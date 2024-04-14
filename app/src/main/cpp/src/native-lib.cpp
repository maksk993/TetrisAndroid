#include <jni.h>

#define STB_IMAGE_IMPLEMENTATION
#define STBI_ONLY_PNG

#include "game/Game.hpp"

std::unique_ptr<Game> pGame;

extern "C" {
    JNIEXPORT void JNICALL
    Java_com_example_tetrisandroid_renderer_MyRenderer_cppStartGame(JNIEnv *env, jobject thiz, jint screen_width,
                                                      jint screen_height,
                                                      jfloat start_speed, jint speed_level,
                                                      jint increase_coef) {
        pGame = std::make_unique<Game>(screen_width, screen_height,
                                       start_speed, speed_level, increase_coef);
    }

    JNIEXPORT void JNICALL
    Java_com_example_tetrisandroid_renderer_MyRenderer_cppRun(JNIEnv *env, jobject thiz) {
        pGame->run();
    }

    JNIEXPORT void JNICALL
    Java_com_example_tetrisandroid_ui_GameActivity_cppHandleTouch(JNIEnv *env, jobject thiz,
                                                                  jint code) {
        pGame->handleTouch(code);
    }

    JNIEXPORT jboolean JNICALL
    Java_com_example_tetrisandroid_ui_GameActivity_cppIsGamePaused(JNIEnv *env, jobject thiz) {
        return pGame->isGamePaused();
    }

    JNIEXPORT void JNICALL
    Java_com_example_tetrisandroid_ui_GameActivity_cppSetGamePaused(JNIEnv *env, jobject thiz) {
        pGame->setGamePaused();
    }
}
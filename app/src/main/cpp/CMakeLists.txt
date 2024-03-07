cmake_minimum_required(VERSION 3.22.1)

project("tetrisandroid")

set(CMAKE_CXX_STANDARD 14)

find_library(EGL-lib EGL)
find_library(GLESv3-lib GLESv3)

add_library(${CMAKE_PROJECT_NAME} SHARED
        src/native-lib.cpp
        src/game/Game.cpp
        src/graphics/ShaderProgram.cpp
        src/graphics/Texture.cpp
        src/graphics/VBO.cpp
        src/graphics/Sprite.cpp
        src/graphics/Renderer.cpp
        src/graphics/MyAssetManager.cpp

        src/objects/FigureManager.cpp
        src/objects/drawable/NumerableObject.cpp
        src/objects/drawable/Speed.cpp
        src/objects/drawable/Score.cpp
        src/objects/drawable/Field.cpp
        src/objects/drawable/Text.cpp

        src/utilities/Clock.cpp
        src/utilities/DataManager.cpp
        )
add_subdirectory(external/glm)

target_link_libraries(${CMAKE_PROJECT_NAME} ${EGL-lib} ${GLESv3-lib} glm android log)
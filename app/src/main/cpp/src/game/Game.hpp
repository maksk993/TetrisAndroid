#pragma once

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <unordered_map>

#include "../objects/drawable/Score.hpp"
#include "../objects/drawable/Speed.hpp"
#include "../objects/drawable/Field.hpp"
#include "../objects/drawable/Text.hpp"
#include "../objects/FigureManager.hpp"

#include "../graphics/Texture.hpp"
#include "../graphics/ShaderProgram.hpp"
#include "../graphics/Sprite.hpp"

#include "../utilities/Clock.hpp"
#include "../utilities/DataManager.h"

class Game {
public:
    Game(size_t screenWidth, size_t screenHeight);
    void run();
    void prepareToRender();
    void handleTouch(int code);
    bool isGamePaused();
    void setGamePaused();

private:
    size_t m_screenWidth;
    size_t m_screenHeight;

    static const size_t FIELD_WIDTH = 10;
    static const size_t FIELD_HEIGHT = 20;
    static const size_t MINISCREEN_WIDTH = 4;
    static const size_t MINISCREEN_HEIGHT = 2;

    std::unordered_map<std::string, std::shared_ptr<ShaderProgram>> shaderProgramMap;

    std::shared_ptr<Texture> scoreTextTex;
    std::shared_ptr<Texture> highScoreTextTex;
    std::shared_ptr<Texture> speedTextTex;
    std::unordered_map<int, std::shared_ptr<Texture>> cellTexMap;
    std::unordered_map<int, std::shared_ptr<Texture>> numsTexMap;

    FigureManager m_figureManager;
    Field m_field;
    MiniScreen m_miniScreen;
    Text m_scoreText;
    Text m_highScoreText;
    Text m_speedText;
    Score m_score;
    HighScore m_highScore;
    Speed m_speed;

    Clock clock;
    int nextColor = 0;
    int nextFigure = 0;
    float timer = 0.f;
    float time = 0.f;
    float figureFallDelay;
    float lineDeletionDelay = 50.f;
    size_t fallenFiguresCounter;
    bool pause;
    bool shouldLineDeletionAnimationStart = false;

    enum class EGameState { figureIsFalling, lineIsDeleting };
    EGameState m_currentGameState;

    DataManager& dataManager = DataManager::getDataManager();
    enum class Buttons {LEFT, DOWN, RIGHT, UP, ROTATE, RESET, PAUSE};

    void loadResources();
    void start();
    void update();
    void spawnNewFigureAndGenerateNext();
    void showGame();
    void increaseSpeed();
};
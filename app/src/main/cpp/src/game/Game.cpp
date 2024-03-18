#include "Game.hpp"

Game::Game(size_t screenWidth, size_t screenHeight, float startSpeed, int speedLevel, int increaseCoef)
: m_screenWidth(screenWidth), m_screenHeight(screenHeight),
m_startSpeed(startSpeed), m_startSpeedLevel(speedLevel), m_speedIncreaseCoef(increaseCoef)
{
    srand(std::time(0));
    loadResources();
    start();
    prepareToRender();
}

void Game::start() {
    m_field.clear();
    m_miniScreen.clear();
    m_figureManager.setShouldNewFigureBeSpawned(true);
    m_score.setValue(0);
    m_speed.setValue(1);
    figureFallDelay = m_startSpeed;
    m_speed.setValue(m_startSpeedLevel);
    fallenFiguresCounter = 0;
    nextColor = m_figureManager.genNextColor();
    nextFigure = m_figureManager.genNextFigure(nextColor);
    m_currentGameState = EGameState::figureIsFalling;
}

void Game::prepareToRender() {
    shaderProgramMap["sprite"]->use();
    shaderProgramMap["sprite"]->setInt("tex", 0);

    glm::mat4 projectionMatrix = glm::ortho(0.f, static_cast<float>(m_screenWidth), 0.f, static_cast<float>(m_screenHeight),
                                            -1.f, 1.f);
    shaderProgramMap["sprite"]->setMatrix4("projectionMat", projectionMatrix);

    Renderer::clearColor(0.2f, 0.2f, 0.2f, 1.0f);
}

void Game::run() {
    update();
    showGame();
}

void Game::handleTouch(int code) {
    Buttons button = static_cast<Buttons>(code);
    if (button <= Buttons::ROTATE && pause) return;
    switch (button) {
        case Buttons::LEFT:
            m_figureManager.handleKeyLeft();
            break;
        case Buttons::DOWN:
            m_figureManager.handleKeyDown();
            break;
        case Buttons::RIGHT:
            m_figureManager.handleKeyRight();
            break;
        case Buttons::UP:
            m_figureManager.handleKeyUp();
            break;
        case Buttons::ROTATE:
            m_figureManager.handleKeyRotate();
            break;
        case Buttons::RESET:
            m_figureManager.setGameOver(true);
            pause = false;
            break;
        default:
            pause ? pause = false : pause = true;
    }
}

void Game::update() {
    switch (m_currentGameState)
    {
        case Game::EGameState::figureIsFalling:
            if (m_figureManager.isGameOver()) {
                m_field.markAllLinesToDelete();
                m_currentGameState = EGameState::gameIsRestarting;
                break;
            }

            time = clock.getElapsedTime();
            clock.restart();
            timer += time;

            if (timer > figureFallDelay && !pause) {
                m_figureManager.handleKeyDown();
                timer = 0.f;
            }

            if (m_figureManager.shouldNewFigureBeSpawned()) {
                if (m_field.shouldAnyLineBeDeleted()) {
                    m_currentGameState = EGameState::lineIsDeleting;
                }
                else {
                    m_figureManager.setShouldNewFigureBeSpawned(false);
                    spawnNewFigureAndGenerateNext();
                }
            }
            break;

        case Game::EGameState::lineIsDeleting:
            if (m_figureManager.shouldNewFigureBeSpawned()) {
                if (m_score > m_highScore) {
                    m_highScore.setValue(m_score.getValue());
                    dataManager.setHighScore(m_highScore.getValue());
                }
                m_figureManager.setShouldNewFigureBeSpawned(false);
                shouldLineDeletionAnimationStart = true;
            }
            else if (shouldLineDeletionAnimationStart) {
                time = clock.getElapsedTime();
                clock.restart();
                timer += time;

                if (timer > lineDeletionDelay) {
                    static int j = 0;
                    m_field.deleteLinesAnimation(j++);
                    if (j > m_field.getWidth()) {
                        shouldLineDeletionAnimationStart = false;
                        j = 0;
                    }
                    timer = 0.f;
                }
            }
            else {
                spawnNewFigureAndGenerateNext();
                m_currentGameState = EGameState::figureIsFalling;
            }
            break;

        case Game::EGameState::gameIsRestarting:
            if (m_figureManager.isGameOver()) {
                time = clock.getElapsedTime();
                clock.restart();
                timer += time;

                if (timer > lineDeletionDelay) {
                    static int j = 0;
                    m_field.deleteLinesAnimation(j++);
                    if (j > m_field.getWidth()) {
                        m_figureManager.setGameOver(false);
                        j = 0;
                    }
                    timer = 0.f;
                }
            }
            else {
                m_currentGameState = EGameState::figureIsFalling;
                start();
            }
            break;
    }
}

void Game::spawnNewFigureAndGenerateNext() {
    m_figureManager.spawnNextFigure(nextFigure, nextColor);
    nextColor = m_figureManager.genNextColor();
    nextFigure = m_figureManager.genNextFigure(nextColor);
    increaseSpeed();
}

void Game::increaseSpeed() {
    if (m_speedIncreaseCoef == 0) return;
    if (++fallenFiguresCounter % m_speedIncreaseCoef == 0) {
        figureFallDelay *= 0.8f;
        m_speed.increaseValue(1);
    }
}

bool Game::isGamePaused() {
    return pause;
}

void Game::setGamePaused() {
    pause = true;
}

void Game::showGame() {
    Renderer::clear();
    m_field.render();
    m_scoreText.render();
    m_highScoreText.render();
    m_speedText.render();
    m_score.render();
    m_highScore.render();
    m_speed.render();
    m_miniScreen.render();
}

void Game::loadResources() {
    scoreTextTex = std::make_shared<Texture>("res/textures/score220x48.png");
    highScoreTextTex  = std::make_shared<Texture>("res/textures/highscore220x24.png");
    speedTextTex = std::make_shared<Texture>("res/textures/speed96x22.png");

    cellTexMap[0] = std::make_shared<Texture>("res/textures/black.png");
    cellTexMap[1] = std::make_shared<Texture>("res/textures/red.png");
    cellTexMap[2] = std::make_shared<Texture>("res/textures/orange.png");
    cellTexMap[3] = std::make_shared<Texture>("res/textures/yellow.png");
    cellTexMap[4] = std::make_shared<Texture>("res/textures/green.png");
    cellTexMap[5] = std::make_shared<Texture>("res/textures/blue.png");
    cellTexMap[6] = std::make_shared<Texture>("res/textures/purple.png");

    numsTexMap[0] = std::make_shared<Texture>("res/textures/0.png");
    numsTexMap[1] = std::make_shared<Texture>("res/textures/1.png");
    numsTexMap[2] = std::make_shared<Texture>("res/textures/2.png");
    numsTexMap[3] = std::make_shared<Texture>("res/textures/3.png");
    numsTexMap[4] = std::make_shared<Texture>("res/textures/4.png");
    numsTexMap[5] = std::make_shared<Texture>("res/textures/5.png");
    numsTexMap[6] = std::make_shared<Texture>("res/textures/6.png");
    numsTexMap[7] = std::make_shared<Texture>("res/textures/7.png");
    numsTexMap[8] = std::make_shared<Texture>("res/textures/8.png");
    numsTexMap[9] = std::make_shared<Texture>("res/textures/9.png");

    shaderProgramMap["sprite"] = std::make_shared<ShaderProgram>("res/shaders/vSprite.txt", "res/shaders/fSprite.txt");

    std::vector<std::shared_ptr<Sprite>> cellSprites(cellTexMap.size());
    std::vector<std::shared_ptr<Sprite>> scoreSprites(numsTexMap.size());
    std::vector<std::shared_ptr<Sprite>> highScoreSprites(numsTexMap.size());
    std::vector<std::shared_ptr<Sprite>> speedSprites(numsTexMap.size());

    std::shared_ptr<Sprite> scoreTextSprite = std::make_shared<Sprite>(
            scoreTextTex,
            shaderProgramMap["sprite"],
            glm::vec2(2*m_screenWidth/3, 17*m_screenHeight/20),
            glm::vec2(m_screenWidth/4, m_screenHeight/35),
            0.f
    );

    std::shared_ptr<Sprite> highScoreTextSprite = std::make_shared<Sprite>(
            highScoreTextTex,
            shaderProgramMap["sprite"],
            glm::vec2(m_screenWidth/10, 37*m_screenHeight/40),
            glm::vec2(m_screenWidth/4, m_screenHeight/70),
            0.f
    );

    std::shared_ptr<Sprite> speedTextSprite = std::make_shared<Sprite>(
            speedTextTex,
            shaderProgramMap["sprite"],
            glm::vec2(7*m_screenWidth/10, 11*m_screenHeight/20),
            glm::vec2(m_screenWidth/9, m_screenHeight/76),
            0.f
    );

    for (int i = 0; i < cellTexMap.size(); i++) {
        cellSprites[i] = std::make_shared<Sprite>(
                cellTexMap[i],
                shaderProgramMap["sprite"],
                glm::vec2(0.f),
                glm::vec2(m_screenWidth/20),
                0.f
        );
    }

    for (int i = 0; i < numsTexMap.size(); i++) {
        scoreSprites[i] = std::make_shared<Sprite>(
                numsTexMap[i],
                shaderProgramMap["sprite"],
                glm::vec2(0.f),
                glm::vec2(m_screenWidth/18,m_screenHeight/33),
                0.f
        );
        highScoreSprites[i] = std::make_shared<Sprite>(
                numsTexMap[i],
                shaderProgramMap["sprite"],
                glm::vec2(0.f),
                glm::vec2(m_screenWidth/36,m_screenHeight/66),
                0.f
        );
        speedSprites[i] = std::make_shared<Sprite>(
                numsTexMap[i],
                shaderProgramMap["sprite"],
                glm::vec2(0.f),
                glm::vec2(m_screenWidth/41,m_screenHeight/76),
                0.f
        );
    }

    m_scoreText.init(scoreTextSprite);
    m_highScoreText.init(highScoreTextSprite);
    m_speedText.init(speedTextSprite);

    m_field.init(
            FIELD_WIDTH,
            FIELD_HEIGHT,
            cellSprites,
            glm::vec2(m_screenWidth/10, 2*m_screenHeight/5),
            m_screenWidth/20
    );

    m_miniScreen.init(
            MINISCREEN_WIDTH,
            MINISCREEN_HEIGHT,
            cellSprites,
            glm::vec2(7*m_screenWidth/10, 2*m_screenHeight/3),
            m_screenWidth/20
    );

    m_score.init(
            scoreSprites,
            glm::vec2(2*m_screenWidth/3, 23*m_screenHeight/30),
            m_screenWidth/18
    );

    m_highScore.init(
            highScoreSprites,
            glm::vec2(2*m_screenWidth/5, 37*m_screenHeight/40),
            m_screenWidth/35
    );

    m_speed.init(
            speedSprites,
            glm::vec2(6*m_screenWidth/7, 11*m_screenHeight/20),
            m_screenWidth/40
    );

    m_highScore.setValue(dataManager.getHighScore());
    m_score.setScorePerLine(10);
    m_field.setScore(&m_score);
    m_figureManager.init(&m_field, &m_miniScreen);
}
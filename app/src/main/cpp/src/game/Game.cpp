#include "Game.hpp"

Game::Game(size_t screenWidth, size_t screenHeight) : m_screenWidth(screenWidth), m_screenHeight(screenHeight) {
    srand(time(0));

    textTex = std::make_shared<Texture>("res/textures/score220x48.png");

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

    std::shared_ptr<Sprite> scoreTextSprite = std::make_shared<Sprite>(
            textTex,
        shaderProgramMap["sprite"],
        glm::vec2(FlexibleSizes::getSize(m_screenWidth, 15), 17*m_screenHeight/20),
        glm::vec2(FlexibleSizes::getSize(m_screenWidth, 4), m_screenHeight/35),
        0.f
    );

    for (int i = 0; i < cellTexMap.size(); i++) {
        cellSprites[i] = std::make_shared<Sprite>(
            cellTexMap[i],
            shaderProgramMap["sprite"],
            glm::vec2(0.f),
            glm::vec2(FlexibleSizes::getSize(m_screenWidth, 20)),
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
    }

    m_scoreText.init(scoreTextSprite);

    m_field.init(
        FIELD_WIDTH, 
        FIELD_HEIGHT, 
        cellSprites, 
        glm::vec2(0.f, FlexibleSizes::getSize(m_screenHeight, 3)),
        FlexibleSizes::getSize(m_screenWidth, 20)
    );

    m_miniScreen.init(
        MINISCREEN_WIDTH, 
        MINISCREEN_HEIGHT, 
        cellSprites, 
        glm::vec2((FIELD_WIDTH + 3) * FlexibleSizes::getSize(m_screenWidth, 20), (FIELD_WIDTH + 3) * FlexibleSizes::getSize(m_screenWidth, 10)),
        FlexibleSizes::getSize(m_screenWidth, 20)
    );

    m_score.init(
        scoreSprites,
        glm::vec2(FlexibleSizes::getSize(2*m_screenWidth, 5), 17*m_screenHeight/20),
        m_screenWidth/18
    );

    m_score.setScorePerLine(10);
    m_field.setScore(&m_score);
    m_figureManager.init(&m_field, &m_miniScreen);

    start();
}

void Game::start() {
    m_field.clear();
    m_miniScreen.clear();
    m_figureManager.setGameOver(false);
    m_figureManager.setShouldNewFigureBeSpawned(true);

    m_score.setScore(0);
}

void Game::prepareToRender() {
    shaderProgramMap["sprite"]->use();
    shaderProgramMap["sprite"]->setInt("tex", 0);

    glm::mat4 projectionMatrix = glm::ortho(0.f, static_cast<float>(m_screenWidth), 0.f, static_cast<float>(m_screenHeight),
                                            -1.f, 1.f);
    shaderProgramMap["sprite"]->setMatrix4("projectionMat", projectionMatrix);

    Renderer::clearColor(0.2f, 0.2f, 0.2f, 1.0f);
    nextColor = m_figureManager.genNextColor();
    nextFigure = m_figureManager.genNextFigure(nextColor);
}

void Game::run() {
    if (m_figureManager.isGameOver()) {
        start();
    }
    Renderer::clear();

    float time = clock.getElapsedTime();
    clock.restart();
    timer += time;

    if (timer > delay) {
        m_figureManager.handleKeyDown();
        timer = 0;
    }

    if (m_figureManager.shouldNewFigureBeSpawned()) {
        m_field.deleteLines();
        m_figureManager.spawnNextFigure(nextFigure, nextColor);
        nextColor = m_figureManager.genNextColor();
        nextFigure = m_figureManager.genNextFigure(nextColor);
        m_figureManager.setShouldNewFigureBeSpawned(false);
    }

    showGame();
}

void Game::showGame() {
    m_field.render();
    m_scoreText.render();
    m_score.render();
    m_miniScreen.render();
}

void Game::handleTouch(int code) {
    switch (code){
        case 1:
            m_figureManager.handleKeyLeft();
            break;
        case 2:
            m_figureManager.handleKeyDown();
            break;
        case 3:
            m_figureManager.handleKeyRight();
            break;
        case 4:
            m_figureManager.handleKeyUp();
            break;
        default:
            m_figureManager.handleKeyRotate();
    }
}

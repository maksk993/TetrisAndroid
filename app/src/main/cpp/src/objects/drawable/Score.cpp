#include "Score.hpp"

Score::Score() : m_score(0), m_offset(0), m_scorePerLine(0), m_pathToHighScoreFile(""), m_position(glm::vec2(0.f)) {
}

void Score::init(std::vector<std::shared_ptr<Sprite>>& sprites, const glm::vec2& position, float offset) {
    m_pNumbersSprites = std::move(sprites);
    m_position = std::move(position);
    m_offset = offset;
}

void Score::render() {
    float dx = 0.f;

    for (char& c : getScoreString()) {
        int num = c - '0';
        m_pNumbersSprites[num]->setPosition(glm::vec2(m_position.x + dx, m_position.y));
        m_pNumbersSprites[num]->render();
        dx += m_offset;
    }
}

void Score::setScore(score_t value) {
    m_score = value;
}

std::string Score::getScoreString() {
    return std::to_string(m_score);
}

void Score::setScorePerLine(score_t scorePerLine) {
    m_scorePerLine = scorePerLine;
}

void Score::increaseScore(int leftShiftBy) {
    m_score += m_scorePerLine << leftShiftBy;
}
#include "Score.hpp"

Score::Score() : m_offset(0), m_scorePerLine(0), m_pathToHighScoreFile(""), m_position(glm::vec2(0.f)) {
}

void Score::init(std::vector<std::shared_ptr<Sprite>>& sprites, const glm::vec2& position, float offset) {
    m_pNumbersSprites = std::move(sprites);
    m_position = std::move(position);
    m_offset = offset;
}

void Score::render() {
    float dx = 0.f;

    for (char& c : getValueString()) {
        int num = c - '0';
        m_pNumbersSprites[num]->setPosition(glm::vec2(m_position.x + dx, m_position.y));
        m_pNumbersSprites[num]->render();
        dx += m_offset;
    }
}

void Score::setScorePerLine(int scorePerLine) {
    m_scorePerLine = scorePerLine;
}

void Score::increaseValue(int leftShiftBy) {
    m_value += m_scorePerLine << leftShiftBy;
}

bool Score::operator>(Score &other) {
    return m_value > other.m_value;
}
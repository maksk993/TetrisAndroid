#pragma once

#include <iostream>
#include "IDrawableObject.hpp"
#include "../../graphics/MyAssetManager.h"

using score_t = int;

class Score : public IDrawableObject {
public:
	Score();

	void init(std::vector<std::shared_ptr<Sprite>>& sprites, const glm::vec2& position, float offset);
	virtual void render() override;
	void setScore(score_t value);
	std::string getScoreString();
	void setScorePerLine(score_t scorePerLine);
	void increaseScore(int leftShiftBy);

private:
    MyAssetManager& m_mgr = MyAssetManager::getAssetManager();
	score_t m_score;
	std::string m_pathToHighScoreFile;
	score_t m_scorePerLine;
	
	std::vector<std::shared_ptr<Sprite>> m_pNumbersSprites;
	glm::vec2 m_position;
	float m_offset;
};
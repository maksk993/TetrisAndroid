#pragma once

#include "IDrawableObject.hpp"

class Background : public IDrawableObject {
public:
    Background() = default;

    void init(std::shared_ptr<Sprite> sprite);
    virtual void render() override;

private:
    std::shared_ptr<Sprite> m_sprite;
};
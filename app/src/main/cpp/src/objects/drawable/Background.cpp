#include "Background.hpp"

void Background::init(std::shared_ptr<Sprite> sprite) {
    m_sprite = std::move(sprite);
}

void Background::render() {
    m_sprite->render();
}

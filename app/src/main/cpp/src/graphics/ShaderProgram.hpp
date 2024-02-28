#pragma once

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <iostream>
#include <string>
#include <sstream>
#include "MyAssetManager.h"

#include <glm/mat4x4.hpp>
#include <glm/gtc/type_ptr.hpp>

class ShaderProgram {
public:
    ShaderProgram(const std::string& vertexShaderSourcePath, const std::string& fragmentShaderSourcePath);
    ~ShaderProgram();

    ShaderProgram() = delete;
    ShaderProgram(const ShaderProgram&) = delete;
    ShaderProgram& operator=(const ShaderProgram&) = delete;

    ShaderProgram(ShaderProgram&&) noexcept;
    ShaderProgram& operator=(ShaderProgram&&) noexcept;

    GLuint getID() const;
    void use() const;
    void setInt(const std::string& name, const GLint value);
    void setMatrix4(const std::string& name, const glm::mat4& matrix);

private:
    MyAssetManager& m_mgr = MyAssetManager::getAssetManager();
    GLuint m_ID = 0;

    std::string load(const std::string& path);
    bool createShader(const char* shaderSource, GLenum shaderType, GLuint& shaderID);
    void createShaderProgram(GLuint& vertexShaderID, GLuint& fragmentShaderID);
};
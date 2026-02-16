package io.github.kayaxcoff.tlx.object;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {

    private final int program;
    private final int vertexShader, fragmentShader;

    public Shader(String vertexPath, String fragmentPath) {
        String vertexSource = this.readFile(vertexPath);
        String fragmentSource = this.readFile(fragmentPath);

        this.vertexShader = this.compile(vertexSource, GL20.GL_VERTEX_SHADER);
        this.fragmentShader = this.compile(fragmentSource, GL20.GL_FRAGMENT_SHADER);

        this.program = GL20.glCreateProgram();
        GL20.glAttachShader(this.program, this.vertexShader);
        GL20.glAttachShader(this.program, this.fragmentShader);
        GL20.glLinkProgram(this.program);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            throw new RuntimeException("Shader link error: " + GL20.glGetProgramInfoLog(program));
        }

        GL20.glDeleteShader(this.vertexShader);
        GL20.glDeleteShader(this.fragmentShader);
    }

    public void use() {
        GL20.glUseProgram(this.program);
    }

    public void detach() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        this.detach();
        if (this.program != 0) {
            GL20.glDetachShader(this.program, this.vertexShader);
            GL20.glDetachShader(this.program, this.fragmentShader);
            GL20.glDeleteShader(this.vertexShader);
            GL20.glDeleteShader(this.fragmentShader);
            GL20.glDeleteProgram(this.program);
        }
    }

    public void upload(String name, Matrix4f item) {
        int location = this.getLocation(name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        item.get(buffer);
        GL20.glUniformMatrix4fv(location, false, buffer);
    }
    public void upload(String name, Matrix3f item) {
        int location = this.getLocation(name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        item.get(buffer);
        GL20.glUniformMatrix3fv(location, false, buffer);
    }
    public void upload(String name, Vector4f item) {
        GL20.glUniform4f(this.getLocation(name), item.x, item.y, item.z, item.w);
    }
    public void upload(String name, Vector3f item) {
        GL20.glUniform3f(this.getLocation(name), item.x, item.y, item.z);
    }
    public void upload(String name, Vector2f item) {
        GL20.glUniform2f(this.getLocation(name), item.x, item.y);
    }
    public void upload(String name, float item) {
        GL20.glUniform1f(getLocation(name), item);
    }
    public void upload(String name, int item) {
        GL20.glUniform1i(getLocation(name), item);
    }
    public void upload(String name, int[] item) {
        GL20.glUniform1iv(getLocation(name), item);
    }

    private String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader file: " + path, e);
        }
    }

    private int getLocation(String name) {
        int location = GL20.glGetUniformLocation(this.program, name);
        if (location == -1) {
            System.err.println("Warning: Uniform '" + name + "' not found!");
        }
        return location;
    }

    private int compile(String source, int type) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            throw new RuntimeException("Shader compile error: " + GL20.glGetShaderInfoLog(shader));
        }

        return shader;
    }
}
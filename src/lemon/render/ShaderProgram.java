package lemon.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL20;

public class ShaderProgram {
	private int id;
	private List<Shader> shaders;
	public ShaderProgram(){
		shaders = new ArrayList<Shader>();
		id = GL20.glCreateProgram();
	}
	public UniformVariable getUniformVariable(String name){
		return new UniformVariable(GL20.glGetUniformLocation(id, name), name);
	}
	public void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(id, attribute, variableName);
	}
	public void link(){
		GL20.glLinkProgram(id);
	}
	public void validate(){
		GL20.glValidateProgram(id);
	}
	public void cleanUp(){
		for(Shader shader: shaders){
			GL20.glDetachShader(id, shader.getId());
			shader.cleanUp();
		}
		GL20.glDeleteProgram(id);
	}
	public void addShader(Shader shader){
		shaders.add(shader);
		GL20.glAttachShader(id, shader.getId());
	}
	public List<Shader> getShaders(){
		return Collections.unmodifiableList(shaders);
	}
	public int getId(){
		return id;
	}
}

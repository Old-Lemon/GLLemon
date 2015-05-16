package lemon.render;

import lemon.engine.math.Matrix;
import lemon.engine.math.Vector;

import org.lwjgl.opengl.GL20;

public class UniformVariable {
	private int id;
	private String name;
	public UniformVariable(int id, String name){
		this.id = id;
		this.name = name;
	}
	public void loadFloat(float value){
		GL20.glUniform1f(id, value);
	}
	public void loadVector(Vector vector){
		GL20.glUniform3f(id, vector.getX(), vector.getY(), vector.getZ());
	}
	public void loadBoolean(boolean value){
		loadFloat(value?1:0);
	}
	public void loadMatrix(Matrix matrix){
		GL20.glUniformMatrix4(id, false, matrix.toFloatBuffer());
	}
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
}

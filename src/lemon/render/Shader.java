package lemon.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
	private int id;
	private int type;
	public Shader(int type, CharSequence sequence){
		this.type = type;
		id = GL20.glCreateShader(type);
		GL20.glShaderSource(id, sequence);
		GL20.glCompileShader(id);
		if(GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
			throw new IllegalStateException("Failed to Compile Shader: "+GL20.glGetShaderInfoLog(id));
		}
	}
	public Shader(int type, String file){
		this(type, getFile(file));
	}
	private static StringBuilder getFile(String path){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line=reader.readLine())!=null){
				builder.append(line).append("\n");
			}
			reader.close();
			return builder;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public int getId(){
		return id;
	}
	public int getType(){
		return type;
	}
	public void cleanUp(){
		GL20.glDeleteShader(id);
	}
}

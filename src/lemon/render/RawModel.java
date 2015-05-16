package lemon.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RawModel {
	private int vaoId;
	private List<Integer> attributes;
	private List<Integer> vbos;
	private int vertexCount;
	
	public RawModel(int vertexCount){
		attributes = new ArrayList<Integer>();
		vbos = new ArrayList<Integer>();
		vaoId = GL30.glGenVertexArrays();
		this.vertexCount = vertexCount;
	}
	public void loadVboAttributeData(FloatBuffer data, int usage){
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, usage);
	}
	public void loadVboIndices(IntBuffer data){
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}
	public void loadVertexAttribPointer(int attribute, int size, int stride, int offset){
		GL20.glVertexAttribPointer(attribute, size, GL11.GL_FLOAT, false, stride, offset);
	}
	public void render(){
		for(int attributeNumber: attributes){
			GL20.glEnableVertexAttribArray(attributeNumber);
		}
		GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
		for(int attributeNumber: attributes){
			GL20.glDisableVertexAttribArray(attributeNumber);
		}
	}
	public void addAttribute(int... attributes){
		for(int i: attributes){
			this.attributes.add(i);
		}
	}
	public int getVbo(){
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		return vbo;
	}
	public void cleanUp(){
		GL30.glDeleteVertexArrays(vaoId);
		for(int vboId: vbos){
			GL15.glDeleteBuffers(vboId);
		}
	}
	public int getVaoId(){
		return vaoId;
	}
	public int getVertexCount(){
		return vertexCount;
	}
}
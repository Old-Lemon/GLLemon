package lemon.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.BufferUtils;

public class ModelData {
	private List<Float> data;
	private List<Integer> indices;
	private int[] attributeSizes;
	private int attributeSize;
	public ModelData(int[] attributeSizes){
		data = new ArrayList<Float>();
		indices = new ArrayList<Integer>();
		this.attributeSizes = attributeSizes;
		for(int i: attributeSizes){
			attributeSize+=i;
		}
	}
	public void addData(float... data){
		for(float f: data){
			this.data.add(f);
		}
	}
	public void addIndex(int index){
		indices.add(index);
	}
	public void addIndices(int... indices){
		for(int i: indices){
			this.indices.add(i);
		}
	}
	public int getSize(){
		return data.size()/attributeSize;
	}
	public int getDataSize(){
		return attributeSize;
	}
	public FloatBuffer getBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.size());
		for(float f: data){
			buffer.put(f);
		}
		buffer.flip();
		return buffer;
	}
	public IntBuffer getIndicesBuffer(){
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.size());
		for(int index: indices){
			indicesBuffer.put(index);
		}
		indicesBuffer.flip();
		return indicesBuffer;
	}
	public List<Integer> getIndices(){
		return Collections.unmodifiableList(indices);
	}
	public List<Float> getData(){
		return Collections.unmodifiableList(data);
	}
	public List<Float> getDataByAttribute(int attribute){
		if(attribute>=attributeSizes.length){
			return new ArrayList<Float>();
		}
		List<Float> data = new ArrayList<Float>();
		int offset = 0;
		for(int i=0;i<attribute;++i){
			offset+=attributeSizes[i];
		}
		for(int i=offset;i<this.data.size();i+=getDataSize()){
			for(int j=0;j<attributeSizes[attribute];++j){
				data.add(this.data.get(i+j));
			}
		}
		return Collections.unmodifiableList(data);
	}
}

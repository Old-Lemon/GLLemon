package lemon.render;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import lemon.engine.loader.Loader;

public class ObjLoader implements Loader<String[], String, ModelData> {
	private int progress;
	private int total;
	private int currentProgress;
	private int currentTotal;
	private Queue<String[]> queue;
	private Queue<ModelData> loaded;
	public ObjLoader(){
		queue = new LinkedList<String[]>();
		loaded = new LinkedList<ModelData>();
	}
	@Override
	public void add(String[] data){
		queue.add(data);
		total+=data.length;
	}
	@Override
	public void load(){
		while(!queue.isEmpty()){
			String[] data = queue.peek();
			if(data==null){
				continue;
			}
			ModelData modelData = new ModelData(new int[]{3, 2, 3});
			List<Float> vertices = new ArrayList<Float>();
			List<Float> textureCoords = new ArrayList<Float>();
			List<Float> normals = new ArrayList<Float>();
			List<Integer> indices = new ArrayList<Integer>();
			currentTotal = data.length;
			currentProgress = 0;
			
			for(int i=0;i<data.length;++i){
				currentProgress+=1;
				progress+=1;
				String line = data[i];
				String[] parts = line.split(" ");
				String prefix = parts[0];
				if(prefix.equals("v")){
					vertices.add(Float.parseFloat(parts[1]));
					vertices.add(Float.parseFloat(parts[2]));
					vertices.add(Float.parseFloat(parts[3]));
				}
				if(prefix.equals("vt")){
					textureCoords.add(Float.parseFloat(parts[1]));
					textureCoords.add(Float.parseFloat(parts[2]));
				}
				if(prefix.equals("vn")){
					normals.add(Float.parseFloat(parts[1]));
					normals.add(Float.parseFloat(parts[2]));
					normals.add(Float.parseFloat(parts[3]));
				}
				if(prefix.equals("f")){
					ObjLoader.processVertex(parts[1], indices, vertices, textureCoords, normals, modelData);
					ObjLoader.processVertex(parts[2], indices, vertices, textureCoords, normals, modelData);
					ObjLoader.processVertex(parts[3], indices, vertices, textureCoords, normals, modelData);
				}
			}
			queue.remove();
			loaded.add(modelData);
		}
	}
	@Override
	public ModelData get() {
		return loaded.poll();
	}
	@Override
	public String getCurrentItem(){
		if(queue.isEmpty()){
			return null;
		}
		return queue.peek()[0].substring(2); //Removes the "# "
	}
	@Override
	public int getCurrentProgress(){
		return currentProgress;
	}
	@Override
	public int getCurrentTotal(){
		return currentTotal;
	}
	@Override
	public int getProgress(){
		return progress;
	}
	@Override
	public int getTotal(){
		return total;
	}
	private static void processVertex(String vertex, List<Integer> indices, List<Float> vertices,
			List<Float> textureCoords, List<Float> normals, ModelData modelData){
		String[] parts = vertex.split("/");
		int positionIndex = (Integer.parseInt(parts[0])-1)*3;
		int textureCoordsIndex = (Integer.parseInt(parts[1])-1)*2;
		int normalsIndex = (Integer.parseInt(parts[2])-1)*3;
		float positionX = vertices.get(positionIndex);
		float positionY = vertices.get(positionIndex+1);
		float positionZ = vertices.get(positionIndex+2);
		float textureCoordX = textureCoords.get(textureCoordsIndex);
		float textureCoordY = 1f-textureCoords.get(textureCoordsIndex+1);
		float normalX = normals.get(normalsIndex);
		float normalY = normals.get(normalsIndex+1);
		float normalZ = normals.get(normalsIndex+2);
		modelData.addData(positionX, positionY, positionZ, textureCoordX, textureCoordY, normalX, normalY, normalZ);
		modelData.addIndex(modelData.getSize()-1);
	}
}

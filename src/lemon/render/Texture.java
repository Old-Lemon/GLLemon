package lemon.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Texture {
	private int id;
	
	private float shineDamper;
	private float reflectivity;
	
	public Texture(BufferedImage image){
		this(image, 0, 0);
	}
	public Texture(BufferedImage image, float shineDamper, float reflectivity){
		id = loadTexture(image);
		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}
	public int getId(){
		return id;
	}
	public float getShineDamper(){
		return shineDamper;
	}
	public float getReflectivity(){
		return reflectivity;
	}
	public void cleanUp(){
		GL11.glDeleteTextures(id);
	}
	private static int loadTexture(BufferedImage image){
		int[] pixels = new int[image.getWidth()*image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth()*image.getHeight()*4); //4=RGBA 3=RGB
		for(int y=0;y<image.getHeight();++y){
			for(int x=0;x<image.getWidth();++x){
				int pixel = pixels[y*image.getWidth()+x];
				buffer.put((byte)((pixel>>16)&0xFF)); //Red
				buffer.put((byte)((pixel>>8)&0xFF)); //Green
				buffer.put((byte)(pixel&0xFF)); //Blue
				buffer.put((byte)((pixel>>24)&0xFF)); //Alpha
			}
		}
		buffer.flip();
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		//Wrap
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		//Scaling
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		//Send to OpenGL
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		//Unbind
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return textureID;
	}
	public static BufferedImage createBufferedImage(float r, float g, float b, float a){
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(new Color(r, g, b, a));
		g2d.drawRect(0, 0, 1, 1);
		g2d.dispose();
		return image;
	}
}

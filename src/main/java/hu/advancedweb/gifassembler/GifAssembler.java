package hu.advancedweb.gifassembler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class GifAssembler {
	private static final int IMAGE_PADDING_SIZE = 20;
	private static final int TEXT_PADDING_SIZE = 5;

	private List<Frame> frames = new ArrayList<Frame>();
	
	public byte[] generate() throws IOException {
		return toGif(normalizeSizes(createImages(frames)));
	}
	
	public void addFrame(String description, byte[] image) {
		Frame frame = new Frame();
		frame.details = description;
		frame.screenshot = image;
		frames.add(frame);
	}

	public void clearFrames() {
		frames.clear();
	}
	
	private static List<BufferedImage> createImages(List<Frame> frames) throws IOException {
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		for (Iterator<Frame> iterator = frames.iterator(); iterator.hasNext();) {
			Frame frame = iterator.next();
			Color paddingColor = iterator.hasNext() ? Color.WHITE : Color.RED;
			BufferedImage image = addPadding(addHeaderText(toBufferedImage(frame.screenshot), frame.details, 30F, TEXT_PADDING_SIZE), paddingColor, IMAGE_PADDING_SIZE);
			result.add(image);
		}
		return result;
	}
	
	private static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
		return ImageIO.read(new ByteArrayInputStream(bytes));
	}
	
	private static byte[] toGif(List<BufferedImage> images) throws IOException {
		try (
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageOutputStream output = ImageIO.createImageOutputStream(baos);
				AnimatedGifImageWriter writer = new AnimatedGifImageWriter(output, images.get(0).getType(), 2000, true); 
		) {
			for (BufferedImage image : images) {
				writer.writeToSequence(image);
			}
			output.flush();
			return baos.toByteArray();
		}
	}

	private List<BufferedImage> normalizeSizes(List<BufferedImage> bufferedImages) {
		int maxWidth = 0;
		int maxHeight = 0;
		for (BufferedImage bufferedImage : bufferedImages) {
			if (maxWidth < bufferedImage.getWidth()) maxWidth = bufferedImage.getWidth();
			if (maxHeight < bufferedImage.getHeight()) maxHeight= bufferedImage.getHeight();
		}
		
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		for (BufferedImage bufferedImage : bufferedImages) {
			BufferedImage normalizedImage = new BufferedImage(maxWidth, maxHeight, bufferedImage.getType());
			Graphics g = normalizedImage.getGraphics();
			g.drawImage(bufferedImage, 0, 0, null);
			g.dispose();
			result.add(normalizedImage);
		}
		return result;
	}
	
	private static BufferedImage addPadding(BufferedImage bufferedImage, Color color, int paddingSize) {
		BufferedImage newImage = new BufferedImage(bufferedImage.getWidth() + paddingSize, bufferedImage.getHeight() + paddingSize, bufferedImage.getType());
		
		Graphics g = newImage.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
		g.drawImage(bufferedImage, paddingSize / 2, paddingSize / 2, null);
		g.dispose();
		
		return newImage;
	}
	
	private static BufferedImage addHeaderText(BufferedImage bufferedImage, String text, float size, int textBottomPadding) {
		BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight() + (int)Math.ceil(size) + textBottomPadding, bufferedImage.getType());
		Graphics g = newImage.getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight() + (int)Math.ceil(size) + textBottomPadding);
		
		g.drawImage(bufferedImage, 0, (int)Math.ceil(size) + textBottomPadding, null);
	    
		g.setFont(g.getFont().deriveFont(size));
		g.setColor(Color.BLACK);
		g.drawString(text, 0, (int)Math.ceil(size));
		
		g.dispose();
	    return newImage;
	}
	
	public static class Frame {
		public String details;
		public byte[] screenshot;
	}
	
}

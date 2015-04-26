package hu.advancedweb.gifassembler;

import java.awt.image.RenderedImage;
import java.io.Closeable;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

public class AnimatedGifImageWriter implements Closeable {
	
	private ImageWriter imageWriter;
	private ImageWriteParam imageWriteParam;
	private IIOMetadata iioMetaData;

	public AnimatedGifImageWriter(ImageOutputStream imageOutputStream, int imageType, long delayTimeInMs, boolean loop) throws IOException {
		imageWriter = ImageIO.getImageWritersBySuffix("gif").next();
		imageWriteParam = imageWriter.getDefaultWriteParam();
		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);

		iioMetaData = imageWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
		String metaFormatName = iioMetaData.getNativeMetadataFormatName();
		IIOMetadataNode root = (IIOMetadataNode) iioMetaData.getAsTree(metaFormatName);

		getNode(root, "GraphicControlExtension").setAttribute("delayTime", Long.toString(delayTimeInMs / 10L));
		getNode(root, "GraphicControlExtension").setAttribute("userInputFlag", "FALSE");
		getNode(root, "GraphicControlExtension").setAttribute("disposalMethod", "none");
		getNode(root, "GraphicControlExtension").setAttribute("transparentColorFlag", "FALSE");
		getNode(root, "GraphicControlExtension").setAttribute("transparentColorIndex", "0");

		if (loop) {
			IIOMetadataNode applicationExtensionNode = new IIOMetadataNode("ApplicationExtension");
			applicationExtensionNode.setAttribute("applicationID", "NETSCAPE");
		    applicationExtensionNode.setAttribute("authenticationCode", "2.0");
			applicationExtensionNode.setUserObject(new byte[] { (byte) 0x1, (byte) (0xFF), (byte) (0xFF) });
			getNode(root, "ApplicationExtensions").appendChild(applicationExtensionNode);			    
		}
		
		iioMetaData.setFromTree(metaFormatName, root);
		imageWriter.setOutput(imageOutputStream);
		imageWriter.prepareWriteSequence(null);
	}

	public void writeToSequence(RenderedImage img) throws IOException {
		imageWriter.writeToSequence(new IIOImage(img, null, iioMetaData), imageWriteParam);
	}

	public void close() throws IOException {
		imageWriter.endWriteSequence();
	}

	private IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
		IIOMetadataNode node = findNode(rootNode, nodeName);
		return node != null ? node : createNode(rootNode, nodeName);
	}
	
	private IIOMetadataNode findNode(IIOMetadataNode rootNode, String nodeName) {
		for (int i = 0; i < rootNode.getLength(); i++) {
			if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
				return ((IIOMetadataNode) rootNode.item(i));
			}
		}
		return null;
	}
	
	private IIOMetadataNode createNode(IIOMetadataNode rootNode, String nodeName) {
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return (node);
	}
	
}
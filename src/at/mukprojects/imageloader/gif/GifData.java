/**
 * This code is copyright (c) Mathias Markl 2016
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package at.mukprojects.imageloader.gif;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import gifAnimation.GifDecoder;
import processing.core.PImage;

/**
 * This class represents an loaded GIF.
 * 
 * @author Mathias Markl
 */
public class GifData {

    private String id;

    private String imgInfo;
    private long timestamp;

    private String gifUrl;
    private Integer delay;

    private PImage[] frames;
    private PImage previewImage;

    private GifDecoder decoder;

    /**
     * Constructs a new GIF.
     * 
     * @param id
     *            The image id. The id must be unique.
     * @param imgInfo
     *            The image info.
     * @param timestamp
     *            The image timestamp.
     * @param imgUrl
     *            The GIF URL.
     */
    public GifData(String id, String imgInfo, long timestamp, String imgUrl) {
	this.id = id;
	this.imgInfo = imgInfo;
	this.timestamp = timestamp;
	this.gifUrl = imgUrl;
    }

    /**
     * Constructs a new image.
     * 
     * @param id
     *            The image id. The id must be unique.
     * @param imgInfo
     *            The image info.
     * @param timestamp
     *            The image timestamp.
     * @param imgUrl
     *            The GIF URL.
     * @param frames
     *            The GIFs.
     */
    public GifData(String id, String imgInfo, long timestamp, String imgUrl, PImage[] frames) {
	this.id = id;
	this.imgInfo = imgInfo;
	this.timestamp = timestamp;
	this.gifUrl = imgUrl;
	this.frames = frames;
    }

    /**
     * Gets the image id.
     * 
     * @return The image id.
     */
    public String getId() {
	return id;
    }

    /**
     * Gets the image info.
     * 
     * @return The image info.
     */
    public String getImgInfo() {
	return imgInfo;
    }

    /**
     * Gets the image timestamp.
     * 
     * @return The image timestamp.
     */
    public long getTimestamp() {
	return timestamp;
    }

    /**
     * Gets the GIF URL.
     * 
     * @return The GIF URL.
     */
    public String getGifUrl() {
	return gifUrl;
    }

    /**
     * Gets the delay. If the GIF is in lazy load mode and an error occurs
     * during the loading process the method will return null.
     * 
     * @return The GIF.
     */
    public Integer getDelay() {
	if (delay == null) {
	    try {
		loadGif();
	    } catch (IOException e) {
	    }
	}
	return delay;
    }

    /**
     * Gets the GIF. If the GIF is in lazy load mode and an error occurs during
     * the loading process the method will return null.
     * 
     * @return The GIF.
     */
    public PImage getPreviewImage() {
	if (previewImage == null) {
	    try {
		loadPreview();
	    } catch (IOException e) {
	    }
	}
	return previewImage;
    }

    /**
     * Gets the GIF. If the GIF is in lazy load mode and an error occurs during
     * the loading process the method will return null.
     * 
     * @return The GIF.
     */
    public PImage[] getGifFrames() {
	if (frames == null) {
	    try {
		loadGif();
	    } catch (IOException e) {
	    }
	}
	return frames;
    }

    /**
     * Frees the memory space.
     */
    public void clearMemSpace() {
	decoder = null;
	previewImage = null;
	frames = null;
    }

    /**
     * Returns the GIF decoder.
     * 
     * @return The decoder.
     */
    public GifDecoder getDecoder() throws IOException {
	if (decoder == null) {
	    decoder = new GifDecoder();

	    if (gifUrl.startsWith("http")) {
		URL u = new URL(gifUrl);
		HttpURLConnection uc = (HttpURLConnection) u.openConnection();
		decoder.read(new BufferedInputStream(uc.getInputStream()));
	    } else {
		decoder.read(new BufferedInputStream(new FileInputStream(gifUrl)));
	    }
	}
	
	return decoder;
    }

    /**
     * Loads the preview image.
     */
    public void loadPreview() throws IOException {
	decoder = getDecoder();

	if (gifUrl.startsWith("http")) {
	    URL u = new URL(gifUrl);
	    HttpURLConnection uc = (HttpURLConnection) u.openConnection();
	    decoder.read(new BufferedInputStream(uc.getInputStream()));
	} else {
	    decoder.read(new BufferedInputStream(new FileInputStream(gifUrl)));
	}

	BufferedImage frame = decoder.getFrame(0);
	previewImage = new PImage(frame.getWidth(), frame.getHeight(), PImage.ARGB);
	System.arraycopy(frame.getRGB(0, 0, frame.getWidth(), frame.getHeight(), null, 0, frame.getWidth()), 0,
		previewImage.pixels, 0, frame.getWidth() * frame.getHeight());
    }

    /**
     * Loads the GIF.
     */
    public void loadGif() throws IOException {
	decoder = getDecoder();

	if (gifUrl.startsWith("http")) {
	    URL u = new URL(gifUrl);
	    HttpURLConnection uc = (HttpURLConnection) u.openConnection();
	    decoder.read(new BufferedInputStream(uc.getInputStream()));
	} else {
	    decoder.read(new BufferedInputStream(new FileInputStream(gifUrl)));
	}

	int n = decoder.getFrameCount();

	frames = new PImage[n];
	int delaySum = 0;

	for (int j = 0; j < n; j++) {
	    delaySum += decoder.getDelay(j);
	    BufferedImage frame = decoder.getFrame(j);
	    frames[j] = new PImage(frame.getWidth(), frame.getHeight(), PImage.ARGB);
	    System.arraycopy(frame.getRGB(0, 0, frame.getWidth(), frame.getHeight(), null, 0, frame.getWidth()), 0,
		    frames[j].pixels, 0, frame.getWidth() * frame.getHeight());
	}

	delay = delaySum / n;
	previewImage = frames[0];
    }

    @Override
    public String toString() {
	String s = "Gif {\n";

	s += " ID: " + id + "\n";
	s += " Info [\n" + imgInfo.replaceAll("(?m)^", "  ") + "\n ]\n";
	s += " GifUrl: " + gifUrl + "\n";
	s += " Timestamp: " + new Date(timestamp) + "\n";

	s += "}";

	return s;
    }
}

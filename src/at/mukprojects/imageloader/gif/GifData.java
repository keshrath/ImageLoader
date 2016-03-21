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

import java.util.Date;

import gifAnimation.Gif;
import processing.core.PImage;

/**
 * This class represents an loaded image.
 * 
 * @author Mathias Markl
 */
public class GifData {

    private String id;

    private String imgInfo;
    private long timestamp;

    private String gifUrl;
    private PImage[] frames;

    /**
     * Constructs a new image.
     * 
     * @param id
     *            The image id. The id must be unique.
     * @param imgInfo
     *            The image info.
     * @param timestamp
     *            The image timestamp.
     * @param gifUrl
     *            The gif url.
     * @param frames
     *            The gifs.
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
     * Gets the gif url.
     * 
     * @return The gif url.
     */
    public String getGifUrl() {
	return gifUrl;
    }

    /**
     * Gets the gif.
     * 
     * @return The gif.
     */
    public PImage[] getGifFrames() {
	return frames;
    }

    @Override
    public String toString() {
	String s = "";
	
	s += "ID: " + id + "\n";
	s += "Info:\n" + imgInfo + "\n";
	s += "gifUrl:\n" + imgInfo + "\n";
	s += "Timestamp: " + new Date(timestamp);
	
	return s;
    }
}

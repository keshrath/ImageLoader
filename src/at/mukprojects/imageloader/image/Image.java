/**
 * This code is copyright (c) Mathias Markl 2015
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

package at.mukprojects.imageloader.image;

import processing.core.PImage;

/**
 * This class represents an loaded image.
 * 
 * @author Mathias Markl
 */
public class Image {

    private String id;

    private String imgInfo;
    private long timestamp;

    private String imgUrl;
    private PImage img;

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
     *            The image url.
     * @param img
     *            The image.
     */
    public Image(String id, String imgInfo, long timestamp, String imgUrl, PImage img) {
	this.id = id;
	this.imgInfo = imgInfo;
	this.timestamp = timestamp;
	this.imgUrl = imgUrl;
	this.img = img;
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
     * Gets the image url.
     * 
     * @return The image url.
     */
    public String getImgUrl() {
	return imgUrl;
    }

    /**
     * Gets the image.
     * 
     * @return The image.
     */
    public PImage getImg() {
	return img;
    }
}

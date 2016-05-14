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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ImageList holds all loaded GIFs.
 * 
 * @author Mathias Markl
 */
public class GifList {

    private ConcurrentHashMap<String, GifData> images;
    private Random random;

    /**
     * Constructs a new ImageList.
     */
    public GifList() {
	images = new ConcurrentHashMap<String, GifData>();
	random = new Random();
    }

    /**
     * Clears the list.
     */
    public void clearList() {
	images.clear();
    }

    /**
     * Adds an image to the list. If the list already has an image with the same
     * id, the image won't be added.
     * 
     * @param gif
     *            The image, which should be added to the list.
     */
    public void addImage(GifData gif) {
	if (!images.containsKey(gif.getId())) {
	    images.put(gif.getId(), gif);
	}
    }

    /**
     * Returns the number of key-value mappings in this list.
     * 
     * @return The size of the list.
     */
    public int size() {
	return images.size();
    }

    /**
     * Returns a list of all available image identifiers.
     * 
     * @return A list of all image identifiers.
     */
    public Enumeration<String> getIds() {
	return images.keys();
    }

    /**
     * Returns a copy of the image list.
     * 
     * @return A copy of the list.
     */
    public Collection<GifData> getCopyList() {
	return images.values();
    }

    /**
     * Returns the image with a given identifier.
     * 
     * @param key
     *            The image identifier.
     * @return The image.
     */
    public GifData getImage(String key) {
	return images.get(key);
    }

    /**
     * Gets a random image from the list.
     * 
     * @return A random image.
     */
    public GifData getRandom() {
	if (images.isEmpty()) {
	    return null;
	} else {
	    return getImage(random.nextInt(images.size()));
	}
    }

    /**
     * Gets the most recent image.
     * 
     * @return The last added image.
     */
    public GifData getMostRecentImage() {
	GifData outputImg = null;

	for (GifData img : images.values()) {
	    if (outputImg == null) {
		outputImg = img;
	    }

	    if (img.getTimestamp() > outputImg.getTimestamp()) {
		outputImg = img;
	    }
	}

	return outputImg;
    }

    private GifData getImage(int index) {
	int counter = 0;

	for (GifData img : images.values()) {
	    if (counter == index) {
		return img;
	    }
	    counter++;
	}

	return null;
    }
}

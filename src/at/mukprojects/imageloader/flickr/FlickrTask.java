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

package at.mukprojects.imageloader.flickr;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

import at.mukprojects.imageloader.image.Image;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents the FlickrLoader task.
 * 
 * @author Mathias Markl
 */
public class FlickrTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FlickrTask.class);

    private PApplet applet;
    private String searchParam;
    private ImageList imageList;
    private Flickr flickr;

    private boolean runOnce;
    private long delay;
    private boolean lazyLoad;

    private volatile boolean running;

    /**
     * Constructs a new FlickrTask.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param searchParam
     *            The search parameter.
     * @param imageList
     *            The ImageList object.
     * @param flickr
     *            The Flickr object.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     */
    public FlickrTask(PApplet applet, String searchParam, ImageList imageList, Flickr flickr, boolean runOnce,
	    long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.imageList = imageList;
	this.flickr = flickr;

	this.runOnce = runOnce;
	this.delay = delay;
	this.lazyLoad = lazyLoad;

	running = true;
    }

    /**
     * Stops the task.
     */
    public void stop() {
	running = false;
    }

    @Override
    public void run() {
	logger.info("Task is running...");

	PhotosInterface photoInterface = flickr.getPhotosInterface();
	SearchParameters searchParameters = new SearchParameters();
	searchParameters.setAccuracy(1);
	searchParameters.setTags(PApplet.split(searchParam, " "));

	for (int i = 0; running; i++) {
	    try {
		logger.debug("Task loads data.");

		PhotoList<Photo> photoList = photoInterface.search(searchParameters, 50, i);

		for (Photo p : photoList) {
		    Photo photo = photoInterface.getInfo(p.getId(), null);

		    String id = "Flickr#" + photo.getId();

		    String imgInfo = "";
		    imgInfo += "Title: " + photo.getTitle() + "\n";
		    imgInfo += "Description: " + photo.getDescription() + "\n";
		    imgInfo += "License: " + FlickrLicenses.fromId(photo.getLicense()).getText();

		    long timestamp = new Date().getTime();
		    String imgUrl = photo.getLargeUrl();

		    PImage img = null;
		    if (!lazyLoad) {
			img = applet.loadImage(imgUrl);
		    }

		    imageList.addImage(new Image(id, imgInfo, timestamp, imgUrl, img));
		}

		if (runOnce) {
		    logger.debug("Task is finished.");
		    running = false;
		} else {
		    logger.debug("Task is delayed...");
		    Thread.sleep(delay);
		}
	    } catch (InterruptedException | FlickrException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

}

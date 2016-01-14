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

package at.mukprojects.imageloader.instagram;

import java.util.Date;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.imageloader.image.Image;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents the FlickrLoader task.
 * 
 * @author Mathias Markl
 */
public class InstagramTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InstagramTask.class);

    private static final long delay = 1000 * 60;

    private PApplet applet;
    private String serachParam;
    private ImageList imageList;
    private Instagram instagram;

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
     * @param instagram
     *            The Instagram object.
     */
    public InstagramTask(PApplet applet, String searchParam, ImageList imageList, Instagram instagram) {
	this.applet = applet;
	this.serachParam = searchParam;
	this.imageList = imageList;
	this.instagram = instagram;

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

	while (running) {
	    try {
		logger.debug("Task loads data.");

		TagMediaFeed mediaFeed = instagram.getRecentMediaTags(serachParam, 30);
		List<MediaFeedData> mediaFeeds = mediaFeed.getData();

		for (MediaFeedData data : mediaFeeds) {
		    String id = "Instagram#" + data.getId();

		    String imgInfo = "";
		    imgInfo += "User: " + data.getUser().getFullName() + "\n";
		    imgInfo += "Description:\n";
		    imgInfo += data.getCaption().getText() + "\n";
		    imgInfo += "Filter:\n";
		    imgInfo += data.getImageFilter();

		    long timestamp = new Date().getTime();
		    String imgUrl = data.getImages().getStandardResolution().getImageUrl();

		    PImage img = applet.loadImage(imgUrl);

		    imageList.addImage(new Image(id, imgInfo, timestamp, imgUrl, img));
		}

		logger.debug("Task is delayed...");
		Thread.sleep(delay);
	    } catch (InterruptedException | InstagramException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

}

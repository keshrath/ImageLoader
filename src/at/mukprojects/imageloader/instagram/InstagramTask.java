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
 * This class represents the InstagramLoader task.
 * 
 * @author Mathias Markl
 */
public class InstagramTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InstagramTask.class);

    private PApplet applet;
    private String searchParam;
    private ImageList imageList;
    private Instagram instagram;

    private boolean runOnce;
    private long delay;
    private boolean lazyLoad;

    private volatile boolean running;

    /**
     * Constructs a new InstagramTask.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param searchParam
     *            The search parameter.
     * @param imageList
     *            The ImageList object.
     * @param instagram
     *            The Instagram object.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     */
    public InstagramTask(PApplet applet, String searchParam, ImageList imageList, Instagram instagram, boolean runOnce,
	    long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.imageList = imageList;

	this.instagram = instagram;

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

	while (running) {
	    try {
		logger.debug("Task loads data.");

		TagMediaFeed mediaFeed = instagram.getRecentMediaTags(searchParam, 30);
		List<MediaFeedData> mediaFeeds = mediaFeed.getData();

		for (MediaFeedData data : mediaFeeds) {
		    String id = "Instagram#" + data.getId();

		    String imgInfo = "";
		    imgInfo += "User: " + data.getUser().getFullName() + "\n";
		    imgInfo += "Description: " + data.getCaption().getText() + "\n";
		    imgInfo += "Filter:\n" + data.getImageFilter();

		    long timestamp = new Date().getTime();
		    String imgUrl = data.getImages().getStandardResolution().getImageUrl();

		    boolean validImgUrl = false;

		    if ((!(imgUrl.endsWith(".jpg") || imgUrl.endsWith(".jpeg") || imgUrl.endsWith(".tga")
			    || imgUrl.endsWith("png") || imgUrl.endsWith(".gif")))
			    && (imgUrl.contains(".jpg") || imgUrl.contains("jpeg") || imgUrl.contains(".tga")
				    || imgUrl.contains("png") || imgUrl.contains(".gif"))) {
			imgUrl = imgUrl.split("ig_cache_key=")[0];
			imgUrl = imgUrl.substring(0, imgUrl.length() - 1);
			validImgUrl = true;
		    } else if (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".jpeg") || imgUrl.endsWith(".tga")
			    || imgUrl.endsWith("png") || imgUrl.endsWith(".gif")) {
			validImgUrl = true;
		    } else {
			validImgUrl = false;
		    }

		    PImage img = null;
		    if (!lazyLoad) {
			img = applet.loadImage(imgUrl);
		    }

		    if (validImgUrl) {
			imageList.addImage(new Image(id, imgInfo, timestamp, imgUrl, img));
		    }
		}

		if (runOnce) {
		    logger.debug("Task is finished.");
		    running = false;
		} else {
		    logger.debug("Task is delayed...");
		    Thread.sleep(delay);
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

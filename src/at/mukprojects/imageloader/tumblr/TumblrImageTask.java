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

package at.mukprojects.imageloader.tumblr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

import at.mukprojects.imageloader.image.Image;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents the TumblrLoader task.
 * 
 * @author Mathias Markl
 */
public class TumblrImageTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TumblrImageTask.class);

    private PApplet applet;
    private String searchParam;
    private ImageList imageList;

    private JumblrClient client;

    private boolean runOnce;
    private long delay;
    private boolean lazyLoad;

    private volatile boolean running;

    /**
     * Constructs a new GoogleTask.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param searchParam
     *            The search parameter.
     * @param imageList
     *            The ImageList object.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     */
    public TumblrImageTask(PApplet applet, String searchParam, ImageList imageList, JumblrClient client,
	    boolean runOnce, long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.imageList = imageList;

	this.client = client;

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

	for (int i = 0; running; i += 20) {
	    try {
		logger.debug("Task loads data.");

		Map<String, Integer> options = new HashMap<String, Integer>();
		options.put("limit", 20);
		options.put("offset", i);

		for (Post post : client.tagged(searchParam, options)) {

		    if (post.getType().equals("photo") && post instanceof PhotoPost) {
			String id = "Tumblr#" + post.getId();

			String imgInfo = "";
			imgInfo += "Blog: " + post.getBlogName() + "\n";
			imgInfo += "Tags: " + post.getTags();

			long timestamp = new Date().getTime();
			String imgUrl = ((PhotoPost) post).getPhotos().get(0).getOriginalSize().getUrl();

			PImage img = null;
			if (!lazyLoad) {
			    img = applet.loadImage(imgUrl);
			}

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
	    } catch (InterruptedException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

}

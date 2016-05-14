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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.PhotoPost;
import com.tumblr.jumblr.types.Post;

import at.mukprojects.imageloader.gif.GifData;
import at.mukprojects.imageloader.gif.GifList;
import processing.core.PApplet;

/**
 * This class represents the TumblrLoader task.
 * 
 * @author Mathias Markl
 */
public class TumblrGifTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TumblrGifTask.class);

    private PApplet applet;
    private String searchParam;
    private GifList gifList;

    private String apiKey;
    private String apiSecret;
    private boolean lazyLoad;
    private JumblrClient jumblr;

    private boolean runOnce;
    private long delay;

    private volatile boolean running;

    /**
     * Constructs a new GoogleTask.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param searchParam
     *            The search parameter.
     * @param gifList
     *            The ImageList object.
     * @param jumblr
     *            The Jumblr object.
     * @param apiKey
     *            The API key.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     */
    public TumblrGifTask(PApplet applet, String searchParam, GifList gifList, String apiKey, String apiSecret,
	    JumblrClient jumblr, boolean runOnce, long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.gifList = gifList;

	this.apiKey = apiKey;
	this.apiSecret = apiSecret;
	this.lazyLoad = lazyLoad;
	this.jumblr = jumblr;

	this.runOnce = runOnce;
	this.delay = delay;

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

		for (Post post : jumblr.tagged(searchParam, options)) {

		    if (post.getType().equals("photo") && post instanceof PhotoPost) {
			String id = "Tumblr#" + post.getId();

			String imgInfo = "";
			imgInfo += "Blog: " + post.getBlogName() + "\n";
			imgInfo += "Tags: " + post.getTags();

			long timestamp = new Date().getTime();
			String imgUrl = ((PhotoPost) post).getPhotos().get(0).getOriginalSize().getUrl();

			if (imgUrl.endsWith("gif")) {
			    if (!lazyLoad) {
				GifData gif = new GifData(id, imgInfo, timestamp, imgUrl, null);
				gif.loadGif();
				gifList.addImage(gif);
			    } else {
				gifList.addImage(new GifData(id, imgInfo, timestamp, imgUrl, null));
			    }
			}
		    }
		}

		if (runOnce) {
		    logger.debug("Task is finished.");
		    running = false;
		} else {
		    logger.debug("Task is delayed...");
		    Thread.sleep(delay);
		}
	    } catch (InterruptedException | IOException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

}

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

package at.mukprojects.imageloader.giphy;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.giphy.GiphyData;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import at.mukprojects.imageloader.gif.GifData;
import at.mukprojects.imageloader.gif.GifList;
import processing.core.PApplet;

/**
 * This class represents the GiphyLoader task.
 * 
 * @author Mathias Markl
 */
public class GiphyTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GiphyTask.class);

    private PApplet applet;
    private String searchParam;
    private GifList gifList;

    private String apiKey;
    private boolean lazyLoad;
    private Giphy giphy;

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
     * @param giphy
     *            The Giphy4J object.
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
    public GiphyTask(PApplet applet, String searchParam, GifList gifList, String apiKey, Giphy giphy, boolean runOnce,
	    long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.gifList = gifList;

	this.apiKey = apiKey;
	this.lazyLoad = lazyLoad;
	this.giphy = giphy;

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

	for (int i = 0; running; i += 25) {
	    try {
		logger.debug("Task loads data.");

		SearchFeed feed = giphy.search(searchParam, 25, i);

		if (feed.getDataList() != null && feed.getDataList().size() > 0) {
		    for (GiphyData result : feed.getDataList()) {
			String id = "Giphy#" + result.getId();

			String imgInfo = "";
			imgInfo += "Slug: " + result.getSlug() + "\n";
			imgInfo += "Source: " + result.getSource() + "\n";
			imgInfo += "ImportDatetime: " + result.getImportDatetime();

			long timestamp = new Date().getTime();
			String imgUrl = result.getImages().getOriginal().getUrl();

			if (!lazyLoad) {
			    GifData gif = new GifData(id, imgInfo, timestamp, imgUrl, null);
			    gif.loadGif();
			    gifList.addImage(gif);
			} else {
			    gifList.addImage(new GifData(id, imgInfo, timestamp, imgUrl, null));
			}
		    }
		} else {
		    logger.warn("No results were found.");
		}

		if (runOnce) {
		    logger.debug("Task is finished.");
		    running = false;
		} else {
		    logger.debug("Task is delayed...");
		    Thread.sleep(delay);
		}
	    } catch (InterruptedException | GiphyException | IOException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

}

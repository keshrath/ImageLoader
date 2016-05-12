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

package at.mukprojects.imageloader.google;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import at.mukprojects.imageloader.flickr.FlickrLicenses;
import at.mukprojects.imageloader.image.Image;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents the GoogleLoader task.
 * 
 * @author Mathias Markl
 */
public class GoogleTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GoogleTask.class);

    private PApplet applet;
    private String searchParam;
    private ImageList imageList;

    private String apiKey;
    private String searchEngineId;

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
     * @param apiKey
     *            The API key.
     * @param searchEngineId
     *            The search engine id.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     */
    public GoogleTask(PApplet applet, String searchParam, ImageList imageList, String apiKey, String searchEngineId,
	    boolean runOnce, long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.imageList = imageList;

	this.apiKey = apiKey;
	this.searchEngineId = searchEngineId;

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

	for (long i = 10L; running; i += 10) {
	    try {
		logger.debug("Task loads data.");

		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		Customsearch customsearch = new Customsearch.Builder(httpTransport, jsonFactory, null)
			.setApplicationName("ImageLoader").build();

		List<Result> resultList = null;

		Customsearch.Cse.List list = customsearch.cse().list(searchParam);

		list.setKey(apiKey);
		list.setCx(searchEngineId);
		list.setSearchType("image");
		list.setStart(i);

		Search results = list.execute();
		resultList = results.getItems();

		if (resultList != null && resultList.size() > 0) {
		    for (Result result : resultList) {
			String id = "Google#" + result.getLink();

			String imgInfo = "";
			imgInfo += "Title: " + result.getTitle() + "\n";
			imgInfo += "DisplayLink: " + result.getDisplayLink() + "\n";
			imgInfo += "Snippet: " + result.getSnippet();

			long timestamp = new Date().getTime();
			String imgUrl = result.getLink();

			PImage img = null;
			if (!lazyLoad) {
			    img = applet.loadImage(imgUrl);
			}

			imageList.addImage(new Image(id, imgInfo, timestamp, imgUrl, img));
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
	    } catch (InterruptedException | IOException e) {
		logger.error("An error occured. The task will be stopped.", e);
		running = false;
	    }
	}

	logger.info("Task has stopped.");
    }

}

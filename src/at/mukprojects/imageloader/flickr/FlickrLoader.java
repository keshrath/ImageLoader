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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;

import at.mukprojects.imageloader.ImageLoader;
import at.mukprojects.imageloader.image.ImageList;

import processing.core.PApplet;

/**
 * This class represents the FlickrLoader.
 * 
 * @author Mathias Markl
 */
public class FlickrLoader extends ImageLoader {

    private static final Logger logger = LoggerFactory.getLogger(FlickrLoader.class);

    private String apiKey;
    private String apiSecret;

    private Flickr flickr;

    private Thread thread = null;
    private FlickrTask runnable = null;

    /**
     * Constructs a new ImageLoader.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param apiKey
     *            The API key.
     * @param apiSecret
     *            The API secret.
     */
    public FlickrLoader(PApplet applet, String apiKey, String apiSecret) {
	super(applet);

	this.apiKey = apiKey;
	this.apiSecret = apiSecret;

	this.flickr = new Flickr(apiKey, apiSecret, new REST());
    }

    public String getApiKey() {
	return apiKey;
    }

    public String getApiSecret() {
	return apiSecret;
    }

    @Override
    public ImageList start(String searchParam, ImageList imageList, boolean runOnce, long delay, boolean lazyLoad) {
	if (thread != null) {
	    logger.info("Loader is already started.");
	    logger.info("The restart method will be used instead.");

	    return restart(searchParam, imageList, runOnce, delay, lazyLoad);
	} else {
	    runnable = new FlickrTask(applet, searchParam, imageList, flickr, runOnce, delay, lazyLoad);
	    thread = new Thread(runnable, "FlickrTask");

	    logger.info("Starting Thread: " + thread + "...");
	    thread.start();
	    logger.debug(thread + " successfully started.");

	    return imageList;
	}
    }

    @Override
    public ImageList restart(String searchParam, ImageList imageList, boolean runOnce, long delay, boolean lazyLoad) {
	logger.info("Stopping the current thread: " + thread + "...");
	runnable.stop();
	logger.debug(thread + " successfully stopped.");

	runnable = new FlickrTask(applet, searchParam, imageList, flickr, runOnce, delay, lazyLoad);
	thread = new Thread(runnable, "FlickrTask");

	logger.info("Starting Thread: " + thread + "...");
	thread.start();
	logger.debug(thread + " successfully started.");

	return imageList;
    }

    @Override
    public void stop() {
	logger.info("Stopping the current thread: " + thread + "...");

	runnable.stop();
	thread = null;
	runnable = null;

	logger.debug(thread + " successfully stopped.");
    }
}

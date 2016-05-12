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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.imageloader.ImageLoader;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;

/**
 * This class represents the GoogleLoader.
 * 
 * @author Mathias Markl
 */
public class GoogleLoader extends ImageLoader {

    private static final Logger logger = LoggerFactory.getLogger(GoogleLoader.class);

    /**
     * Default search engine id.
     */
    private static final String DEFAULT_SEARCH_ENGINE_ID = "018120529865639172616:gn3bbggenh8";

    private String apiKey;
    private String searchEngineId;

    private Thread thread = null;
    private GoogleTask runnable = null;

    /**
     * Constructs a new ImageLoader.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param apiKey
     *            The API key.
     */
    public GoogleLoader(PApplet applet, String apiKey) {
	super(applet);

	this.apiKey = apiKey;
	this.searchEngineId = DEFAULT_SEARCH_ENGINE_ID;
    }

    /**
     * Constructs a new ImageLoader.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param apiKey
     *            The API key.
     * @param searchEngineId
     *            The search engine id.
     */
    public GoogleLoader(PApplet applet, String apiKey, String searchEngineId) {
	super(applet);

	this.apiKey = apiKey;
	this.searchEngineId = searchEngineId;
    }

    @Override
    public ImageList start(String searchParam, ImageList imageList, boolean runOnce, long delay, boolean lazyLoad) {
	if (thread != null) {
	    logger.info("Loader is already started.");
	    logger.info("The restart method will be used instead.");

	    return restart(searchParam, imageList, runOnce, delay, lazyLoad);
	} else {
	    runnable = new GoogleTask(applet, searchParam, imageList, apiKey, searchEngineId, runOnce, delay, lazyLoad);
	    thread = new Thread(runnable, "GoogleTask");

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

	runnable = new GoogleTask(applet, searchParam, imageList, apiKey, searchEngineId, runOnce, delay, lazyLoad);
	thread = new Thread(runnable, "GoogleTask");

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

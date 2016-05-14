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

package at.mukprojects.imageloader.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.imageloader.gif.GifData;
import at.mukprojects.imageloader.gif.GifList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents the FileGifLoader task.
 * 
 * @author Mathias Markl
 */
public class FileGifTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileGifTask.class);

    private PApplet applet;
    private String searchParam;
    private GifList gifList;
    
    private boolean lazyLoad;
    private boolean runOnce;
    private long delay;

    private volatile boolean running;

    /**
     * Constructs a new FileGifTask.
     * 
     * @param applet
     *            The Processing PApplet.
     * @param searchParam
     *            The search parameter.
     * @param gifList
     *            The ImageList object.
     * @param runOnce
     *            If the value is set to true, the loader will only run once.
     * @param delay
     *            The delay between two loading tasks. (milliseconds)
     * @param lazyLoad
     *            Indicates if the load process is lazy or not. Use lazy mode to
     *            save memory space.
     */
    public FileGifTask(PApplet applet, String searchParam, GifList gifList, boolean runOnce, long delay, boolean lazyLoad) {
	this.applet = applet;
	this.searchParam = searchParam;
	this.gifList = gifList;

	this.lazyLoad = lazyLoad;
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

		List<File> files = getFiles(searchParam);

		for (File file : files) {
		    String id = "File#" + file.getAbsolutePath();

		    String imgInfo = "";
		    imgInfo += "Name: " + file.getName() + "\n";
		    imgInfo += "Last Modified: " + new Date(file.lastModified()).toString();

		    long timestamp = new Date().getTime();
		    String imgUrl = file.getAbsolutePath();

		    PImage img = null;
		    if (!lazyLoad) {
			GifData gif = new GifData(id, imgInfo, timestamp, imgUrl, null);
			gif.loadGif();
			gifList.addImage(gif);
		    } else {
			gifList.addImage(new GifData(id, imgInfo, timestamp, imgUrl, null));
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
    
    private List<File> getFiles(String path) throws IOException {
	List<File> files = new ArrayList<File>();
	File pathLocation = new File(path);

	if (!pathLocation.exists()) {
	    throw new IOException("Path does not exist.");
	}

	if (pathLocation.isFile()) {
	    if (pathLocation.getName().endsWith(".gif")) {
		files.add(pathLocation);
	    }
	} else if (pathLocation.isDirectory()) {
	    File[] fileArray = pathLocation.listFiles();

	    for (int i = 0; i < fileArray.length; i++) {
		files.addAll(getFiles(fileArray[i].getAbsolutePath()));
	    }
	}

	return files;
    }
}

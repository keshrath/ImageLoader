package at.mukprojects.imageloader.instagram;

import org.jinstagram.Instagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mukprojects.imageloader.ImageLoader;
import at.mukprojects.imageloader.image.ImageList;
import processing.core.PApplet;

public class InstagramLoader extends ImageLoader {

    private static final Logger logger = LoggerFactory.getLogger(InstagramLoader.class);

    private Instagram instagram;

    private Thread thread = null;
    private InstagramTask runnable = null;

    public InstagramLoader(PApplet applet, String accessToken, String secret) {
	super(applet);
	
	instagram = new Instagram(accessToken, secret);
    }
    
    public InstagramLoader(PApplet applet, String clientId) {
	super(applet);
	
	instagram = new Instagram(clientId);
    }
    
    @Override
    public ImageList start(String searchParam) {
	return start(searchParam, new ImageList());
    }

    @Override
    public ImageList start(String searchParam, ImageList imageList) {
	if (thread != null) {
	    logger.info("Loader is already started.");
	    logger.info("The restart method will be used instead.");

	    return restart(searchParam, imageList);
	} else {
	    runnable = new InstagramTask(applet, searchParam, imageList, instagram);
	    thread = new Thread(runnable, "FlickrTask");

	    logger.info("Starting Thread: " + thread + "...");
	    thread.start();
	    logger.debug(thread + " successfully started.");

	    return imageList;
	}
    }

    @Override
    public ImageList restart(String searchParam) {
	return restart(searchParam, new ImageList());

    }

    @Override
    public ImageList restart(String searchParam, ImageList imageList) {
	logger.info("Stopping the current thread: " + thread + "...");
	runnable.stop();
	logger.debug(thread + " successfully stopped.");

	runnable = new InstagramTask(applet, searchParam, imageList, instagram);
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

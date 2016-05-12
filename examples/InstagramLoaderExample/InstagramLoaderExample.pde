import at.mukprojects.imageloader.*;
import at.mukprojects.imageloader.instagram.*;
import at.mukprojects.imageloader.image.*;

// API with access token
String accessToken = "2544932527.317b3d0.7a6f760d29554bad8c8bf9c26b160d63";

ImageLoader loader;
ImageList list;
Image img;

void setup() {
  size(800, 450);

  // API with access token
  loader = new InstagramLoader(this, accessToken, "");

  // API with client ID
   //loader = new InstagramLoader(this, clientId);

  list = loader.start("vienna", false, 60 * 1000);
}

void draw() {
  println(list.size());
  
  if (img == null) {
    println("Picking random image...");
    img = list.getRandom();
    if (img != null)
      println("Image picked.");
  } else {
    image(img.getImg(), 0, 0, width, height);
  }
}

void mousePressed() {
  img = list.getRandom();
}
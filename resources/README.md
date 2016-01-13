# Console

Console is an library for the [Processing](http://processing.org/) Development Environment (PDE).

The console redirects the standard output and error stream. So by using the `print` or `prinltn` functions the input will be automatically redirected to the console. There are multiple ways to draw the console on the sketch screen. In the default mode the console will be rendered on the bottom of the screen with a black background and a white text color.

## Example

```java
Console console;

void setup()
  // Initialize the console 
  console = new Console(this);
  
  // Start the console
  console.start();
}

void draw() {
  background(200);

  println("Frame: " + frameCount);
  
  // Draw the console to the screen.
  console.draw();
  
  // Print the console to the system out.
  console.print();
}
```

## How to install

Download Console library from [here](https://github.com/keshrath/Console/blob/master/distribution/Console/download/Console.zip?raw=true).

Unzip and copy it into the `libraries` folder in the Processing sketchbook. You will need to create this `libraries` folder if it does not exist.

To find (and change) the Processing sketchbook location on your computer, open the Preferences window from the Processing application (PDE) and look for the "Sketchbook location" item at the top.

By default the following locations are used for your sketchbook folder: 
  * For Mac users, the sketchbook folder is located inside `~/Documents/Processing` 
  * For Windows users, the sketchbook folder is located inside `My Documents/Processing`

The folder structure for library Console should be as follows:

```
Processing
  libraries
    Console
      examples
      	CustomConsole
      	 CustomConsole.pde
      	DefaultConsole
      	 DefaultConsole.pde
      library
        console.jar
        slf4j-api.jar
        slf4j-simple.jar
      reference
        docs
      src
      	../Console.java
      library.properties
```
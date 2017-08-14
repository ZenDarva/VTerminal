package com.valkryst.VTerminal;

import com.valkryst.VRadio.Radio;
import com.valkryst.VRadio.Receiver;
import com.valkryst.VTerminal.builder.PanelBuilder;
import com.valkryst.VTerminal.component.Component;
import com.valkryst.VTerminal.component.Screen;
import com.valkryst.VTerminal.misc.ColoredImageCache;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.imageio.ImageIO;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;

@EqualsAndHashCode
@ToString
public class Panel extends Canvas implements Receiver<String> {
    /** The width of the panel, in characters. */
    @Getter private int widthInCharacters;
    /** The height of the panel, in characters. */
    @Getter private int heightInCharacters;

    /** The screen being displayed on the panel. */
    @Getter private Screen screen;

    /** The radio being listened to. */
    @Getter private Radio<String> radio = new Radio<>();

    /** The image cache to retrieve character images from. */
    @Getter private final ColoredImageCache imageCache;

    /**
     * Constructs a new VTerminal.
     *
     * @param builder
     *         The builder to use.
     *
     * @throws NullPointerException
     *         If the builder is null.
     */
    public Panel(final @NonNull PanelBuilder builder) {
        this.widthInCharacters = builder.getWidthInCharacters();
        this.heightInCharacters = builder.getHeightInCharacters();

        int pixelWidth = widthInCharacters * builder.getFont().getWidth();
        int pixelHeight = heightInCharacters * builder.getFont().getHeight();

        this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));

        screen = builder.getScreen();

        radio.addReceiver("DRAW", this);

        imageCache = new ColoredImageCache(builder.getFont());
    }

    @Override
    public void receive(final String event, final String data) {
        if (event.equals("DRAW")) {
            draw();
        }
    }

    /** Draws every character of every row onto the canvas. */
    public void draw() {
        final BufferStrategy bs = this.getBufferStrategy();
        final Graphics2D gc;

        try {
            gc = (Graphics2D) bs.getDrawGraphics();
        } catch (final NullPointerException | IllegalStateException e) {
            // BufferStrategy may not have been created on the first call,
            // so just do a recursive call until it works.
            // This may be a bad idea.
            draw();
            return;
        }

        gc.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        gc.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        gc.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        gc.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Font characters are pre-rendered images, so no need for AA.
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // No-need for text rendering related options.
        gc.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // If alpha is used in the character images, we want computations related to drawing them to be fast.
        gc.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

        screen.draw(gc, imageCache);
        gc.dispose();
        bs.show();
    }

    /**
     * Draws the canvas onto an image.
     *
     * This calls the current screen's draw function, so the
     * screen may look a little different if there are new
     * updates to characters that haven't yet been drawn.
     *
     * This is an expensive operation as it essentially creates
     * an in-memory screen and draws each AsciiCharacter onto
     * that screen.
     *
     * @return
     *        An image of the canvas.
     */
    public BufferedImage screenshot() {
        return screen.screenshot(imageCache);
    }

    /**
     * Saves a screenshot of the canvas to a PNG file.
     *
     * @param filename
     *        The name of the file.
     *
     * @throws NullPointerException
     *         If the filename is null.
     *
     * @throws IllegalArgumentException
     *         If the filename is empty.
     *
     * @throws IOException
     *         If an I/O error occurred.
     */
    public void screenshotToFile(final @NonNull String filename) throws IOException {
        if (filename.isEmpty()) {
            throw new IllegalArgumentException("The filename cannot be null.");
        }

        final File file = new File(filename + ".png");

        if (file.exists() == false) {
            if (file.createNewFile()) {
                screenshotToFile(file, "PNG");
            } else {
                throw new IOException("Could not create the file " + filename + ".png.");
            }
        } else {
            screenshotToFile(file, "PNG");
        }
    }

    /**
     * Saves a screenshot of the canvas to a file.
     *
     * @param file
     *        The file.
     *
     * @param extension
     *        The extension of the file, the file should have a
     *        matching extension.
     *
     *        Ex: PNG or JPG
     *
     * @throws NullPointerException
     *         If the file or extension is null.
     *
     * @throws IllegalArgumentException
     *         If the filename is empty.
     *
     * @throws IOException
     *         If an error occurs during writing.
     */
    public void screenshotToFile(final @NonNull File file, final @NonNull String extension) throws IOException {
        if (extension.isEmpty()) {
            throw new IllegalArgumentException("The extension cannot be null.");
        }

        ImageIO.write(screenshot(), extension, file);
    }

    /**
     * Swaps-out the current screen for the new screen.
     *
     * @param newScreen
     *         The new screen to swap-in.
     *
     * @return
     *         The swapped-out screen.
     *
     * @throws NullPointerException
     *         If the new screen is null.
     */
    public Screen swapScreen(final @NonNull Screen newScreen) {
        if (newScreen == screen) {
            return screen;
        }

        final Screen oldScreen = screen;
        screen = newScreen;
        draw();
        return oldScreen;
    }

    /**
     * Adds a component to the current screen.
     *
     * Registers event listeners of the component to the panel,
     * if required.
     *
     * @param component
     *          The component.
     *
     * @throws NullPointerException
     *         If the new component is null.
     */
    public void addComponent(final @NonNull Component component) {
        screen.addComponent(component);

        if (component.getEventListeners().size() == 0) {
            component.createEventListeners(this);
        }

        for (final EventListener eventListener : component.getEventListeners()) {
            if (eventListener instanceof KeyListener) {
                this.addKeyListener((KeyListener) eventListener);
            }

            if (eventListener instanceof MouseListener) {
                this.addMouseListener((MouseListener) eventListener);
            }

            if (eventListener instanceof MouseMotionListener) {
                this.addMouseMotionListener((MouseMotionListener) eventListener);
            }
        }
    }


    /**
     * Adds one or more components to the current screen.
     *
     * @param components
     *        The components.
     *
     * @throws NullPointerException
     *         If the new components are null.
     */
    public void addComponents(final @NonNull Component ... components) {
        for (final Component component : components) {
            addComponent(component);
        }
    }

    /**
     * Removes a component from the current screen.
     *
     * Removes event listeners of the component from the panel,
     * if required.
     *
     * @param component
     *          The component.
     *
     * @throws NullPointerException
     *         If the new component is null.
     */
    public void removeComponent(final @NonNull Component component) {
        screen.removeComponent(component);

        for (final EventListener eventListener : component.getEventListeners()) {
            if (eventListener instanceof KeyListener) {
                this.removeKeyListener((KeyListener) eventListener);
            }

            if (eventListener instanceof MouseListener) {
                this.removeMouseListener((MouseListener) eventListener);
            }

            if (eventListener instanceof MouseMotionListener) {
                this.removeMouseMotionListener((MouseMotionListener) eventListener);
            }
        }
    }



    /**
     * Removes one or more components from the current screen.
     *
     * @param components
     *        The components.
     *
     * @throws NullPointerException
     *         If the new components are null.
     */
    public void removeComponents(final @NonNull Component ... components) {
        for (final Component component : components) {
            removeComponent(component);
        }
    }
}

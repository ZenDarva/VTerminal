package com.valkryst.VTerminal;

import com.valkryst.VRadio.Radio;
import com.valkryst.VRadio.Receiver;
import com.valkryst.VTerminal.builder.PanelBuilder;
import com.valkryst.VTerminal.component.Component;
import com.valkryst.VTerminal.component.Screen;
import com.valkryst.VTerminal.misc.ImageCache;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.EventListener;

@ToString
public class Panel extends Canvas implements Receiver<String> {
    /** The width of the panel, in characters. */
    @Getter private int widthInCharacters;
    /** The height of the panel, in characters. */
    @Getter private int heightInCharacters;

    /** The screen being displayed on the panel. */
    @Getter private Screen screen;

    /** The radio being listened to. */
    @Getter private Radio<String> radio;

    /** The image cache to retrieve character images from. */
    @Getter private final ImageCache imageCache;

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
        screen.setParentPanel(this);

        radio = builder.getRadio();
        radio.addReceiver("DRAW", this);

        imageCache = builder.getImageCache();
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

        do {
            do {
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
            } while (bs.contentsRestored()); // Repeat render if drawing buffer contents were restored.

            bs.show();
        } while (bs.contentsLost()); // Repeat render if drawing buffer was lost.
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

        // Unregister all of the old screen's components:
        screen.getComponents().forEach(component -> component.getEventListeners().forEach(this::removeListener));
        screen.setParentPanel(null);
        screen.setRadio(null);

        // Register all of the new screen's components:
        newScreen.getComponents().forEach(component -> component.createEventListeners(this));
        newScreen.getComponents().forEach(component -> component.getEventListeners().forEach(this::addListener));
        newScreen.setParentPanel(this);
        newScreen.setRadio(radio);

        final Screen oldScreen = screen;
        screen = newScreen;
        draw();
        return oldScreen;
    }

    /**
     * Adds a component to the current screen.
     *
     * @param component
     *          The component.
     *
     * @throws NullPointerException
     *         If the new component is null.
     */
    public void addComponent(final @NonNull Component component) {
        screen.addComponent(component);
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
     * @param component
     *          The component.
     *
     * @throws NullPointerException
     *         If the new component is null.
     */
    public void removeComponent(final @NonNull Component component) {
        screen.removeComponent(component);
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

    /**
     * Adds an event listener to the Panel.
     *
     * @param eventListener
     *        The event listener.
     *
     * @throws IllegalArgumentException
     *        If the event listener isn't supported by this function.
     */
    public void addListener(final EventListener eventListener) {
        if (eventListener instanceof KeyListener) {
            this.addKeyListener((KeyListener) eventListener);
            return;
        }

        if (eventListener instanceof MouseListener) {
            this.addMouseListener((MouseListener) eventListener);
            return;
        }

        if (eventListener instanceof MouseMotionListener) {
            this.addMouseMotionListener((MouseMotionListener) eventListener);
            return;
        }

        throw new IllegalArgumentException("The " + eventListener.getClass().getSimpleName() + " is not supported.");
    }

    /**
     * Removes an event listener from the Panel.
     *
     * @param eventListener
     *        The event listener.
     *
     * @throws IllegalArgumentException
     *        If the event listener isn't supported by this function.
     */
    public void removeListener(final EventListener eventListener) {
        if (eventListener instanceof KeyListener) {
            this.removeKeyListener((KeyListener) eventListener);
            return;
        }

        if (eventListener instanceof MouseListener) {
            this.removeMouseListener((MouseListener) eventListener);
            return;
        }

        if (eventListener instanceof MouseMotionListener) {
            this.removeMouseMotionListener((MouseMotionListener) eventListener);
            return;
        }

        throw new IllegalArgumentException("The " + eventListener.getClass().getSimpleName() + " is not supported.");
    }
}

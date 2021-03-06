package com.valkryst.VTerminal.component;

import com.valkryst.VTerminal.Screen;
import com.valkryst.VTerminal.Tile;
import com.valkryst.VTerminal.palette.AbstractPaletteFactory;
import com.valkryst.VTerminal.palette.RendererType;
import com.valkryst.VTerminal.palette.java2d.Java2DPalette;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ToString
public class Layer extends Component {
    /** The components on the layer. */
    private final List<Component> components = new ArrayList<>(0);

    /** The lock used to control access to the components. */
    private final ReentrantReadWriteLock componentsLock = new ReentrantReadWriteLock();

    /** The screen that the layer resides on. */
    @Setter private Screen rootScreen;

    /**
     * Constructs a new Layer at position (0, 0) with the default color palette.
     *
     * @param dimensions
     *          The dimensions of the layer.
     */
    public Layer(final @NonNull Dimension dimensions) {
        this(dimensions, null, null);
    }

    /**
     * Constructs a new Layer with the default color palette.
     *
     * @param dimensions
     *          The dimensions of the layer.
     *
     * @param position
     *          The position of the layer within it's parent.
     */
    public Layer(final @NonNull Dimension dimensions, final Point position) {
        this(dimensions, position, null);
    }

    /**
     * Constructs a new Layer.
     *
     * @param dimensions
     *          The dimensions of the layer.
     *
     * @param position
     *          The position of the layer within it's parent.
     *
     *          If null, then the position (0, 0) is used.
     *
     * @param palette
     *          The color palette to color the layer with.
     *
     *          If null, then the default color palette is used.
     */
    public Layer(final @NonNull Dimension dimensions, final Point position, Java2DPalette palette) {
        super(dimensions, (position == null ? new Point(0, 0) : position));

        if (palette == null) {
            try {
                palette = (Java2DPalette) AbstractPaletteFactory.getInstance().createPalette(RendererType.JAVA2D);
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }

        for (int y = 0 ; y < super.tiles.getHeight() ; y++) {
            for (int x = 0 ; x < super.tiles.getWidth() ; x++) {
                final Tile tile = super.getTileAt(x, y);

                if (tile != null) {
                    tile.setBackgroundColor(palette.getLayerPalette().getBackground());
                    tile.setForegroundColor(palette.getLayerPalette().getForeground());
                }
            }
        }
    }

    @Override
    public void setPalette(final Java2DPalette palette, final boolean redraw) {
        if (palette == null) {
            return;
        }

        this.palette = palette;

        // Change the color of the layer's tiles.
        final Color backgroundColor = palette.getLayerPalette().getBackground();
        final Color foregroundColor = palette.getLayerPalette().getForeground();

        for (int y = 0 ; y < super.tiles.getHeight() ; y++) {
            for (int x = 0 ; x < super.tiles.getWidth() ; x++) {
                final Tile tile = super.getTileAt(x, y);

                if (tile != null) {
                    tile.setBackgroundColor(backgroundColor);
                    tile.setForegroundColor(foregroundColor);
                }
            }
        }

        // Change child component color palettes.
        componentsLock.readLock().lock();

        for (final Component component : components) {
            component.setPalette(palette, false);
        }

        componentsLock.readLock().unlock();

        // Redraw if necessary
        if (redraw) {
            try {
                redrawFunction.run();
            } catch (final IllegalStateException ignored) {
                /*
                 * If we set the color palette before the screen is displayed, then it'll throw...
                 *
                 *      IllegalStateException: Component must have a valid peer
                 *
                 * We can just ignore it in this case, because the screen will be drawn when it is displayed for
                 * the first time.
                 */
            }
        }
    }

    /**
     * Adds one or more components to the layer.
     *
     * @param components
     *          The components.
     */
    public void addComponent(final Component ... components) {
        if (components == null) {
            return;
        }

        for (final Component component : components) {
            if (component == null) {
                return;
            }

            if (component.equals(this)) {
                return;
            }

            final int x = this.getTiles().getXPosition() + component.getTiles().getXPosition();
            final int y = this.getTiles().getYPosition() + component.getTiles().getYPosition();
            component.setBoundingBoxOrigin(x, y);

            if (component instanceof Layer) {
                final Queue<Component> subComponents = new ConcurrentLinkedQueue<>();
                subComponents.add(component);

                while(subComponents.size() > 0) {
                    final Component temp = subComponents.remove();

                    if (rootScreen != null) {
                        // Set the component's redraw function
                        temp.setRedrawFunction(rootScreen::draw);

                        // Create and add the component's listeners
                        temp.createEventListeners(rootScreen);

                        for (final EventListener listener : temp.getEventListeners()) {
                            rootScreen.addListener(listener);
                        }
                    }

                    // If the component's a layer, then we need to deal with it's sub-components.
                    if (temp instanceof Layer) {
                        ((Layer) temp).setRootScreen(rootScreen);
                        subComponents.addAll(((Layer) temp).getComponents());

                        updateChildBoundingBoxesOfLayer((Layer) temp);
                    }
                }
            }

            // Add the component
            componentsLock.writeLock().lock();

            super.tiles.addChild(component.getTiles());
            this.components.add(component);

            componentsLock.writeLock().unlock();

            /*
             * This line is relevant only when adding components to a Layer that's already on a Screen.
             *
             * Before a Layer is first added to a Screen, none of the sub-components will have had their listeners
             * initialized. The initialization is done when the Layer is being added to the Screen.
             */
            if (rootScreen != null) {
                // Set the component's redraw function
                component.setRedrawFunction(rootScreen::draw);

                for (final EventListener listener : component.getEventListeners()) {
                    rootScreen.addListener(listener);
                }
            }
        }
    }

    /**
     * Removes one or more components from the layer.
     *
     * @param components
     *          The components.
     */
    public void removeComponent(final Component ... components) {
        if (components == null) {
            return;
        }

        for (final Component component : components) {
            if (component == null) {
                return;
            }

            // Remove the component
            componentsLock.writeLock().lock();
            super.tiles.removeChild(component.getTiles());
            this.components.remove(component);
            componentsLock.writeLock().unlock();

            // Unset the component's redraw function
            component.setRedrawFunction(() -> {});

            // Remove the event listeners of the component and all of it's sub-components.
            final Queue<Component> subComponents = new ConcurrentLinkedQueue<>();
            subComponents.add(component);

            while (subComponents.size() > 0) {
                final Component temp = subComponents.remove();

                if (temp instanceof Layer) {
                    subComponents.addAll(((Layer) temp).getComponents());
                }

                for (final EventListener listener : temp.getEventListeners()) {
                    rootScreen.removeListener(listener);
                }
            }

            // Reset all of the tiles where the component used to be.
            final int startX = component.getTiles().getXPosition();
            final int startY = component.getTiles().getYPosition();

            final int endX = startX + component.getTiles().getWidth();
            final int endY = startY + component.getTiles().getHeight();

            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    final Tile tile = tiles.getTileAt(x, y);

                    if (tile != null) {
                        tile.reset();
                        tile.setBackgroundColor(rootScreen.getPalette().getDefaultBackground());
                        tile.setForegroundColor(rootScreen.getPalette().getDefaultForeground());
                    }
                }
            }
        }
    }

    /** Removes all components from the layer. */
    public void removeAllComponents() {
        final Queue<Component> subComponents = new ConcurrentLinkedQueue<>(components);

        while (subComponents.size() > 0) {
            removeComponent(subComponents.remove());
        }
    }

    /**
     * Recursively updates the bounding box origin points of a layer, all of it's child components, and all of
     * the child components of any layer that is a child to the screen.
     *
     * @param layer
     *          The layer.
     */
    private void updateChildBoundingBoxesOfLayer(final Layer layer) {
        if (layer == null) {
            return;
        }

        for (final Component component : layer.getComponents()) {
            final int x = layer.getBoundingBoxOrigin().x + component.getTiles().getXPosition();
            final int y = layer.getBoundingBoxOrigin().y + component.getTiles().getYPosition();
            component.setBoundingBoxOrigin(x, y);

            if (component instanceof Layer) {
                updateChildBoundingBoxesOfLayer((Layer) component);
            }
        }
    }

    /**
     * Retrieves all components which use a specific ID.
     *
     * @param id
     *          The ID to search for.
     *
     * @return
     *          All components using the ID.
     */
    public List<Component> getComponentsByID(final String id) {
        if (id == null || id.isEmpty() || components.size() == 0) {
            return new ArrayList<>(0);
        }

        final List<Component> results = new ArrayList<>(1);

        for (final Component component : components) {
            if (component.getId().equals(id)) {
                results.add(component);
            }
        }

        return results;
    }

    /**
     * Retrieves an unmodifiable list of the layer's components.
     *
     * @return
     *          An unmodifiable list of the layer's components.
     */
    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }
}

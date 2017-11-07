package com.valkryst.VTerminal.component;

import com.valkryst.VTerminal.AsciiCharacter;
import com.valkryst.VTerminal.AsciiString;
import com.valkryst.VTerminal.builder.component.ButtonBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.event.MouseEvent;

@ToString
public class Button extends Component {
    /** Whether or not the button is in the normal state. */
    private boolean isInNormalState = true;
    /** whether or not the button is in the hovered state. */
    private boolean isInHoveredState = false;
    /** Whether or not the button is in the pressed state. */
    private boolean isInPressedState = false;

    /** The background color for when the button is in the normal state. */
    @Getter private Color backgroundColor_normal;
    /** The foreground color for when the button is in the normal state. */
    @Getter private Color foregroundColor_normal;

    /** The background color for when the button is in the hover state. */
    @Getter private Color backgroundColor_hover;
    /** The foreground color for when the button is in the hover state. */
    @Getter private Color foregroundColor_hover;

    /** The background color for when the button is in the pressed state. */
    @Getter private Color backgroundColor_pressed;
    /** The foreground color for when the button is in the pressed state. */
    @Getter private Color foregroundColor_pressed;

    /** The function to run when the button is clicked. */
    @Getter @Setter @NonNull private Runnable onClickFunction;

    /**
     * Constructs a new AsciiButton.
     *
     * @param builder
     *         The builder to use.
     *
     * @throws NullPointerException
     *         If the builder is null.
     */
    public Button(final @NonNull ButtonBuilder builder) {
        super(builder);

        this.backgroundColor_normal = builder.getBackgroundColor_normal();
        this.foregroundColor_normal = builder.getForegroundColor_normal();

        this.backgroundColor_hover = builder.getBackgroundColor_hover();
        this.foregroundColor_hover = builder.getForegroundColor_hover();

        this.backgroundColor_pressed = builder.getBackgroundColor_pressed();
        this.foregroundColor_pressed = builder.getForegroundColor_pressed();

        this.onClickFunction = builder.getOnClickFunction();

        // Set the button's text:
        final char[] text = builder.getText().toCharArray();

        final AsciiCharacter[] characters = super.getString(0).getCharacters();

        for (int column = 0; column < characters.length; column++) {
            characters[column].setCharacter(text[column]);
        }

        // Set the button's colors (must be done after setting text):
        setColors(backgroundColor_normal, foregroundColor_normal);
    }

    @Override
    public void createEventListeners() {
        if (super.getEventListeners().size() > 0) {
            return;
        }

        super.createEventListeners();

        if (this instanceof CheckBox || this instanceof RadioButton) {
            return;
        }

        final MouseInputListener mouseListener = new MouseInputListener() {
            @Override
            public void mouseDragged(final MouseEvent e) {}

            @Override
            public void mouseMoved(final MouseEvent e) {
                if (intersects(e)) {
                    setStateHovered();
                } else {
                    setStateNormal();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (intersects(e)) {
                        setStatePressed();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (isInPressedState) {
                        onClickFunction.run();
                    }

                    if (intersects(e)) {
                        setStateHovered();
                    } else {
                        setStateNormal();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };

        super.getEventListeners().add(mouseListener);
    }

    /**
     * Sets the button state to normal if the current state allows for the normal
     * state to be set.
     */
    protected void setStateNormal() {
        boolean canSetState = isInNormalState == false;
        canSetState &= isInHoveredState || isInPressedState;

        if (canSetState) {
            isInNormalState = true;
            isInHoveredState = false;
            isInPressedState = false;

            setColors(backgroundColor_normal, foregroundColor_normal);
            transmitDraw();
        }
    }

    /**
     * Sets the button state to hovered if the current state allows for the normal
     * state to be set.
     */
    protected void setStateHovered() {
        boolean canSetState = isInNormalState || isInPressedState;
        canSetState &= isInHoveredState == false;

        if (canSetState) {
            isInNormalState = false;
            isInHoveredState = true;
            isInPressedState = false;

            setColors(backgroundColor_hover, foregroundColor_hover);
            transmitDraw();
        }
    }

    /**
     * Sets the button state to pressed if the current state allows for the normal
     * state to be set.
     */
    protected void setStatePressed() {
        boolean canSetState = isInNormalState || isInHoveredState;
        canSetState &= isInPressedState == false;

        if (canSetState) {
            isInNormalState = false;
            isInHoveredState = false;
            isInPressedState = true;

            setColors(backgroundColor_pressed, foregroundColor_pressed);
            transmitDraw();
        }
    }

    /**
     * Sets the back/foreground colors of all characters to the specified colors.
     *
     * @param backgroundColor
     *         The new background color.
     *
     * @param foregroundColor
     *         The new foreground color.
     *
     * @throws NullPointerException
     *         If the background or foreground color is null.
     */
    private void setColors(final @NonNull Color backgroundColor, final @NonNull Color foregroundColor) {
        for (final AsciiString s : getStrings()) {
            s.setBackgroundColor(backgroundColor);
            s.setForegroundColor(foregroundColor);
        }
    }

    /**
     * Sets the normal background color.
     *
     * @param color
     *         The new normal background color.
     *
     * @throws NullPointerException
     *         If the color is null.
     */
    public void setBackgroundColor_normal(final @NonNull Color color) {
        backgroundColor_normal = color;

        if (isInNormalState) {
            setColors(backgroundColor_normal, foregroundColor_normal);
        }
    }

    /**
     * Sets the normal foreground color.
     *
     * @param color
     *         The new normal foreground color.
     *
     * @throws NullPointerException
     *         If the color is null.
     */
    public void setForegroundColor_normal(final @NonNull Color color) {
        foregroundColor_normal = color;

        if (isInNormalState) {
            setColors(backgroundColor_normal, foregroundColor_normal);
        }
    }

    /**
     * Sets the hovered background color.
     *
     * @param color
     *         The new normal background color.
     *
     * @throws NullPointerException
     *         If the color is null.
     */
    public void setBackgroundColor_hover(final @NonNull Color color) {
        backgroundColor_hover = color;

        if (isInHoveredState) {
            setColors(backgroundColor_normal, foregroundColor_normal);
        }
    }

    /**
     * Sets the hovered foreground color.
     *
     * @param color
     *         The new hovered foreground color.
     *
     * @throws NullPointerException
     *         If the color is null.
     */
    public void setForegroundColor_hover(final @NonNull Color color) {
        foregroundColor_hover = color;

        if (isInHoveredState) {
            setColors(backgroundColor_normal, foregroundColor_normal);
        }
    }

    /**
     * Sets the pressed background color.
     *
     * @param color
     *         The new pressed background color.
     *
     * @throws NullPointerException
     *         If the color is null.
     */
    public void setBackgroundColor_pressed(final @NonNull Color color) {
        backgroundColor_pressed = color;

        if (isInPressedState) {
            setColors(backgroundColor_normal, foregroundColor_normal);
        }
    }

    /**
     * Sets the pressed foreground color.
     *
     * @param color
     *         The new pressed foreground color.
     *
     * @throws NullPointerException
     *         If the color is null.
     */
    public void setForegroundColor_pressed(final @NonNull Color color) {
        foregroundColor_pressed = color;

        if (isInPressedState) {
            setColors(backgroundColor_normal, foregroundColor_normal);
        }
    }
}
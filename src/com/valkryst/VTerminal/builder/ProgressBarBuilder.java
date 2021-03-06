package com.valkryst.VTerminal.builder;

import com.valkryst.VJSON.VJSON;
import com.valkryst.VTerminal.component.ProgressBar;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class ProgressBarBuilder extends ComponentBuilder<ProgressBar> {
    /** The character that represents an incomplete cell. */
    private char incompleteCharacter;
    /** The character that represents a complete cell. */
    private char completeCharacter;

    /**
     * Constructs a new ProgressBarBuilder and initializes it using the JSON representation of a progress bar
     * component.
     *
     * @param json
     *          The JSON representation of a progress bar component.
     */
    public ProgressBarBuilder(final JSONObject json) {
        super(json);

        if (json == null) {
            return;
        }

        final Character incompleteCharacter = VJSON.getChar(json, "Incomplete Character");
        final Character completeCharacter = VJSON.getChar(json, "Complete Character");

        this.incompleteCharacter = (incompleteCharacter == null ? '█' : incompleteCharacter);
        this.completeCharacter = (completeCharacter == null ? '█' : completeCharacter);
    }

    @Override
    public ProgressBar build() {
        checkState();
        return new ProgressBar(this);
    }

    @Override
    public void reset() {
        super.reset();

        super.setDimensions(10, 1);

        incompleteCharacter = '█';
        completeCharacter = '█';
    }
}

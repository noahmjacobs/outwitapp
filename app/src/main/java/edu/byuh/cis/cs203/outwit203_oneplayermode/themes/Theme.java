package edu.byuh.cis.cs203.outwit203_oneplayermode.themes;

import android.graphics.Canvas;
import android.graphics.Paint;

import edu.byuh.cis.cs203.outwit203_oneplayermode.Cell;
import edu.byuh.cis.cs203.outwit203_oneplayermode.Chip;
import edu.byuh.cis.cs203.outwit203_oneplayermode.Team;

/**
 * This is the abstract superclass of all the Themes
 */
public abstract class Theme {

    Paint lightCell;
    Paint darkCell;
    Paint neutralColor;
    Paint suggestedMoveColor;
    Paint goldLeaf;
    Paint darkChip;
    Paint lightChip;

    /**
     * Return the color for the cell, given a team
     * @param team
     * @return a paint object
     */
    public Paint getCellColor(Team team) {
        return switch (team) {
            case DARK -> darkCell;
            case LIGHT -> lightCell;
            default -> neutralColor;
        };
    }

    /**
     * return the color for a chip, given a team
     * @param team
     * @return a paint object
     */
    public Paint getChipColor(Team team) {
        if (team == Team.DARK) {
            return darkChip;
        } else {
            return lightChip;
        }
    }

    /**
     * return the color for the little dot on power chips
     * @return a paint object
     */
    public Paint getPowerDotColor() {
        return goldLeaf;
    }

    /**
     * return the color for the "halo" around selected chips
     * @return a paint object
     */
    public Paint getSelectedHighlightColor() {
        return goldLeaf;
    }

    /**
     * return the color for the little dots that show where the legal moves are
     * @return a paint object
     */
    public Paint getSuggestedMoveColor() {
        return suggestedMoveColor;
    }

    public abstract String getTeamName(Team team);
    public abstract int getBorderColor();
    public abstract int getTextColor();


    /**
     * This default implementation just draws a round chip.
     * Subclasses can override this to do different shapes
     * @param c
     * @param chippy
     */
    public void drawChip(Canvas c, Chip chippy, Paint blackLine) {
        chippy.draw(c, this, blackLine);
    }

    /**
     * This default implementation just draws a round dot.
     * Subclasses can override this to do different shapes
     * @param c
     * @param cel
     */
    public void drawSuggestedMove(Canvas c, Cell cel) {
        cel.drawHighlight(c, this);
    }
}

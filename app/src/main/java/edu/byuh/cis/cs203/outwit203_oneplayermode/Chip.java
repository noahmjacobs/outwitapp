package edu.byuh.cis.cs203.outwit203_oneplayermode;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.outwit203_oneplayermode.themes.Theme;

public class Chip {

    private Team team;
    private boolean power;
    private RectF currentLocation;
    private Cell currentCell;
    private boolean selected;
    //private final static Paint GOLD_LEAF, DARKCHIP, LIGHTCHIP;
    private PointF velocity;
    private Cell destination;

    /**
     * Static initializer for the Paint fields that are shared by all chip objects
     */
//    static {
//        GOLD_LEAF = new Paint();
//        GOLD_LEAF.setColor(Color.rgb(202,192,6));
//        GOLD_LEAF.setStyle(Paint.Style.FILL);
//        DARKCHIP = new Paint(GOLD_LEAF);
//        LIGHTCHIP = new Paint(GOLD_LEAF);
//        DARKCHIP.setColor(Color.rgb(98,78, 26));
//        LIGHTCHIP.setColor(Color.rgb(250,233, 188));
//    }

    /**
     * Constructor for the Chip class
     * @param t the team (light or dark) that the chip belongs to
     * @param p true if a power chip, false if normal
     */
    private Chip(Team t, boolean p) {
        team = t;
        power = p;
        currentLocation = new RectF();
        selected = false;
        velocity = new PointF();
        destination = null;
    }

    /**
     * A "simple factory" for instantiating normal (non-power) chips
     * @param t the chip's color, light or dark
     * @return a new normal chip
     */
    public static Chip normal(Team t) {
        return new Chip(t, false);
    }

    /**
     * A "simple factory" for instantiating power chips
     * @param t the chip's color, light or dark
     * @return a new power chip
     */
    public static Chip power(Team t) {
        return new Chip(t, true);
    }

    /**
     * Basic getter for the chip's location
     * @return
     */
    public RectF getBounds() {
        return currentLocation;
    }

    /**
     * Assign this chip to a particular cell
     * @param c the cell that this chip will reside in
     */
    public void setCell(Cell c) {
        if (currentCell != null) {  //kludgy
            currentCell.liberate();
        }
        currentCell = c;
        currentCell.setOccupied();
        velocity.set(0,0);
        currentLocation.set(currentCell.bounds());
    }

    public void select() {
        selected = true;
    }

    public void unselect() {
        selected = false;
    }

    /**
     * Checks whether a given cell is the same cell that the chip resides in
     * @param cel the cell to test
     * @return true if the given cell is this chip's current cell, false otherwise
     */
    public boolean areYouHere(Cell cel) {
        return (currentCell == cel);
    }

    /**
     * Draw the chip on the screen
     * @param c the Canvas object
     * @param theme, the theme used for the current color scheme
     */
    public void draw(Canvas c, Theme theme, Paint blackLine) {
        if (selected) {
            c.drawCircle(currentLocation.centerX(), currentLocation.centerY(),
                    currentLocation.width()*0.6f, theme.getSelectedHighlightColor());
        }
        c.drawCircle(currentLocation.centerX(), currentLocation.centerY(),
                    currentLocation.width()*0.45f, theme.getChipColor(team));
        c.drawCircle(currentLocation.centerX(), currentLocation.centerY(),
                currentLocation.width()*0.45f, blackLine);

        if (power) {
            c.drawCircle(currentLocation.centerX(), currentLocation.centerY(),
                    currentLocation.width()*0.2f, theme.getPowerDotColor());
        }
    }

    /**
     * Tests whether the given (x,y) coordinate is within this chip's bounding box
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if (x,y) is inside this chip's bounding box, false otherwise
     */
    public boolean contains(float x, float y) {
        return currentLocation.contains(x,y);
    }

    /**
     * Basic getter method for the chip's color (light or dark)
     * @return the color
     */
    public Team getTeam() {
        return team;
    }

    /**
     * is the chip in its "home corner"?
     * @return true if the chip is in its home corner; false otherwise
     */
    public boolean isHome() {
        return this.team == currentCell.color();
    }

    /**
     * simple getter method for the power field
     * @return true if this is a power chip; false otherwise.
     */
    public boolean isPowerChip() {
        return power;
    }

    /**
     * simple getter for the current cell
     * @return the current cell
     */
    public Cell getHostCell() {
        return currentCell;
    }

    /**
     * Is animation currently happening?
     * @return true if the token is currently moving (i.e. has a non-zero velocity); false otherwise.
     */
    public boolean isMoving() {
        return (velocity.x != 0 || velocity.y != 0);
    }

    /**
     * Assign a destination location to the token
     * @param c the cell where the token should stop
     */
    public void setDestination(Cell c/*, boolean animated*/) {
        destination = c;
        //if (animated) {
            PointF dir = currentCell.directionTo(destination);
            velocity.x = dir.x * currentLocation.width() * 0.333f;
            velocity.y = dir.y * currentLocation.width() * 0.333f;
        //} else {
       //     setCell(destination);
        //}
    }

    /**
     * Move the token by its current velocity.
     * Stop when it reaches its destination location.
     */
    public boolean animate() {
        boolean justFinished = false;
        if (isMoving()) {
            float dx = destination.bounds().left - currentLocation.left;
            float dy = destination.bounds().top - currentLocation.top;
            if (PointF.length(dx, dy) < currentLocation.width() / 2) {
                setCell(destination);
                justFinished = true;
            }
            currentLocation.offset(velocity.x, velocity.y);
        }
        return justFinished;
    }

    /**
     * Returns an arraylist with all possible moves
     * for the currently-selected chip
     * @param cellz all the cells
     * @return a new arraylist, containing the cells this chip could move to.
     */
    public List<Cell> findPossibleMoves(Cell[][] cellz) {
        List<Cell> legalMoves =  new ArrayList<>();
        int newX, newY;
        //final Cell currentCell = selected.getHostCell();
        if (power) {
            //can we go right?
            for (newX = currentCell.x()+1; newX < 9; newX++) {
                Cell candidate = cellz[newX][currentCell.y()];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                } else {
                    break;
                }
            }
            //can we go left?
            for (newX = currentCell.x()-1; newX >= 0; newX--) {
                Cell candidate = cellz[newX][currentCell.y()];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                } else {
                    break;
                }
            }
            //can we go up?
            for (newY = currentCell.y()-1; newY >= 0; newY--) {
                Cell candidate = cellz[currentCell.x()][newY];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                } else {
                    break;
                }
            }
            //can we go down?
            for (newY = currentCell.y()+1; newY < 10; newY++) {
                Cell candidate = cellz[currentCell.x()][newY];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                } else {
                    break;
                }
            }
            //can we go up/right diagonal?
            newX = currentCell.x()+1;
            newY = currentCell.y()-1;
            while (newX < 9 && newY >= 0) {
                Cell candidate = cellz[newX][newY];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                    newX++;
                    newY--;
                } else {
                    break;
                }
            }
            //can we go up/left diagonal?
            newX = currentCell.x()-1;
            newY = currentCell.y()-1;
            while (newX >= 0 && newY >= 0) {
                Cell candidate = cellz[newX][newY];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                    newX--;
                    newY--;
                } else {
                    break;
                }
            }
            //can we go down/right diagonal?
            newX = currentCell.x()+1;
            newY = currentCell.y()+1;
            while (newX < 9 && newY < 10) {
                Cell candidate = cellz[newX][newY];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                    newX++;
                    newY++;
                } else {
                    break;
                }
            }
            //can we go down/left diagonal?
            newX = currentCell.x()-1;
            newY = currentCell.y()+1;
            while (newX >= 0 && newY < 10) {
                Cell candidate = cellz[newX][newY];
                if (candidate.isLegalMove(this)) {
                    legalMoves.add(candidate);
                    newX--;
                    newY++;
                } else {
                    break;
                }
            }

            //REGULAR CHIPS (not power chips)
        } else {
            //can we go right?
            Cell vettedCandidate = null;
            for (newX = currentCell.x()+1; newX < 9; newX++) {
                Cell candidate = cellz[newX][currentCell.y()];
                if (candidate.isLegalMove(this)) {
                    vettedCandidate = candidate;
                } else {
                    break;
                }
            }
            if (vettedCandidate != null) {
                legalMoves.add(vettedCandidate);
            }
            //can we go left?
            vettedCandidate = null;
            for (newX = currentCell.x()-1; newX >= 0; newX--) {
                Cell candidate = cellz[newX][currentCell.y()];
                if (candidate.isLegalMove(this)) {
                    vettedCandidate = candidate;
                } else {
                    break;
                }
            }
            if (vettedCandidate != null) {
                legalMoves.add(vettedCandidate);
            }

            //can we go up?
            vettedCandidate = null;
            for (newY = currentCell.y()-1; newY >= 0; newY--) {
                Cell candidate = cellz[currentCell.x()][newY];
                if (candidate.isLegalMove(this)) {
                    vettedCandidate = candidate;
                } else {
                    break;
                }
            }
            if (vettedCandidate != null) {
                legalMoves.add(vettedCandidate);
            }

            //can we go down?
            vettedCandidate = null;
            for (newY = currentCell.y()+1; newY < 10; newY++) {
                Cell candidate = cellz[currentCell.x()][newY];
                if (candidate.isLegalMove(this)) {
                    vettedCandidate = candidate;
                } else {
                    break;
                }
            }
            if (vettedCandidate != null) {
                legalMoves.add(vettedCandidate);
            }
        }
        return legalMoves;
    }

}
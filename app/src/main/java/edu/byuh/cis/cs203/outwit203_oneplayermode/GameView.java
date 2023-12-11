package edu.byuh.cis.cs203.outwit203_oneplayermode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.byuh.cis.cs203.outwit203_oneplayermode.activities.MainActivity;
import edu.byuh.cis.cs203.outwit203_oneplayermode.activities.Prefs;
import edu.byuh.cis.cs203.outwit203_oneplayermode.themes.Theme;

public class GameView extends View {

    private Cell[][] cellz;
    private List<Chip> chipz;
    private List<Cell> legalMoves;
    private Chip selected;
    private boolean initialized = false;
    private Timer tim;
    private RectF undoButton;
    private Bitmap undoIcon;
    private Stack<Move> undoStack;
    private Team currentPlayer;
    private Team computerPlayer;
    private Paint textFont;
    private Paint blackLine;
    private Theme theme;
    private Bot ai;
    //private boolean undoing;
    private int undoHack;

    private class Timer extends Handler {

        private boolean paused;
        private int delay;

        /**
         * Start the timer
         */
        public void resume() {
            paused = false;
            sendMessageDelayed(obtainMessage(), delay);
        }

        /**
         * pause the timer
         */
        public void pause() {
            paused = true;
            removeCallbacksAndMessages(null);
        }

        /**
         * Instantiate the timer and start it running
         */
        public Timer() {
            delay = Prefs.animationSpeed(getContext());
            resume();
        }

        /**
         * The most important method in the Timer class.
         * Here, we put all the code that needs to happen at each clock-tick
         * @param m the Message object (unused)
         */
        @Override
        public void handleMessage(Message m) {
            for (var c : chipz) {
                if (c.animate() == true) {
                    swapPlayers();
                    checkForWinner();
                    if (undoHack > 0) {
                        undoLastMove();
                    } else if (currentPlayer == computerPlayer) {
                        doComputerMove();
                    }
                    break;
                }
            }
            invalidate();
            if (!paused) {
                sendMessageDelayed(obtainMessage(), delay);
            }
        }
    }

    private void doComputerMove() {
        Move aiMove = ai.getMove(cellz);
        //pretendToThink();
        undoStack.push(aiMove);
        Chip chipToMove = getChipAt(aiMove.src());
        chipToMove.setDestination(aiMove.dest());
    }


    /**
     * Our constructor. This is where we initialize our fields.
     * @param c Context is the superclass of Activity. Thus, this
     *          parameter is basically a polymorphic reference to
     *          whatever Activity created this View... in this case,
     *          it's our MainActivity.
     */
    public GameView(Context c) {
        super(c);
        theme = Prefs.getTheme(c);
        blackLine = new Paint();
        blackLine.setColor(theme.getBorderColor());
        blackLine.setStyle(Paint.Style.STROKE);
        cellz = new Cell[9][10]; //[x][y]
        chipz = new ArrayList<>();
        legalMoves = new ArrayList<>();
        selected = null;
        undoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.undo);
        undoStack = new Stack<>();
        textFont = new Paint();
        textFont.setColor(theme.getTextColor());
        textFont.setStyle(Paint.Style.FILL);
        computerPlayer = Prefs.getWhosePlayingPreference(c);
        undoHack = 0;
    }

    /**
     * onDraw is roughly equivalent to the paintComponent method in the
     * JPanel class from the standard Java API. Override it to perform
     * custom drawing for the user interface
     * @param c the Canvas object, which contains the methods we need for
     *          drawing basic shapes. Similar to the Graphics class in
     *          the standard Java API.
     */
    @Override
    public void onDraw(Canvas c) {
        final var w = c.getWidth();
        final var h = c.getHeight();
        final float cellSize = w/9f;
        if (! initialized) {
            blackLine.setStrokeWidth(cellSize * 0.03f);

            //create all the cells
            for (var i=0; i<10; i++) {
                for (var j=0; j<9; j++) {
                    var color = Team.NEUTRAL;
                    if (i<3 && j>5) {
                        color = Team.LIGHT;
                    } else if (i>6 && j<3) {
                        color = Team.DARK;
                    }
                    cellz[j][i] = new Cell(j,i,color,cellSize);
                }
            }

            //this creates all the chips and puts them in their starting positions
            resetGameboard();

            //create the undo button
            undoButton = new RectF(w-cellSize, h-cellSize, w, h);

            //set the "current player" text size to something reasonable
            textFont.setTextSize(MainActivity.findThePerfectFontSize(cellSize/2));

            //instantiate the timer
            tim = new Timer();

            initialized = true;
        }

        //draw the orange background
        c.drawRect(0,0,cellSize*9,cellSize*10,theme.getCellColor(Team.NEUTRAL));

        //draw the light brown corner
        c.drawRect(cellSize*6,0, cellSize*10, cellSize*3, theme.getCellColor(Team.LIGHT));

        //draw the dark brown color
        c.drawRect(0, cellSize*7, cellSize*3, cellSize*10, theme.getCellColor(Team.DARK));

        //draw a nice solid border around the whole thing
        c.drawRect(0,0,cellSize*9,cellSize*10, blackLine);

        //draw horizontal black lines
        for (int i=1; i<=9; i++) {
            c.drawLine(0, i*cellSize, cellSize*9, cellSize*i, blackLine);
        }
        //draw vertical black lines
        for (int i=1; i<=8; i++) {
            c.drawLine(i*cellSize, 0, i*cellSize, cellSize*10, blackLine);
        }

        //draw the chips
        chipz.forEach(ch -> ch.draw(c, theme, blackLine));

//          DEBUGGING
//        c.drawText(""+undoHack, 100, 100, textFont);
//        final Cell lightCorner = cellz[8][0];
//        final Cell darkCorner = cellz[0][9];
//        if (selected != null) {
//            if (currentPlayer == Team.DARK) {
//                int md = selected.getHostCell().manhattanDistance(darkCorner);
//                selected.getHostCell().drawNumber(c, md, textFont);
//            } else {
//                int md = selected.getHostCell().manhattanDistance(lightCorner);
//                selected.getHostCell().drawNumber(c, md, textFont);
//            }
//        }
//        for (Cell lm : legalMoves) {
//            if (currentPlayer == Team.DARK) {
//                int md = lm.manhattanDistance(darkCorner);
//                lm.drawNumber(c, md, textFont);
//            } else {
//                int md = lm.manhattanDistance(lightCorner);
//                lm.drawNumber(c, md, textFont);
//            }
//        }

        //draw the highlights
        legalMoves.forEach(lm -> lm.drawHighlight(c, theme));

        //draw the undo button
        c.drawRoundRect(undoButton, undoButton.width()*0.1f, undoButton.width()*0.1f, blackLine);
        c.drawBitmap(undoIcon, null, undoButton, null);

        //draw the "current player" text
        String reminder;
        reminder = theme.getTeamName(currentPlayer) + "'s Turn";
        c.drawText(reminder, cellSize/2, cellSize*11, textFont);
    }

    /**
     * This method gets called anytime the user touches the screen
     * @param m the object that holds information about the touch event
     * @return true (to prevent the touch event from getting passed to other objects. Please refer to the Chain of Responsibility design pattern.
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        final var x = m.getX();
        final var y = m.getY();
        if (m.getAction() == MotionEvent.ACTION_DOWN) {

            //ignore touch events while a chip is moving
            if (anyMovingChips()) {
                return true;
            }

            //did the user tap the undo button?
            if (undoButton.contains(x,y)) {
                if (onePlayerMode()) {
                    undoHack = 2;
                }
                undoLastMove();
            }

            //did the user tap one of the "legal move" cells?
            for (var cell : legalMoves) {
                if (cell.contains(x,y)) {
                    final var moov = new Move(selected.getHostCell(), cell);
                    undoStack.push(moov);
                    selected.setDestination(cell/*, Prefs.getAnimationPrefs(getContext())*/);
                    selected = null;
                    //swapPlayers();
                    break;
                }
            }

            //first, clear old selections
//            for (var chippy : chipz) {
//                chippy.unselect();
//            }
            chipz.forEach(ch -> ch.unselect());
            legalMoves.clear();

            //now, check which chip got tapped
            for (var chippy : chipz) {
                if (chippy.contains(x, y) && currentPlayer == chippy.getTeam()) {
                    //if user taps the selected chip, unselect it
                    if (selected == chippy) {
                        selected.unselect();
                        selected = null;
                        legalMoves.clear();
                        break;
                    }
                    selected = chippy;
                    chippy.select();
                    legalMoves.addAll(selected.findPossibleMoves(cellz));
                    break;
                }
            }
            invalidate();
        }
        return true;
    }

    /**
     * change the current player after each move
     */
    private void swapPlayers() {
        if (currentPlayer == Team.LIGHT) {
            currentPlayer = Team.DARK;
        } else {
            currentPlayer = Team.LIGHT;
        }
        //if animation is disabled, check for winners now, instead of
        //when the animation finishes.
//        if (Prefs.getAnimationPrefs(getContext()) == false) {
//            checkForWinner();
//        }
    }

    /**
     * Determine if any player has won yet. If there is a winner,
     * find out who it is, and initiate the endgame dialog.
     */
    private void checkForWinner() {
        int lightCount = 0;
        int darkCount = 0;
        for (var chippy : chipz) {
            if (chippy.isHome()) {
                if (chippy.getTeam() == Team.LIGHT) {
                    lightCount++;
                } else {
                    darkCount++;
                }
            }
        }
        if (lightCount == 9 || darkCount == 9) {
            String winner;
            if (lightCount > darkCount) {
                winner = theme.getTeamName(Team.LIGHT);
            } else {
                winner = theme.getTeamName(Team.DARK);
            }
            showVictoryMessage(winner);
        }
    }

    /**
     * Pop up a dialog box announcing the winner, and give the player(s) the option to play again
     * @param winner the name of the winning team
     */
    private void showVictoryMessage(String winner) {
        tim.pause();
        AlertDialog.Builder ab = new AlertDialog.Builder((getContext()));
        ab.setTitle("Game Over!")
            .setMessage(winner + " wins!")
            .setPositiveButton("Play again?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int i) {
                    resetGameboard();
                    tim.resume();
                }
            })
            .setNegativeButton("Quit", (d,i) -> ((Activity)getContext()).finish())
            .setCancelable(false);
        AlertDialog box = ab.create();
        box.show();
    }

    /**
     * Initializes the gameboard back to the starting state
     */
    private void resetGameboard() {
        undoStack.clear();
        legalMoves.clear();
        chipz.clear();
        selected = null;

        //clear all the cells
        for (var i=0; i<10; i++) {
            for (var j=0; j<9; j++) {
                cellz[j][i].liberate();
            }
        }

        //refactored this to a helper method since the logic was
        //getting pretty hairy, what with all the preferences...
        createChips();

        currentPlayer = Prefs.whoGoesFirst(getContext());
        ai = new Bot(computerPlayer, chipz);
        if (currentPlayer == computerPlayer) {
            doComputerMove();
        }
    }

    /**
     * Helper method for resetGameboard()
     */
    private void createChips() {
        int layout = Prefs.chipLayout(getContext());
        int chipset = Prefs.chipset(getContext());
        for (var i=0; i<9; i++) {
            Chip dark = null;
            Chip light = null;

            //first, instantiate a dark and a light chip
            if (chipset == Prefs.ALL_POWER || (i==4 && chipset == Prefs.STANDARD_CHIPSET)) {
                dark = Chip.power(Team.DARK);
                light = Chip.power(Team.LIGHT);
            } else {
                dark = Chip.normal(Team.DARK);
                light = Chip.normal(Team.LIGHT);
            }

            //second, figure out where to put them.
            //this depends on which layout algorithm the user chose.
            if (layout == Prefs.STANDARD_LAYOUT) {
                dark.setCell(cellz[i][i]);
                light.setCell(cellz[i][i + 1]);
            } else if (layout == Prefs.RANDOMIZED_LAYOUT) {
                for (Chip ch : new Chip[]{dark, light}) {
                    while (true) {
                        int x = (int) (Math.random() * 9);
                        int y = (int) (Math.random() * 10);
                        Cell candidate = cellz[x][y];
                        if (candidate.isFree() && candidate.color() == Team.NEUTRAL) {
                            ch.setCell(candidate);
                            break;
                        }
                    }
                }
            } else {
                //CENTRALIZED LAYOUT
                //(Yes, I know it's terribly inefficient to instantiate these arrays
                //inside a loop. But since this section of code runs so infrequently,
                //I decided to sacrifice efficiency for simplicity.)
                int[] darkX = {2,3,4,5,6,6,6,5,5};
                int[] liteX = {6,5,4,3,2,2,2,3,3};
                int[] darkY = {3,3,3,3,3,4,5,5,4};
                int[] liteY = {6,6,6,6,6,5,4,4,5};
                dark.setCell(cellz[darkX[i]][darkY[i]]);
                light.setCell(cellz[liteX[i]][liteY[i]]);
            }

            //third, add the two chips to their respective arraylists
            chipz.add(dark);
            chipz.add(light);
        }

    }

    /**
     * Pop the most recent move off the stack
     * and execute it in reverse.
     * Show a toast if the stack is empty.
     */
    public void undoLastMove() {

        if (anyMovingChips()) {
            return;
        }

        if (selected != null) {
            //un-highlight the selected chip before undo-ing
            selected.unselect();
            legalMoves.clear();
        }

        if (undoStack.isEmpty()) {
            var toasty = Toast.makeText(getContext(), "No moves to undo!", Toast.LENGTH_LONG);
            toasty.show();
            undoHack = 0;
        } else {
            undoHack--;
            ai.decrement();
            Move move = undoStack.pop();
            Cell current = move.dest();
            Cell moveTo = move.src();
            selected = getChipAt(current);
            selected.setDestination(moveTo/*, Prefs.getAnimationPrefs(getContext())*/);
            selected.unselect();
            selected = null;
        }
    }

    /**
     * Given a cell, find the chip that's on it.
     * @param cel the Cell we're investigating
     * @return the Chip currently sitting on that Cell, or null if the cell is vacant.
     */
    private Chip getChipAt(Cell cel) {
        return chipz.stream().filter(ch -> ch.areYouHere(cel)).findFirst().get();
    }

    private boolean onePlayerMode() {
        return computerPlayer != Team.NEUTRAL;
    }

    /**
     * checks if a chip is moving
     * @return true if a chip is currently moving; false otherwise.
     */
    private boolean anyMovingChips() {
        return chipz.stream().anyMatch(c -> c.isMoving());
    }

    private void pretendToThink() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            //TODO can this ever happen?
            Log.d("CS203", "can this ever happen?");
        }
    }
}
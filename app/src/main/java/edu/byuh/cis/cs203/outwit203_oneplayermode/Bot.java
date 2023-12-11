package edu.byuh.cis.cs203.outwit203_oneplayermode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Bot {
    private Team me;
    private List<Chip> mychips;
    private int numTurns;

    /**
     * Initialize the AI
     * @param t which team the AI plays for
     * @param chipz all the chips on the gameboard (the AI will only consider its own chips)
     */
    public Bot(Team t, List<Chip> chipz) {
        numTurns = 0;
        me = t;
        mychips = chipz.stream().filter(c -> c.getTeam() == me).collect(Collectors.toList());
    }

    /**
     * Collect all possible moves that any chip in my team can make.
     * For each move, measure the manhattan distance from the move
     * to the corner. choose the move with the smallest positive distance.
     * But for the first 3 moves, just move randomly.
     * @param cellz a 2D array of all the cells on the gameboard
     * @return a Move object that encapsulates the move chosen by the AI
     */
    public Move getMove(Cell[][] cellz) {
        final Cell lightCorner = cellz[8][0];
        final Cell darkCorner = cellz[0][9];
        numTurns++;
        List<Move> allMoves = new ArrayList<>();
        List<Move> candidateMoves = new ArrayList<>();

        for (Chip eachChip : mychips) {
            List<Cell> possibleMoves = eachChip.findPossibleMoves(cellz);
            possibleMoves.forEach(dest -> allMoves.add(new Move(eachChip.getHostCell(), dest)));
        }
        for (Move mv : allMoves) {
            int distanceFromOriginToCorner;
            int distanceFromDestinationToCorner;
            if (me == Team.DARK) {
                distanceFromDestinationToCorner = mv.dest().manhattanDistance(darkCorner);
                distanceFromOriginToCorner = mv.src().manhattanDistance(darkCorner);
            } else {
                distanceFromDestinationToCorner = mv.dest().manhattanDistance(lightCorner);
                distanceFromOriginToCorner = mv.src().manhattanDistance(lightCorner);
            }
            //for now, only consider moves that get us closer to home
            if (distanceFromDestinationToCorner < distanceFromOriginToCorner) {
                mv.setDistance(distanceFromDestinationToCorner);
                candidateMoves.add(mv);
            }
        }

        if (numTurns < 4 || candidateMoves.isEmpty()) {
            //submit a random move
            Collections.shuffle(allMoves);
            return allMoves.get(0);
        } else {
            Collections.sort(candidateMoves);
            return candidateMoves.get(0);
        }

    }

    /**
     * Helper method used by the undo functionality. Since the AI keeps track of how many moves
     * it's played thus far, and undo modifies that, this method allows the undo function to
     * decrement the AI's internal counter.
     */
    public void decrement() {
        numTurns--;
    }
}


//CODE GRAVEYARD - HERE BE EARLIER ATTEMPTS AT MY AI ALGORITHM

//    public static Move getMove(Team me, List<Chip> chipz, Map<String, Cell> cellz) {
//        //pick a random chip to move
//        while (true) {
//            //Optional<Chip> tmp = chipz.stream().filter(c -> c.getColor() == me).findAny();
//            List<Chip> possibleChips = chipz.stream().filter(c -> c.getColor() == me).collect(Collectors.toList());
//            Chip randomChip = possibleChips.get((int)(Math.random()*possibleChips.size()));
//            String src = randomChip.id();
//            //Optional<Cell> tmp2 = randomChip.possibleMoves(cellz).stream().findAny();
//            List<Cell> possibleMoves = randomChip.possibleMoves(cellz);
//            if (possibleMoves.size() > 0) {
//                Cell pickOne = possibleMoves.get((int)(Math.random()*possibleMoves.size()));
//                String dest = pickOne.id();
//                return new Move(src, dest);
//            }
//            //else loop again
//        }
//    }

//    public static Move getMove(Team me, List<Chip> chipz, Cell[][] cellz) {
//        //pick a random chip to move
//        Map<Chip, List<Cell>> allMoves = new HashMap<>();
//        List<Move> candidateMoves = new ArrayList<>();
//        while (true) {
//            List<Chip> possibleChips = chipz.stream().filter(c -> c.getTeam() == me).collect(Collectors.toList());
//            for (Chip eachChip : possibleChips) {
//                List<Cell> possibleMoves = eachChip.findPossibleMoves(cellz);
//                allMoves.put(eachChip, possibleMoves);
//            }
//            for (Chip eachChip : possibleChips) {
//                if (eachChip.isPowerChip() == false && eachChip.isHome() == false) {
//                    List<Cell> possibleMoves = allMoves.get(eachChip);
//                    for (Cell c : possibleMoves) {
//                        if (c.color() == me) {
//                            candidateMoves.add(new Move(eachChip.getHostCell(), c));
//                        }
//                    }
//                }
//            }
//            if (candidateMoves.isEmpty()) {
//                //if there are no "go home" moves, then pick a random one.
//                Chip randomChip = possibleChips.get((int)(Math.random()*possibleChips.size()));
//                List<Cell> possibleMoves = allMoves.get(randomChip);
//                if (possibleMoves.size() > 0) {
//                    Cell pickOne = possibleMoves.get((int)(Math.random()*possibleMoves.size()));
//                    candidateMoves.add(new Move(randomChip.getHostCell(), pickOne));
//                }
//            }
//            if (!candidateMoves.isEmpty()) {
//                Collections.shuffle(candidateMoves);
//                return candidateMoves.get(0);
//            }
//        }
//        //else loop again
//    }

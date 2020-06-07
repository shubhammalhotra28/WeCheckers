package com.webcheckers.util;

import com.webcheckers.model.*;

import java.util.ArrayList;

public class ValidationHelper {

    /**
     * find how many pieces of given color are on the board
     */
    public int piecesOfColor(BoardView board, Piece.Color color) {
        int pieces = 0;
        //iterate through each space
        for (Row row: board) {
            for (Space space: row) {
                Piece piece = space.getPiece();
                Piece.Color pieceColor = null;
                //if the piece is of the desired color, add to count
                if (piece != null){
                    pieceColor = piece.getColor();
                }
                if (pieceColor==color) pieces++;
            }
        }
        return pieces;
    }

    /**
     * scan the board for all possible moves a given color can make
     * add moves, jumps, and double jumps to the respective list
     */
    public void scanBoard(BoardView board, Piece.Color activeColor, ArrayList<Move> possibleMoves,
                          ArrayList<ArrayList<Move>> possibleJumps){
        //get all the valid single moves and the first move of each jump sequence and add them to the lists
        for (Row row: board){
            int rowIndx = row.getIndex();
            for (Space space: row){
                int colIndx = space.getCellIdx();
                Space.SPACECOLOR spaceColor = space.getSpaceColor();
                Piece piece = space.getPiece();
                Piece.Color pieceColor = null;
                if (piece != null){
                    pieceColor = piece.getColor();
                }
                if (spaceColor == Space.SPACECOLOR.DARK && pieceColor == activeColor){
                    Position start = new Position(rowIndx, colIndx);
                    initMoves(start, board, activeColor, piece.getType(), possibleMoves, possibleJumps);
                }
            }
        }

        //add any additional jumps until you did not add anymore jumps
        while (true){
            boolean jumpsLeft = addMoreJumps(board, activeColor, possibleJumps);
            if (!jumpsLeft){
                break;
            }
        }
    }


    /**
     * Get all the valid single moves and the first move of each jump sequence,
     * add these to their respective lists.
     * @param start the starting position
     * @param board the boardview to scan
     * @param activeColor the active color (the color of the player with the active turn)
     * @param pieceType the piece type that is being inspected for moves
     * @param possibleMoves the list of normal moves
     * @param possibleJumps the list of move sequences
     */
    private void initMoves(Position start, BoardView board, Piece.Color activeColor, Piece.Type pieceType, ArrayList<Move> possibleMoves, ArrayList<ArrayList<Move>> possibleJumps){
        int startRow = start.getRow();
        int startCol = start.getCell();

        int positionMult = 1;
        if (activeColor == Piece.Color.WHITE){
            positionMult = -1;
        }

        //get all of the Positions needed
        Position N1E1 = new Position(startRow-(1*positionMult), startCol+1);
        Position N1W1 = new Position(startRow-(1*positionMult), startCol-1);
        Position N2E2 = new Position(startRow-(2*positionMult), startCol+2);
        Position N2W2 = new Position(startRow-(2*positionMult), startCol-2);
        Position S1E1 = new Position(startRow+(1*positionMult), startCol+1);
        Position S1W1 = new Position(startRow+(1*positionMult), startCol-1);
        Position S2E2 = new Position(startRow+(2*positionMult), startCol+2);
        Position S2W2 = new Position(startRow+(2*positionMult), startCol-2);

        //check for forward moves only if they are on the board
        if (isOnBoard(N1E1)) checkAndAddNormalMove(start, N1E1, board, possibleMoves);
        if (isOnBoard(N1W1)) checkAndAddNormalMove(start, N1W1, board, possibleMoves);
        //check backward for king pieces
        if (isOnBoard(S1E1) && pieceType == Piece.Type.KING) checkAndAddNormalMove(start, S1E1, board, possibleMoves);
        if (isOnBoard(S1W1) && pieceType == Piece.Type.KING) checkAndAddNormalMove(start, S1W1, board, possibleMoves);

        //check each possible initial jump
        //if the jump is possible, create a move sequence (list) and add it to the list
        if (isOnBoard(N1E1) && isOnBoard(N2E2)){
            Move jump1 = checkValidJump(start, N1E1, N2E2, board, activeColor);
            if (jump1 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump1);
                possibleJumps.add(tempList);
            }
        }

        if (isOnBoard(N1W1) && isOnBoard(N2W2)){
            Move jump2 = checkValidJump(start, N1W1, N2W2, board, activeColor);
            if (jump2 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump2);
                possibleJumps.add(tempList);
            }
        }
        //only if king type
        if (isOnBoard(S1E1) && isOnBoard(S2E2) && pieceType == Piece.Type.KING){
            Move jump3 = checkValidJump(start, S1E1, S2E2, board, activeColor);
            if (jump3 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump3);
                possibleJumps.add(tempList);
            }
        }

        if (isOnBoard(S1W1) && isOnBoard(S2W2) && pieceType == Piece.Type.KING){
            Move jump4 = checkValidJump(start, S1W1, S2W2, board, activeColor);
            if (jump4 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump4);
                possibleJumps.add(tempList);
            }
        }
    }

    /**
     * Iterates over the list of move sequences and tries to add jumps onto the last move of the sequence.
     * @param board the boardview to check
     * @param activeColor the color of the player in the playing my turn state
     * @param possibleJumps the list of move sequences
     * @return true if any moves were added to a sequence, false if no moves were added
     */
    private boolean addMoreJumps(BoardView board, Piece.Color activeColor,
                                ArrayList<ArrayList<Move>> possibleJumps){
        boolean added_flag = false;

        //get all the valid single moves and add them to the board
        for (int i=0; i < possibleJumps.size(); i++){
            ArrayList<Move> sequence = possibleJumps.get(i);
            int sequenceSize = possibleJumps.get(i).size();
            Position startPosition = sequence.get(0).getStart();
            Piece.Type startingPieceType = board.getSpace(startPosition.getRow(), startPosition.getCell()).getPiece().getType();
            Move lastMoveInSequence = sequence.get(sequenceSize-1);
            System.out.println("Testing index: " + i + " at last move of: " + lastMoveInSequence);

            boolean newJumps = addAJump(lastMoveInSequence, board, activeColor, startingPieceType, possibleJumps, i);
            if (newJumps){
                added_flag = true; //if you add anything, do another loop through to doubple check
            }
        }

        return added_flag; //if anything was added, this will be true. False otherwise.

    }

    /**
     * Adds any possible jumps onto the move provided (the last move in the move sequence)
     * @param move the last move in the move sequence
     * @param board the board to check for jumps on
     * @param activeColor the player with the active turn
     * @param pieceType the piece type that will be making the move
     * @param possibleJumps the list of move sequences (any new sequences or updated sequences go here)
     * @param moveListIndex the index of the sequence to update or add onto
     * @return true if any moves were added to the sequence
     */
    private boolean addAJump(Move move, BoardView board, Piece.Color activeColor, Piece.Type pieceType, ArrayList<ArrayList<Move>> possibleJumps, int moveListIndex){
        //start at the end of the last move
        int startRow = move.getEnd().getRow();
        int startCol = move.getEnd().getCell();

        int positionMult = 1;
        if (activeColor == Piece.Color.WHITE){
            positionMult = -1;
        }

        //get all of the Positions needed
        Position N1E1 = new Position(startRow-(1*positionMult), startCol+1);
        Position N1W1 = new Position(startRow-(1*positionMult), startCol-1);
        Position N2E2 = new Position(startRow-(2*positionMult), startCol+2);
        Position N2W2 = new Position(startRow-(2*positionMult), startCol-2);
        Position S1E1 = new Position(startRow+(1*positionMult), startCol+1);
        Position S1W1 = new Position(startRow+(1*positionMult), startCol-1);
        Position S2E2 = new Position(startRow+(2*positionMult), startCol+2);
        Position S2W2 = new Position(startRow+(2*positionMult), startCol-2);

        Move jump1 = null;
        Move jump2 = null;
        Move jump3 = null;
        Move jump4 = null;

        //check if the jumps are valid
        if (isOnBoard(N1E1) && isOnBoard(N2E2)) jump1 = checkValidJump(move.getEnd(), N1E1, N2E2, board, activeColor);
        if (isOnBoard(N1W1) && isOnBoard(N2W2)) jump2 = checkValidJump(move.getEnd(), N1W1, N2W2, board, activeColor);
        if (isOnBoard(S1E1) && isOnBoard(S2E2)
                && pieceType == Piece.Type.KING) jump3 = checkValidJump(move.getEnd(), S1E1, S2E2, board, activeColor);
        if (isOnBoard(S1W1) && isOnBoard(S2W2)
                && pieceType == Piece.Type.KING) jump4 = checkValidJump(move.getEnd(), S1W1, S2W2, board, activeColor);

        ArrayList<Move> notNullMoves = new ArrayList<>();

        //add each valid jump that was not the inverse of a move already made to the notNullMoves List
        if  (jump1 != null && !jump1.getEnd().equals(move.getStart())) notNullMoves.add(jump1);
        if  (jump2 != null && !jump2.getEnd().equals(move.getStart())) notNullMoves.add(jump2);
        if  (jump3 != null && !jump3.getEnd().equals(move.getStart())) notNullMoves.add(jump3);
        if  (jump4 != null && !jump4.getEnd().equals(move.getStart())) notNullMoves.add(jump4);

        //update the list of move sequences at the indexed lists
        if (notNullMoves.size() > 0){
            updateList(possibleJumps, moveListIndex, notNullMoves);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Add a move onto the original sequence in the list.
     * Create copies of the original sequence and add any other moves onto their respective copies.
     * Add the copies into as new move sequences in the list of move sequences.
     * @param possibleJumps the list of valid move sequences
     * @param moveListIndex the index of the list to the move sequence that is being added onto
     * @param notNullMoves the list of moves to be added
     */
    private void updateList(ArrayList<ArrayList<Move>> possibleJumps, int moveListIndex, ArrayList<Move> notNullMoves){
        ArrayList<Move> originalList = possibleJumps.get(moveListIndex);
        ArrayList<Move> temp_copy = new ArrayList<Move>(originalList);

        if (notNullMoves.size() == 3){
            //get the list they came from
            temp_copy.add(notNullMoves.get(0));
            possibleJumps.add(temp_copy);
            temp_copy = new ArrayList<Move>(originalList);
            temp_copy.add(notNullMoves.get(1));
            possibleJumps.get(moveListIndex).add(notNullMoves.get(2)); //add the last jump to the original list
        }else if (notNullMoves.size() == 2){
            //get the list they came from
            temp_copy.add(notNullMoves.get(0));
            possibleJumps.add(temp_copy);
            possibleJumps.get(moveListIndex).add(notNullMoves.get(1)); //add the last jump to the original list
        }else if(notNullMoves.size() == 1){
            possibleJumps.get(moveListIndex).add(notNullMoves.get(0)); //add the last jump to the original list
        }
    }

    /**
     * Attempts to get a piece from the board.
     * If the location and a piece exists there, then the piece will be returned.
     * Otherwise, null is returned.
     * @param board the boardview to check for a piece
     * @param position the posiiton to check for a piece at
     * @return Piece if on board and exists, null otherwise.
     */
    private Piece attemptPiece(BoardView board, Position position){
        if (isOnBoard(position)){
            return board.getSpace(position.getRow(), position.getCell()).getPiece();
        }
        return null;
    }

    /**
     * Determines if a piece is on the board.
     * @param position the position to check if it is within range.
     * @return true if on board, false if not on the board.
     */
    private boolean isOnBoard(Position position){
        int row = position.getRow();
        int cell = position.getCell();

        if (0 <= row && row <= 7){
            if (0 <= cell && cell <= 7){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a non jump move given the start and end
     * @param start the starting position for the move
     * @param end the end position for the move
     * @param board the board to check for the move on
     * @param possibleForwardMoves the list to add valid forward moves to
     * @return true if the move was added
     */
    public boolean checkAndAddNormalMove(Position start, Position end, BoardView board, ArrayList<Move> possibleForwardMoves){
        //get all of the potential pieces
        Piece endPiece = attemptPiece(board, end);
        if (endPiece == null){
//            System.out.println("valid forward move");
            Move tempMove = new Move(start, end); // build the move
            possibleForwardMoves.add(tempMove); // append it to the moves list
            return true;
        }
        return false;
    }

    /**
     * Checks for a jump move given the start, middle, and end positions.
     * @param start the starting position
     * @param middle the middle position (jumped position)
     * @param end the ending position (landing position)
     * @param board the board to check for jumps on
     * @param activeColor the player who has the active turn
     * @return The move if a valid jump, null otherwise
     */
    public Move checkValidJump(Position start, Position middle, Position end, BoardView board, Piece.Color activeColor){

        //dont worry about the piece at the start position
        //get all of the potential pieces whether they are null or not
        Piece middlePiece = attemptPiece(board, middle);
        Piece endPiece = attemptPiece(board, end);
        //check that the piece in the middle is not the active color and there is no piece at the end
        if (middlePiece != null && middlePiece.getColor() != activeColor & endPiece == null){
            return new Move(start, end);
        }
        return null;
    }
}


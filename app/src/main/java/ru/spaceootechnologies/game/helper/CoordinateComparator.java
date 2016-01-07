package ru.spaceootechnologies.game.helper;

import java.util.Comparator;

import ru.spaceootechnologies.game.entity.Coordinate;

/**
 * Created by Anton on 22.12.2015.
 */
public class CoordinateComparator implements Comparator<Coordinate> {

    private Coordinate playerPosition;

    public CoordinateComparator (Coordinate playerPosition){
        this.playerPosition= playerPosition;
    }


    private double  GetDistance (Coordinate from, Coordinate target){

        return  Math.hypot((from.getRow()-target.getRow()), (from.getColumn()-target.getColumn()));
    }


    /**
     * Compares the two specified objects to determine their relative ordering. The ordering
     * implied by the return value of this method for all possible pairs of
     * {@code (lhs, rhs)} should form an <i>equivalence relation</i>.
     * This means that
     * <ul>
     * <li>{@code compare(a,a)} returns zero for all {@code a}</li>
     * <li>the sign of {@code compare(a,b)} must be the opposite of the sign of {@code
     * compare(b,a)} for all pairs of (a,b)</li>
     * <li>From {@code compare(a,b) > 0} and {@code compare(b,c) > 0} it must
     * follow {@code compare(a,c) > 0} for all possible combinations of {@code
     * (a,b,c)}</li>
     * </ul>
     *
     * @param lhs an {@code Object}.
     * @param rhs a second {@code Object} to compare with {@code lhs}.
     * @return an integer < 0 if {@code lhs} is less than {@code rhs}, 0 if they are
     * equal, and > 0 if {@code lhs} is greater than {@code rhs}.
     * @throws ClassCastException if objects are not of the correct type.
     */
    @Override
    public int compare(Coordinate lhs, Coordinate rhs) {

        double left = GetDistance(playerPosition, lhs);
        double right = GetDistance(playerPosition, rhs);

        if (left < right)
            return -1;

        if (left == right)
            return 0;

        if (left > right)
            return 1;


        return 5;
    }




}

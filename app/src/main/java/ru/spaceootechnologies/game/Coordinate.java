package ru.spaceootechnologies.game;

/**
 * Created by Anton on 19.12.2015.
 */
public class Coordinate extends Object implements Cloneable {

    private int row;
    private int column;

    public Coordinate(int x, int y) {
        row = x;
        column = y;
    }

    public Coordinate() {
        row = 0;
        column = 0;

    }


    public int getPositionInList(int[][] array) {
        return row * array[0].length + column;
    }

    public static Coordinate CoordinateForPositionInList(int[][] array, int positionInList) {
        int column = positionInList % array[0].length;
        int row = (positionInList - column) / array[0].length;

        Coordinate mCoordinate = new Coordinate(row, column);

        return mCoordinate;
    }

    public int getRow() {
        return row;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Coordinate Plus(Coordinate right) {

        Coordinate cord = new Coordinate(this.row + right.row, this.column + right.column);

        return cord;

    }

    @Override
    public boolean equals(Object rhs) {
        return (rhs instanceof Coordinate) && (this.row == ((Coordinate) rhs).row)
                && (this.column == (((Coordinate) rhs).column));
    }

    public Coordinate MakeInversion() {
        Coordinate cord = new Coordinate(this.getRow() * -1, this.getColumn() * -1);

        return cord;
    }


}

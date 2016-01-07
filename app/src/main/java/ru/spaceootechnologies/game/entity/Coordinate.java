package ru.spaceootechnologies.game.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anton on 19.12.2015.
 */
public class Coordinate  implements Cloneable, Parcelable {

    private int row;
    private int column;


    // Реализация интерфейса Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(row);
        dest.writeInt(column);
    }

    private Coordinate(Parcel in) {

        this.row = in.readInt();
        this.column = in.readInt();

    }

    public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
        @Override
        public Coordinate createFromParcel(Parcel source) {
            return new Coordinate(source);
        }

        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };

    // Конец реализации


    public Coordinate(int x, int y) {
        row = x;
        column = y;
    }


    public int getPositionInList(int[][] array) {
        return row * array[0].length + column;
    }

    public static Coordinate CoordinateForPositionInList(int[][] array, int positionInList) {
        int column = positionInList % array[0].length;
        int row = (positionInList - column) / array[0].length;

        return new Coordinate(row, column);
    }

    public int getRow() {
        return row;
    }

    @Override
    public Object clone() {
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

        return new Coordinate(this.row + right.row, this.column + right.column);

    }

    @Override
    public boolean equals(Object rhs) {
        return (rhs instanceof Coordinate) && (this.row == ((Coordinate) rhs).row)
                && (this.column == (((Coordinate) rhs).column));
    }

}

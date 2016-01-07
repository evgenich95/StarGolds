package ru.spaceootechnologies.game.helper;

import java.util.ArrayList;
import java.util.List;

import ru.spaceootechnologies.game.entity.Coordinate;

/**
 * Created by Anton on 18.12.2015.
 */
 public  class Helper {

    public static List<Integer> DetectUpdates(int[][]oldArray, int[][]newArray){

        List listUpdatesAdapter = new ArrayList();
        for (int i=0; i < oldArray.length; i++)
            for (int k=0; k < oldArray[0].length; k++)
                if (oldArray[i][k] != newArray[i][k])
                {
                    Coordinate pos = new Coordinate(i,k);

                    listUpdatesAdapter.add(pos.getPositionInList(oldArray));

                }
        return listUpdatesAdapter;
    }

    public static int[][] CopyArray ( int[][] fromArray){
        int[][] inArray=new int[fromArray.length][fromArray[0].length];
        for (int i=0; i < fromArray.length; i++)
            System.arraycopy(fromArray[i], 0, inArray[i], 0, fromArray[i].length);

        return inArray;
    }


//    public static int  getXYFromPosition (int[][]array, int position){
//        columnPosition = position % array[0].length;
//        rowPosition = (position - columnPosition) / array[0].length;
//    }
}

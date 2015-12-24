package ru.spaceootechnologies.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ru.spaceootechnologies.game.Helpers.CoordinateComparator;
import ru.spaceootechnologies.game.Helpers.Helper;

/**
 * Created by Anton on 17.12.2015.
 */
public class Map {

    public static final String ConsoleLog = "Смотреть сюда";
    /*
     * 0-робот 1-игрок 2-золото 3-яма 4-ничего
     */


    public static final int RobotId = MainFragment.RobotId;
    public static final int RobotISFreezed = MainFragment.RobotISFreezed;
    public static final int PlayerId = MainFragment.PlayerId;
    public static final int GoldID = MainFragment.GoldID;
    public static final int PitID = MainFragment.PitID;
    public static final int EmptyId = MainFragment.EmptyId;


    int[][] temp;

    private int[][] arrayMap;

    private int sizeMap;
    private int amountGold;
    private int goldFound;
    private int amountFreezenStep;

    private Coordinate playerPosition;
    private int amountPlayerSteps;

    HashMap<ArrayList<Coordinate>, Integer> frezenRobots;

    private boolean GameOver;
    private boolean GameWin;


    public static final List<Coordinate> neighborOffsets = Arrays.asList(new Coordinate(-1, 0),
            new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(0, -1));


    public Map(int[][] arrayMap, Coordinate playerPosition, int goldAmount) {

        this.sizeMap = arrayMap.length;
        this.arrayMap = arrayMap;
        this.playerPosition = playerPosition;
        this.amountGold = goldAmount;
        this.goldFound = 0;

        this.GameOver = false;

        this.amountFreezenStep = 5;
        this.frezenRobots = new HashMap<ArrayList<Coordinate>, Integer>();


    }

    public int getAmountPlayerSteps() {
        return amountPlayerSteps;
    }

    public boolean isGameOver() {
        return GameOver;
    }


    public Coordinate getPlayerPosition() {
        return playerPosition;
    }


    public int[][] getArrayMap() {
        return arrayMap;
    }


    public boolean MovePlayer(Coordinate position) {

        if (position.equals(playerPosition)) {
            amountPlayerSteps++;
            return true; // игрок не двигается, но роботы делают ход
        }

        if (position.getRow() < 0 || position.getRow() >= sizeMap)
            return false;

        if (position.getColumn() < 0 || position.getColumn() >= sizeMap)
            return false;

        int place = arrayMap[position.getRow()][position.getColumn()];


        if (place != PitID && place != RobotISFreezed) {
            if (place == GoldID)
                goldFound++;

            arrayMap[playerPosition.getRow()][playerPosition.getColumn()] = EmptyId;
            if (place == RobotId) {
                GameOver = true;
                return false; // игра закончена, игрок ушёл со своей клитки, но роботы не двигаются
            }
            arrayMap[position.getRow()][position.getColumn()] = PlayerId;
            playerPosition = (Coordinate) position.clone();

            amountPlayerSteps++;


            return true;
        }


        return false;

    }


    private void RobotDidBitePlayer() {
        GameOver = true;
    }

    private boolean RobotCanMoveTo(Coordinate target) {
        if (target.getRow() < 0 || target.getRow() >= sizeMap)
            return false;

        if (target.getColumn() < 0 || target.getColumn() >= sizeMap)
            return false;

        int id = arrayMap[target.getRow()][target.getColumn()];
        if (id == RobotId || id == PitID || id == GoldID || id == RobotISFreezed) {
            return false;
        }
        return true;
    }

    private void MoveRobot(Coordinate coordinate) {
        Coordinate target = null;
        // Ищем первую попавшуюся свободную клетку

        Random generator = new Random();
        Coordinate nextPosition;

        ArrayList<Coordinate> offsets = new ArrayList<>(neighborOffsets);

        ArrayList<Coordinate> way = MapGenerator.FindShortWay(playerPosition, coordinate, arrayMap); // ищем
        // путь
        // от
        // робота
        // до
        // игрока

        if (way.size() > 0) {
            target = way.get(0);
        }

        if (target == null || RobotCanMoveTo(target) == false) { // если кратчайшего пути нет или по
            // нему нельзя двигаться
            target = null;
            int numberOffset;

            while (offsets.size() > 0) {
                numberOffset = generator.nextInt(offsets.size());
                nextPosition = coordinate.Plus(offsets.get(numberOffset));
                if (RobotCanMoveTo(nextPosition)) {
                    target = (Coordinate) nextPosition.clone();
                    break;
                } else
                    offsets.remove(numberOffset);
            }
        }

        if (target == null)
            return;
        // Перемещаем робота на новое место
        arrayMap[coordinate.getRow()][coordinate.getColumn()] = EmptyId;
        arrayMap[target.getRow()][target.getColumn()] = RobotId;

        // Робот наступил на игрока - игра окончена должна быть
        if (target.equals(playerPosition)) {
            RobotDidBitePlayer();
        }
    }

    public void MoveAllRobots() {
        int[][] tempArray = Helper.CopyArray(arrayMap);
        List<Coordinate> listRobots = new ArrayList<Coordinate>();


        for (int i = 0; i < sizeMap; i++)
            for (int k = 0; k < sizeMap; k++)
                if (arrayMap[i][k] == RobotId)
                    listRobots.add(new Coordinate(i, k));

        Collections.sort(listRobots, new CoordinateComparator(playerPosition)); // сортируем, чтобы
        // сначала двигался
        // ближайший к
        // игроку
        // робот

        for (Coordinate cor : listRobots) {
            MoveRobot(cor);
        }

        if (frezenRobots.size() <= 0)
            return; // если нет замороженных роботов, то просто ходят все роботы

        HashMap<ArrayList<Coordinate>, Integer> tempMap =
                (HashMap<ArrayList<Coordinate>, Integer>) frezenRobots.clone();

        for (java.util.Map.Entry<ArrayList<Coordinate>, Integer> entry : tempMap.entrySet()) {

            int step = entry.getValue();
            if (step == 0) {
                UnFreezeAllRobots(entry.getKey());
                frezenRobots.remove(entry.getKey());
            } else { // обновляем кол-во ходов до размораживания роботов
                step--;
                frezenRobots.put(entry.getKey(), step);
            }


        }


    }

    public int getAmoutGold() {
        return amountGold;
    }


    public int getGoldFound() {
        return goldFound;
    }


    public void FreezeAllRobots() {

        Coordinate nextPosition;
        ArrayList<Coordinate> listRobotsForFreeze = new ArrayList<>();

        Coordinate currentPosition = (Coordinate) playerPosition.clone();


        List<Coordinate> offsets = Arrays.asList(new Coordinate(-1, 0), new Coordinate(1, 0),
                new Coordinate(0, 1), new Coordinate(0, -1), new Coordinate(1, 1),
                new Coordinate(-1, 1), new Coordinate(1, -1), new Coordinate(-1, -1));

        for (Coordinate cord : offsets) {
            nextPosition = currentPosition.Plus(cord);

            if (nextPosition.getRow() < 0 || nextPosition.getRow() >= sizeMap) {
                continue;
            }

            if (nextPosition.getColumn() < 0 || nextPosition.getColumn() >= sizeMap)
                continue;

            if (arrayMap[nextPosition.getRow()][nextPosition.getColumn()] == RobotId)
                listRobotsForFreeze.add(nextPosition);
        }


        if (listRobotsForFreeze.size() <= 0)
            return;


        frezenRobots.put(listRobotsForFreeze, amountFreezenStep);

        for (Coordinate cord : listRobotsForFreeze) {
            arrayMap[cord.getRow()][cord.getColumn()] = RobotISFreezed;
        }


    }

    public void UnFreezeAllRobots(ArrayList<Coordinate> listFreezenRobots) {

        if (listFreezenRobots.size() <= 0)
            return;

        for (Coordinate cord : listFreezenRobots) {
            arrayMap[cord.getRow()][cord.getColumn()] = RobotId;
        }
    }
}


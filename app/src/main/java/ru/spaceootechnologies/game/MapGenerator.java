package ru.spaceootechnologies.game;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Anton on 23.12.2015.
 */
public class MapGenerator {

    public static final int RobotId = MainFragment.RobotId;
    public static final int PlayerId = MainFragment.PlayerId;
    public static final int GoldID = MainFragment.GoldID;
    public static final int PitID = MainFragment.PitID;
    public static final int EmptyId = MainFragment.EmptyId;

    private Map mMap;

    private int mapSize;
    private int robotAmount;
    private int goldAmount;
    private int pitAmount;

    public MapGenerator(int mapSize, int robotAmount, int goldAmount, int pitAmount) {

        this.mMap = null;
        this.mapSize = mapSize;
        this.robotAmount = robotAmount;
        this.goldAmount = goldAmount;
        this.pitAmount = pitAmount;
    }

    public Map getMap() {
        if (mMap == null)
            GenerateNewMap();
        return mMap;
    }

    public void GenerateNewMap() {

        ArrayList<Coordinate> allCoordinates = new ArrayList<>();
        int[][] arrayMap;
        // Создаём массив и формируем список всех координат
        arrayMap = new int[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++)
            for (int k = 0; k < mapSize; k++)
                allCoordinates.add(new Coordinate(i, k));

        ArrayList<Coordinate> placesOfRobots = new ArrayList<>();
        ArrayList<Coordinate> placesOfGold = new ArrayList<>();
        ArrayList<Coordinate> placesForPit = new ArrayList<>();



        if (allCoordinates.size() <= 1) {
            return;

        }

        // Помещаем игрока

        Coordinate playerPosition = new Coordinate(mapSize / 2, mapSize / 2);
        arrayMap[playerPosition.getRow()][playerPosition.getColumn()] = PlayerId;

        allCoordinates.remove(playerPosition);


        if (allCoordinates.size() < goldAmount) {
            return;
        }

        ArrayList<Coordinate> GoldtCanPlace = (ArrayList<Coordinate>) allCoordinates.clone();

        // ЗОЛОТО!!
        for (int i = 0; i < goldAmount; i++) {
            Coordinate place = GoldtCanPlace.get(new Random().nextInt(GoldtCanPlace.size()));

            arrayMap[place.getRow()][place.getColumn()] = GoldID;
            placesOfGold.add(place);    // список где стоят золото
            GoldtCanPlace.remove(place);
        }

        for (Coordinate cord : placesOfGold) {
            allCoordinates.removeAll(FindShortWay(cord, playerPosition, arrayMap));
        }

        if (allCoordinates.size() < robotAmount) {
            return;
        }

        // Случайно расставляем роботов, в любое место на заданном радиусе от игрока

        // РОБОТЫ!!
        ArrayList<Coordinate> RobotCanPlace = (ArrayList<Coordinate>) allCoordinates.clone();

        // Получаем список координат в радиусе 2 клетки от игрока
        ArrayList<Coordinate> areaPlayer = GetAreaPlayer(playerPosition);

        RobotCanPlace.removeAll(areaPlayer);

        for (int i = 0; i < robotAmount; i++) {
            Coordinate place = RobotCanPlace.get(new Random().nextInt(RobotCanPlace.size()));
            arrayMap[place.getRow()][place.getColumn()] = RobotId;

            placesOfRobots.add(place); // список где стоят роботы

            RobotCanPlace.remove(place);

        }

        for (Coordinate cord : placesOfRobots) {
            allCoordinates.removeAll(FindShortWay(cord, playerPosition, arrayMap));
        }

        if (allCoordinates.size() < pitAmount) {
            return;
        }

        // ЯМЫ!!
        ArrayList<Coordinate> pitCanPlace = (ArrayList<Coordinate>) allCoordinates.clone();

        for (int i = 0; i < pitAmount; i++) {
            Coordinate place = pitCanPlace.get(new Random().nextInt(pitCanPlace.size()));
            arrayMap[place.getRow()][place.getColumn()] = PitID;

            placesForPit.add(place); // список где стоят ямы

            pitCanPlace.remove(place);

        }
        // Получили все необходимые данные для создания карты

        mMap = new Map(arrayMap, playerPosition, goldAmount);
    }

    public static ArrayList<Coordinate> FindShortWay(Coordinate fromPoint, Coordinate targetPoint,
            int[][] arrayMap) {
        
        //формируем массив разметки
        int[][] temp = new int[arrayMap.length][arrayMap[0].length];

        for (int i = 0; i < arrayMap.length; i++) {
            for (int j = 0; j < arrayMap[0].length; j++) {
                temp[i][j] = -1;
            }
        }

        ArrayList<Coordinate> inStackCoordinate = new ArrayList<>();
        ArrayList<Coordinate> ShortWay = new ArrayList<>();
        ArrayList<Coordinate> offsets = new ArrayList<Coordinate>(Map.neighborOffsets);

        int marker = 0;

        temp[fromPoint.getRow()][fromPoint.getColumn()] = marker;
        Coordinate currentPosition = (Coordinate) fromPoint.clone();

        inStackCoordinate.add(fromPoint);

        Coordinate nextPosition;

        int IdTarget = arrayMap[targetPoint.getRow()][targetPoint.getColumn()];
        int IdFrom = arrayMap[fromPoint.getRow()][fromPoint.getColumn()];

        //начинаем разметку карты в ширину
        while (!currentPosition.equals(targetPoint) && inStackCoordinate.size() > 0) {


            if (temp[inStackCoordinate.get(0).getRow()][inStackCoordinate.get(0)
                    .getColumn()] != marker) {
                marker++;
                continue;
            }

            for (Coordinate pos : offsets) {

                nextPosition = currentPosition.Plus(pos);

                if (nextPosition.getRow() < 0 || nextPosition.getRow() >= arrayMap.length) {
                    continue;
                }

                if (nextPosition.getColumn() < 0 || nextPosition.getColumn() >= arrayMap[0].length)
                    continue;

                int point = arrayMap[nextPosition.getRow()][nextPosition.getColumn()];

                if (point != EmptyId && point != IdTarget && point != IdFrom) {
                    continue;
                }

                if (temp[nextPosition.getRow()][nextPosition.getColumn()] == -1) {

                    temp[nextPosition.getRow()][nextPosition.getColumn()] = marker + 1;

                    inStackCoordinate.add(nextPosition);
                }
            }
            inStackCoordinate.remove(0);

            if (inStackCoordinate.size() == 0)
                return ShortWay;// значит путь не найден

            currentPosition = (Coordinate) inStackCoordinate.get(0).clone(); // текущая позиция
                                                                             // верхняя вершина
                                                                             // стека

        }

        //Формируем путь по разметке
        //Путь обязательно будет существовать
        currentPosition = (Coordinate) targetPoint.clone();
        temp[fromPoint.getRow()][fromPoint.getColumn()] = 0;

        while (!currentPosition.equals(fromPoint)) {
            for (Coordinate pos : offsets) {

                nextPosition = currentPosition.Plus(pos);


                if (nextPosition.getRow() < 0 || nextPosition.getRow() >= arrayMap.length) {
                    continue;
                }

                if (nextPosition.getColumn() < 0 || nextPosition.getColumn() >= arrayMap[0].length)
                    continue;

                int valueNextPosition = arrayMap[nextPosition.getRow()][nextPosition.getColumn()];

                if (valueNextPosition != EmptyId && valueNextPosition != IdFrom
                        && valueNextPosition != IdTarget) {
                    continue;
                }

                int currentValue = temp[currentPosition.getRow()][currentPosition.getColumn()];
                int nextValue = temp[nextPosition.getRow()][nextPosition.getColumn()];
                if ((currentValue - nextValue) == 1) {

                    ShortWay.add(nextPosition);
                    currentPosition = (Coordinate) nextPosition.clone();
                    break;
                }
            }

        }
        return ShortWay;

    }

    public ArrayList<Coordinate> GetAreaPlayer(Coordinate positionPlayer) {

        ArrayList<Coordinate> areaPlayer = new ArrayList<>();

        List<Coordinate> offsets =
                Arrays.asList(new Coordinate(-1, 0), new Coordinate(-2, 0), new Coordinate(1, 0),
                        new Coordinate(2, 0), new Coordinate(0, 1), new Coordinate(0, 2),
                        new Coordinate(0, -1), new Coordinate(0, -2), new Coordinate(1, 1),
                        new Coordinate(-1, 1), new Coordinate(1, -1), new Coordinate(-1, -1));

        for (Coordinate cor : offsets) {
            areaPlayer.add(positionPlayer.Plus(cor));
        }
        return areaPlayer;
    }
}

package ru.spaceootechnologies.game.helper;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import ru.spaceootechnologies.game.R;
import ru.spaceootechnologies.game.entity.Coordinate;
import ru.spaceootechnologies.game.entity.Map;

/**
 * Created by Anton on 23.12.2015.
 */
public class MapGenerator {

    private Map mMap;

    private int mapSize;
    private int robotAmount;
    private int goldAmount;
    private int pitAmount;
    private String ErrorMessage;
    private Context context;

    public MapGenerator(int mapSize, int robotAmount, int goldAmount, int pitAmount, Context context) {

        this.mMap = null;
        this.mapSize = mapSize;
        this.robotAmount = robotAmount;
        this.goldAmount = goldAmount;
        this.pitAmount = pitAmount;
        this.context = context;

        this.ErrorMessage = null;
    }

    public Map getMap() {
        if (mMap == null)
            generateNewMap();
        return mMap;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void generateNewMap() {

        ArrayList<Coordinate> allCoordinates = new ArrayList<>();
        int[][] arrayMap;
        // Создаём массив и формируем список всех координат
        arrayMap = new int[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++)
            for (int k = 0; k < mapSize; k++)
                allCoordinates.add(new Coordinate(i, k));

        ArrayList<Coordinate> placesOfRobots = new ArrayList<>();
        ArrayList<Coordinate> placesOfGold = new ArrayList<>();

        if (allCoordinates.size() <= 1) {
            ErrorMessage = context.getString(R.string.error_size_map);
            return;

        }

        // Помещаем игрока

        Coordinate playerPosition = new Coordinate(mapSize / 2, mapSize / 2);
        arrayMap[playerPosition.getRow()][playerPosition.getColumn()] = Constants.PlayerId;

        allCoordinates.remove(playerPosition);


        if (allCoordinates.size() < goldAmount) {
            ErrorMessage = context.getString(R.string.error_count_gold);
            return;
        }

        ArrayList<Coordinate> GoldtCanPlace = (ArrayList<Coordinate>) allCoordinates.clone();

        // ЗОЛОТО!!
        for (int i = 0; i < goldAmount; i++) {
            Coordinate place = GoldtCanPlace.get(new Random().nextInt(GoldtCanPlace.size()));

            arrayMap[place.getRow()][place.getColumn()] = Constants.GoldID;
            placesOfGold.add(place); // список где стоят золото
            GoldtCanPlace.remove(place);
        }

        for (Coordinate cord : placesOfGold) {
            allCoordinates.removeAll(findShortWay(cord, playerPosition, arrayMap));
        }

        if (allCoordinates.size() < robotAmount) {
            ErrorMessage = context.getString(R.string.error_robots_cant_place);
            return;
        }

        // Случайно расставляем роботов, в любое место на заданном радиусе от игрока

        // РОБОТЫ!!
        ArrayList<Coordinate> RobotCanPlace = (ArrayList<Coordinate>) allCoordinates.clone();

        // Получаем список координат в радиусе 2 клетки от игрока
        ArrayList<Coordinate> areaPlayer = getAreaPlayer(playerPosition);

        RobotCanPlace.removeAll(areaPlayer);

        for (int i = 0; i < robotAmount; i++) {
            if (RobotCanPlace.size() < 1){
                ErrorMessage = context.getString(R.string.error_robots_cant_place);
                return; // построить карту нельзя
            }

            Coordinate place = RobotCanPlace.get(new Random().nextInt(RobotCanPlace.size()));
            arrayMap[place.getRow()][place.getColumn()] = Constants.RobotId;

            placesOfRobots.add(place); // список где стоят роботы

            RobotCanPlace.remove(place);

        }

        for (Coordinate cord : placesOfRobots) {
            allCoordinates.removeAll(findShortWay(cord, playerPosition, arrayMap));
        }

        if (allCoordinates.size() < pitAmount) {
            ErrorMessage = context.getString(R.string.error_pits_cant_placed);
            return;
        }

        // ЯМЫ!!
        ArrayList<Coordinate> pitCanPlace = (ArrayList<Coordinate>) allCoordinates.clone();

        for (int i = 0; i < pitAmount; i++) {
            Coordinate place = pitCanPlace.get(new Random().nextInt(pitCanPlace.size()));
            arrayMap[place.getRow()][place.getColumn()] = Constants.PitID;

            pitCanPlace.remove(place);

        }
        // Получили все необходимые данные для создания карты

        mMap = new Map(arrayMap, playerPosition, goldAmount);
    }

    public static ArrayList<Coordinate> findShortWay(Coordinate fromPoint, Coordinate targetPoint,
                                                     int[][] arrayMap) {

        // формируем массив разметки
        int[][] temp = new int[arrayMap.length][arrayMap[0].length];

        for (int i = 0; i < arrayMap.length; i++) {
            for (int j = 0; j < arrayMap[0].length; j++) {
                temp[i][j] = -1;
            }
        }

        ArrayList<Coordinate> inStackCoordinate = new ArrayList<>();
        ArrayList<Coordinate> ShortWay = new ArrayList<>();
        ArrayList<Coordinate> offsets = new ArrayList<>(Map.neighborOffsets);

        int marker = 0;

        temp[fromPoint.getRow()][fromPoint.getColumn()] = marker;
        Coordinate currentPosition = (Coordinate) fromPoint.clone();

        inStackCoordinate.add(fromPoint);

        Coordinate nextPosition;

        int IdTarget = arrayMap[targetPoint.getRow()][targetPoint.getColumn()];
        int IdFrom = arrayMap[fromPoint.getRow()][fromPoint.getColumn()];

        // начинаем разметку карты в ширину
        while (!currentPosition.equals(targetPoint) && inStackCoordinate.size() > 0) {


            if (temp[inStackCoordinate.get(0).getRow()][inStackCoordinate.get(0)
                    .getColumn()] != marker) {
                marker++;
                continue;
            }

            for (Coordinate pos : offsets) {

                nextPosition = currentPosition.plus(pos);

                if (nextPosition.getRow() < 0 || nextPosition.getRow() >= arrayMap.length) {
                    continue;
                }

                if (nextPosition.getColumn() < 0 || nextPosition.getColumn() >= arrayMap[0].length)
                    continue;

                int point = arrayMap[nextPosition.getRow()][nextPosition.getColumn()];

                if (point != Constants.EmptyId && point != IdTarget && point != IdFrom) {
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

        // Формируем путь по разметке
        // Путь обязательно будет существовать
        currentPosition = (Coordinate) targetPoint.clone();
        temp[fromPoint.getRow()][fromPoint.getColumn()] = 0;

        while (!currentPosition.equals(fromPoint)) {
            for (Coordinate pos : offsets) {

                nextPosition = currentPosition.plus(pos);


                if (nextPosition.getRow() < 0 || nextPosition.getRow() >= arrayMap.length) {
                    continue;
                }

                if (nextPosition.getColumn() < 0 || nextPosition.getColumn() >= arrayMap[0].length)
                    continue;

                int valueNextPosition = arrayMap[nextPosition.getRow()][nextPosition.getColumn()];

                if (valueNextPosition != Constants.EmptyId && valueNextPosition != IdFrom
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

    public ArrayList<Coordinate> getAreaPlayer(Coordinate positionPlayer) {

        ArrayList<Coordinate> areaPlayer = new ArrayList<>();

        List<Coordinate> offsets =
                Arrays.asList(new Coordinate(-1, 0), new Coordinate(-2, 0), new Coordinate(1, 0),
                        new Coordinate(2, 0), new Coordinate(0, 1), new Coordinate(0, 2),
                        new Coordinate(0, -1), new Coordinate(0, -2), new Coordinate(1, 1),
                        new Coordinate(-1, 1), new Coordinate(1, -1), new Coordinate(-1, -1));

        for (Coordinate cor : offsets) {
            areaPlayer.add(positionPlayer.plus(cor));
        }
        return areaPlayer;
    }
}

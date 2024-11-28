package com.example.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

class Card {
//    Paint p = new Paint();
    Bitmap frontImage;
    Bitmap backImage;

    boolean isOpen = false;

    float x, y, width, height;

    public Card(float x, float y, float width, float height, Bitmap frontImage, Bitmap backImage) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.frontImage = frontImage;
        this.backImage = backImage;
    }

//    int color, backColor = Color.DKGRAY;
//    boolean isOpen = false; // цвет карты
//    float x, y, width, height;

    public void draw(Canvas c) {
        // нарисовать карту в виде цветного прямоугольника

        Bitmap toDraw = isOpen ? frontImage: backImage;
        c.drawBitmap(toDraw, x, y, null);

//        if (isOpen) {
//            p.setColor(color);
//        } else p.setColor(backColor);
//        c.drawRect(x,y, x+width, y+height, p);
    }
    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        } else return false;
    }

}

public class TilesView extends View {
    // пауза для запоминания карт
    final int PAUSE_LENGTH = 1; // в секундах
    boolean isOnPauseNow = false;

    // число открытых карт
    int openedCard = 0;
    ArrayList<Card> cards = new ArrayList<>();

    int width, height; // ширина и высота канвы

    Card firstCard = null;
    Card secondCard = null;

    Bitmap backImage;
    ArrayList<Bitmap> frontImages = new ArrayList<>();

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadImages(context);
        newGame();
//        // 1) заполнить массив tiles случайными цветами
//        // сгенерировать поле 2*n карт, при этом
//        // должно быть ровно n пар карт разных цветов
//        cards.add(new Card(0,0, 200, 150, Color.YELLOW));
//        cards.add(new Card(200+50, 0, 200 + 200 + 50, 150, Color.YELLOW));
//
//        cards.add(new Card(0,200, 200, 150 + 200, Color.RED));
//        cards.add(new Card(200+50, 200, 200 + 200 + 50, 150+200, Color.RED));
    }

    private void loadImages(Context context)
    {
        backImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.im9);


        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im1));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im2));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im3));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im4));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im5));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im6));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im7));
        frontImages.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.im8));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        // 2) отрисовка плиток
        // задать цвет можно, используя кисть
        //Paint p = new Paint();
        for (Card c: cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {
            // 3) получить координаты касания
//            int x = (int) event.getX();
//            int y = (int) event.getY();
            float x = event.getX();
            float y = event.getY();
            // 4) определить тип события
//            if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)


            // палец коснулся экрана

            for (Card c: cards) {

                if (!c.isOpen && c.flip(x, y)) {

                    if (openedCard == 0) {
                        firstCard = c;
                        openedCard ++;
//                        if (c.flip(x, y)) {
//                            Log.d("mytag", "card flipped: " + openedCard);
//                            openedCard++;
//                            invalidate();
//                            return true;
//
                    } else if (openedCard == 1) {

                        secondCard = c;
                        openedCard++;
                        isOnPauseNow = true;

                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);

//                        // перевернуть карту с задержкой
//                        if (c.flip(x, y)) {
//                            openedCard++;
//                            // 1) если открылис карты одинакового цвета, удалить их из списка
//                            // например написать функцию, checkOpenCardsEqual
//
//                            // 2) проверить, остались ли ещё карты
//                            // иначе сообщить об окончании игры
//
//                            // если карты открыты разного цвета - запустить задержку
//                            invalidate();
//                            PauseTask task = new PauseTask();
//                            task.execute(PAUSE_LENGTH);
//                            isOnPauseNow = true;
//                            return true;
                    }
                    // заставляет экран перерисоваться
//        return true;
                    invalidate();
                    return true;
                }
            }

        }
        return true;
    }


    public void newGame() {
        // запуск новой игры

        cards.clear();
        openedCard = 0;
        isOnPauseNow = false;

        ArrayList<Bitmap> images = new ArrayList<>();

        for (Bitmap img: frontImages)
        {
            images.add(img);
            images.add(img);
        }

        Collections.shuffle(images);

//        int numPairs = 8;
//        ArrayList<Integer> colors = new ArrayList<>();
//        Random random = new Random();
//
//        for (int i = 0; i < numPairs; i++)
//        {
//            colors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
//            colors.add(colors.get(i));
//        }
//        Collections.shuffle(colors);

        int numCols = 4;
        int numRows = 4;
        int tileWidts = width / numCols;
        int tileHeight = height / numRows;

        for (int i = 0; i < images.size(); i++)
        {
            int col = i % numCols;
            int row = i / numCols;
            cards.add(new Card(col * tileWidts, row * tileHeight, tileWidts - 10, tileHeight - 10, images.get(i), backImage));

        }
        invalidate();
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
//            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаём число секунд ожидания
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
//            Log.d("mytag", "Pause finished");
            return null;
        }

        // после паузы, перевернуть все карты обратно


        @Override
        protected void onPostExecute(Void aVoid) {

            if (firstCard != null && secondCard != null)
            {
                if (firstCard.frontImage == secondCard.frontImage)
                {
                    cards.remove(firstCard);
                    cards.remove(secondCard);
                } else {
                    firstCard.isOpen = false;
                    secondCard.isOpen = false;
                }
            }

            firstCard = null;
            secondCard = null;
            openedCard = 0;
            isOnPauseNow = false;
            invalidate();

            if (cards.isEmpty())
            {
                Toast.makeText(getContext(), "You win", Toast.LENGTH_SHORT).show();
            }



//            for (Card c: cards) {
//                if (c.isOpen) {
//                    c.isOpen = false;
//                }
//            }
//            openedCard = 0;
//            isOnPauseNow = false;
//            invalidate();
        }
    }
}
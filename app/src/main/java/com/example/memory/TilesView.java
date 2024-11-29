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

    public void draw(Canvas c) {

        Bitmap toDraw = isOpen ? frontImage: backImage;
        c.drawBitmap(toDraw, x, y, null);

    }
    public boolean flip (float touch_x, float touch_y) {
        if (touch_x >= x && touch_x <= x + width && touch_y >= y && touch_y <= y + height) {
            isOpen = ! isOpen;
            return true;
        } else return false;
    }

}

public class TilesView extends View {
    final int PAUSE_LENGTH = 1; 
    boolean isOnPauseNow = false;

    int openedCard = 0;
    ArrayList<Card> cards = new ArrayList<>();

    int width, height; 

    Card firstCard = null;
    Card secondCard = null;

    Bitmap backImage;
    ArrayList<Bitmap> frontImages = new ArrayList<>();

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadImages(context);
        newGame();

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

        for (Card c: cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow)
        {

            float x = event.getX();
            float y = event.getY();

            for (Card c: cards) {

                if (!c.isOpen && c.flip(x, y)) {

                    if (openedCard == 0) {
                        firstCard = c;
                        openedCard ++;

                    } else if (openedCard == 1) {

                        secondCard = c;
                        openedCard++;
                        isOnPauseNow = true;

                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);


                    invalidate();
                    return true;
                }
            }

        }
        return true;
    }


    public void newGame() {

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
            try {
                Thread.sleep(integers[0] * 1000); 
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }



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

        }
    }
}

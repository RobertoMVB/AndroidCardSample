package cards.example.com.cardsampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import cards.example.com.cardsampleapp.cardLayout.CardLayout;
import cards.example.com.cardsampleapp.cardLayout.contract.ICardLayout;
import cards.example.com.cardsampleapp.cardLayout.model.CardModel;

public class MainActivity extends Activity implements OnClickListener {

    CardLayout myCardLayout;
    int cardCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCardLayout = (CardLayout) findViewById(R.id.cardLayout);

        ArrayList<CardModel> myCollection = new ArrayList<CardModel>();

        myCardLayout.setContractor(new ICardLayout() {
            @Override
            public void swipeLeft(CardModel card) {
                Toast.makeText(getApplicationContext(), "RODANDO PARA ESQUERDA - CARD " + card.getCardType(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void swipeRight(CardModel card) {
                Toast.makeText(getApplicationContext(), "RODANDO PARA A DIREITA - CARD " + card.getCardType(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(CardModel card) {
                Toast.makeText(getApplicationContext(), "CLICOU  - CARD " + card.getCardType(), Toast.LENGTH_SHORT).show();

            }
        });

        Button addCardButton = (Button) findViewById(R.id.addCard);
        Button addCarListdButton = (Button) findViewById(R.id.removeCard);

        addCardButton.setOnClickListener(this);
        addCarListdButton.setOnClickListener(this);

        //myCardLayout.SetListeners(myClickListener,myTouchEvent);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.addCard:

                CardModel cardSample = new CardModel();
                cardSample.setCardType(cardCount);
                cardSample.setCardURL("http://");

                // sample to add random image
                int randomNumber = (int) (Math.random() * (100 - 0)) + 0;

                cardSample.setCardText("Este é o meu cartão : " + cardCount + " com o id aleatório : " + randomNumber);

                if (randomNumber > 66) {
                    cardSample.setImageResourceId(R.drawable.icon_action_alert1);
                } else if (randomNumber > 33) {
                    cardSample.setImageResourceId(R.drawable.icon_action_alert2);
                } else {
                    cardSample.setImageResourceId(R.drawable.icon_action_alert3);
                }

                myCardLayout.addCard(cardSample);
                cardCount++;
                break;
            case R.id.removeCard:

                ArrayList<CardModel> cards =  new ArrayList<CardModel>();

                for (int i = 0 ; i < 3 ; i ++) {

                    CardModel card = new CardModel();
                    card.setCardType(cardCount);
                    card.setCardURL("http://");

                    // sample to add random image
                    int randomN = (int) (Math.random() * (100 - 0)) + 0;

                    card.setCardText("Este é o meu cartão : " + cardCount + " com o id aleatório : " + randomN);

                    if (randomN > 66) {
                        card.setImageResourceId(R.drawable.icon_action_alert1);
                    } else if (randomN > 33) {
                        card.setImageResourceId(R.drawable.icon_action_alert2);
                    } else {
                        card.setImageResourceId(R.drawable.icon_action_alert3);
                    }
                    cards.add(card);
                    cardCount++;
                }

                myCardLayout.addCards(cards);
                break;
        }
    }

}

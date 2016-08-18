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

        myCardLayout.setNumberOfDisplayedCards(3);

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
        Button add3CardsListdButton = (Button) findViewById(R.id.add3Cards);
        Button add6CardsListdButton = (Button) findViewById(R.id.add6Cards);

        addCardButton.setOnClickListener(this);
        add3CardsListdButton.setOnClickListener(this);
        add6CardsListdButton.setOnClickListener(this);

        //myCardLayout.SetListeners(myClickListener,myTouchEvent);

    }

    @Override
    public void onClick(View view) {

        ArrayList<CardModel> cards =  new ArrayList<CardModel>();
        CardModel card = new CardModel();

        switch (view.getId()) {
            case R.id.addCard:

                card.setCardType(cardCount);
                card.setCardURL("http://");
                card.setCardNextText("Próximo");
                card.setCardDeleteText("Fechar");
                card.setImageNextId(R.drawable.icon_action_next);
                card.setImageDeleteId(R.drawable.icon_action_delete);

                // sample to add random image
                int randomNumber = (int) (Math.random() * (100 - 0)) + 0;

                card.setCardText("Este é o meu cartão : " + cardCount + " com o id aleatório : " + randomNumber);

                if (randomNumber > 66) {
                    card.setImageResourceId(R.drawable.icon_action_alert1);
                } else if (randomNumber > 33) {
                    card.setImageResourceId(R.drawable.icon_action_alert2);
                } else {
                    card.setImageResourceId(R.drawable.icon_action_alert3);
                }

                myCardLayout.addCard(card);
                cardCount++;
                break;
            case R.id.add3Cards:

                for (int i = 0 ; i < 3 ; i ++) {

                    card = new CardModel();

                    card.setCardType(cardCount);
                    card.setCardURL("http://");

                    // sample to add random image
                    int randomN = (int) (Math.random() * (100 - 0)) + 0;
                    card.setCardNextText("Próximo");
                    card.setCardDeleteText("Fechar");
                    card.setImageNextId(R.drawable.icon_action_next);
                    card.setImageDeleteId(R.drawable.icon_action_delete);
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
            case R.id.add6Cards:
                for (int i = 0 ; i < 6 ; i ++) {

                    card = new CardModel();
                    card.setCardNextText("Próximo");
                    card.setCardDeleteText("Fechar");
                    card.setImageNextId(R.drawable.icon_action_next);
                    card.setImageDeleteId(R.drawable.icon_action_delete);
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

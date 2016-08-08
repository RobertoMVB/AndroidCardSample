package cards.example.com.cardsampleapp.cardLayout.contract;

import android.view.View.OnClickListener;

import cards.example.com.cardsampleapp.cardLayout.model.CardModel;

/**
 * Created by rvilas on 8/5/16.
 */

public interface ICardLayout {
    public void swipeLeft(CardModel card);
    public void swipeRight(CardModel model);
    public void onClick(CardModel model);
}

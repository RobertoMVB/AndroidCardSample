package cards.example.com.cardsampleapp.cardLayout.model;

import android.view.View.OnClickListener;

/**
 * Created by rvilas on 8/3/16.
 */

public class CardModel {

    private int cardType;
    private String cardText;
    private String cardURL;
    private int imageResourceId;

    public CardModel() {
        super();
    }

    public CardModel(int cardType, String cardText, int cardImageResourceId, String cardURL) {
        this.cardType = cardType;
        this.cardText = cardText;
        this.cardURL = cardURL;
        this.imageResourceId = cardImageResourceId;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cartText) {
        this.cardText = cartText;
    }

    public String getCardURL() {
        return cardURL;
    }

    public void setCardURL(String cardURL) {
        this.cardURL = cardURL;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

}

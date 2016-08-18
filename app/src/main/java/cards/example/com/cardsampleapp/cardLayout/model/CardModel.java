package cards.example.com.cardsampleapp.cardLayout.model;

import android.view.View.OnClickListener;

/**
 * Created by rvilas on 8/3/16.
 */

public class CardModel {

    private int cardType;
    private String cardText,cardURL,cardNextText = "next", cardDeleteText = "delete";
    private int imageResourceId, imageNextId, imageDeleteId;

    public CardModel() {
        super();
    }

    public CardModel(int cardType, String cardText, int cardImageResourceId, String cardURL, String nextText, String deleteText, int nextImage, int deleteImage) {
        this.cardType = cardType;
        this.cardText = cardText;
        this.cardURL = cardURL;
        this.imageResourceId = cardImageResourceId;
        this.cardNextText = nextText;
        this.cardDeleteText = deleteText;
        this.imageNextId = nextImage;
        this.imageDeleteId = deleteImage;

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
    public String getCardNextText() {
        return cardNextText;
    }

    public void setCardNextText(String cardNextText) {
        this.cardNextText = cardNextText;
    }

    public String getCardDeleteText() {
        return cardDeleteText;
    }

    public void setCardDeleteText(String cardDeleteText) {
        this.cardDeleteText = cardDeleteText;
    }

    public int getImageNextId() {
        return imageNextId;
    }

    public void setImageNextId(int imageNextId) {
        this.imageNextId = imageNextId;
    }

    public int getImageDeleteId() {
        return imageDeleteId;
    }

    public void setImageDeleteId(int imageDeleteId) {
        this.imageDeleteId = imageDeleteId;
    }
}

package cards.example.com.cardsampleapp.cardLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cards.example.com.cardsampleapp.R;
import cards.example.com.cardsampleapp.cardLayout.contract.ICardLayout;
import cards.example.com.cardsampleapp.cardLayout.model.CardModel;

/**
 * Created by rvilas on 8/2/16.
 */

public class CardLayout extends LinearLayout {

    private final int size100Dp = dpToPx(100);
    private final int size80Dp = dpToPx(80);
    private final int size50Dp = dpToPx(50);
    private final int size40dp = dpToPx(45);
    private final int size35Dp = dpToPx(35);
    private final int size25Dp = dpToPx(25);
    private final int size10Dp = dpToPx(10);
    private final int size5Dp = dpToPx(5);

    private int moveX,moveImage1X,moveImage2X,moveBadgeX;
    private int MIN_DISTANCE = 0, cardWidth, moveDelta=0, currentCard=0, totalCards=0;
    private CardModel currentCardModel;
    private ArrayList<CardModel> cardCollection = new ArrayList<CardModel>();
    private LinearLayout linearLayout;
    private ImageView nextCardimageView, deleteCardimageView, contentCardImageView;
    private TextView textView, quantityTextView;
    private LinearLayout.LayoutParams cardViewLayoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,size100Dp);
    private ICardLayout iCardLayout = null;
    private AnimatorListenerAdapter cardViewSwipeRightAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            linearLayout.setVisibility(View.GONE);
            nextCardData();
            if (iCardLayout!=null) {
                iCardLayout.swipeRight(currentCardModel);
            }
        }
    };

    AnimatorListenerAdapter cardViewSwipeLeftAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeCard(currentCardModel);
            if (iCardLayout!=null) {
                iCardLayout.swipeLeft(currentCardModel);
            }
        }
    };

    public void setContractor(ICardLayout iCardLayout) {
        this.iCardLayout = iCardLayout;
    }

    public CardLayout(Context context) {
        super(context);
        initCardLayout(context);
    }

    public CardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCardLayout(context);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCardLayout(context);
    }

    @TargetApi(21)
    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCardLayout(context);
    }

    /**
     *
     * @param cards
     * @return
     */
    public boolean addCards(ArrayList<CardModel> cards) {
        boolean addSuccess = this.cardCollection.addAll(cards);

        if(addSuccess) {

            if (quantityTextView == null) {
                initCardLayout(getContext());
            }

            totalCards = this.cardCollection.size();
            quantityTextView.setText((String.valueOf(totalCards)));
        }

        return addSuccess;
    }


    /**
     *
     * @param card
     * @return
     */
    public boolean addCard(CardModel card) {

        if (getVisibility() == View.INVISIBLE) {
            setVisibility(View.VISIBLE);
        }

        boolean addSuccess = this.cardCollection.add(card);

        if(addSuccess) {

            if (quantityTextView == null) {
                initCardLayout(getContext());
            }

            totalCards = this.cardCollection.size();
            if (totalCards > 0) {
                currentCardModel = this.cardCollection.get(0);
                quantityTextView.setText((String.valueOf(totalCards)));
            }
            nextCardData();
        }

        return addSuccess;

    }

    /**
     *
     * @param cards
     */
    public void replaceCards (ArrayList<CardModel> cards) {
        this.cardCollection = cards;
    }

    /**
     *
     * @param card
     * @return
     */
    public boolean removeCard (CardModel card) {
        boolean success = this.cardCollection.remove(card);

        if (success) {
            if (quantityTextView == null) {
                initCardLayout(getContext());
            }
            totalCards = this.cardCollection.size();

            if (totalCards > 0) {
                quantityTextView.setText((String.valueOf(totalCards)));
            } else {
                setVisibility(View.GONE);
            }
            nextCardData();
        }

        return success;
    }

    /**
     *
      */
    private void nextCardData() {
        removeAllViews();
        setVisibility(View.INVISIBLE);
        animate().scaleY(0);
        animate().scaleX(0);

        initCardLayout(getContext());

        currentCard++;
        if (currentCard >= totalCards) {
            currentCard = 0;
        }

        if (totalCards > 0) {
            currentCardModel = this.cardCollection.get(currentCard);

            textView.setText(currentCardModel.getCardText());
            contentCardImageView.setImageResource(currentCardModel.getImageResourceId());
            quantityTextView.setText((String.valueOf(totalCards)));
            setVisibility(View.VISIBLE);
            animate().scaleY(1);
            animate().scaleX(1);
        }


    }

    /**
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     *
     * @param context
     */
    private void initCardLayout(Context context) {

        cardWidth = getWidth();

        cardViewLayoutParam.setMargins(size10Dp,size10Dp,size10Dp,size10Dp);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(cardViewLayoutParam);

        ImageView cardViewNextImageView = new ImageView(context);
        cardViewNextImageView.setImageResource(R.drawable.icon_action_next);
        LayoutParams cardViewNextLayoutParam = new LayoutParams(size50Dp,size50Dp);
        cardViewNextLayoutParam.setMargins(-size50Dp,size10Dp,size10Dp,0);
        cardViewNextImageView.setLayoutParams(cardViewNextLayoutParam);

        ImageView cardViewContentImageView = new ImageView(context);
        //cardViewContentImageView.setImageResource(R.drawable.icon_action_alert1);
        LayoutParams cardViewContentImageLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT,size50Dp);
        cardViewContentImageLayoutParam.setMargins(size10Dp,size10Dp,size10Dp,size10Dp);
        cardViewContentImageView.setLayoutParams(cardViewContentImageLayoutParam);

        TextView cardViewContentTextView = new TextView(context);
        LayoutParams cardViewContentTextLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        cardViewContentTextLayoutParam.setMargins(0,0,size25Dp,0);
        cardViewContentTextView.setPadding(size10Dp,size10Dp,size10Dp,size10Dp);
        cardViewContentTextView.setMaxLines(3);
        cardViewContentTextView.setLayoutParams(cardViewContentTextLayoutParam);
        cardViewContentTextView.setGravity(Gravity.CENTER_VERTICAL);
        cardViewContentTextView.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sollicitudin ut quam et aliquam. Quisque justo mauris, convallis et massa in, aliquam commodo tortor. Duis posuere luctus turpis ut maximus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Aliquam faucibus justo a neque congue imperdiet. Praesent ac lacinia lorem, in elementum enim. Phasellus fermentum ligula imperdiet nisl pharetra, quis aliquam velit maximus. In ullamcorper aliquet nunc a egestas. Aliquam semper gravida diam, blandit efficitur sem convallis nec. Cras feugiat ultrices erat at dignissim.");

        TextView cardViewBadgeTextView = new TextView(context);
        LayoutParams cardViewBadgeLayoutParam = new LayoutParams(size25Dp,size25Dp);
        cardViewBadgeTextView.setBackgroundResource(R.drawable.background_card_badge);
        cardViewBadgeTextView.setTextSize(16);
        cardViewBadgeTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        cardViewBadgeLayoutParam.setMargins(-size25Dp,-size35Dp,0,0);
        cardViewBadgeTextView.setLayoutParams(cardViewBadgeLayoutParam);
        cardViewBadgeTextView.setTextColor(Color.WHITE);

        LinearLayout cardViewLinearLayout = new LinearLayout(context);
        cardViewLinearLayout.setBackgroundResource(R.drawable.background_card);
        cardViewLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams cardViewLinearLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT,size80Dp);
        cardViewLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        cardViewLinearLayout.setLayoutParams(cardViewLinearLayoutParam);
        cardViewLinearLayout.addView(cardViewContentImageView);
        cardViewLinearLayout.addView(cardViewContentTextView);
        //cardViewLinearLayout.addView(cardViewBadgeTextView);

        ImageView cardViewDeleteImageView = new ImageView(context);
        LayoutParams cardViewDeleteLayoutParams = new LayoutParams(size50Dp,size50Dp);
        cardViewDeleteLayoutParams.setMargins(size25Dp,size10Dp,0,size10Dp);
        cardViewDeleteImageView.setLayoutParams(cardViewDeleteLayoutParams);
        cardViewDeleteImageView.setImageResource(R.drawable.icon_action_delete);

        addView(cardViewNextImageView);
        addView(cardViewLinearLayout);
        addView(cardViewBadgeTextView);
        addView(cardViewDeleteImageView);


        nextCardimageView = cardViewNextImageView;
        linearLayout = cardViewLinearLayout;
        contentCardImageView = cardViewContentImageView;
        textView = cardViewContentTextView;
        quantityTextView = cardViewBadgeTextView;
        deleteCardimageView = cardViewDeleteImageView;

        if(totalCards <= 0) {
            setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     * @param context
     */
    private void initCardLayout(Context context, ArrayList<CardModel> cards) {
        initCardLayout(context);
        cardCollection.addAll(cards);

        //check if the cards lenght is bigger than 0
        if (!cards.isEmpty()) {
            textView.setText(cards.get(0).getCardText());
        }

        quantityTextView.setText(cards.size());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.v("CardLayout","onTouchEvent");
        Log.v("CardLayout","onTouchEvent - MIN_DISTANCE : " + MIN_DISTANCE);
        final int posX = (int) event.getRawX();
        MIN_DISTANCE = cardWidth/2;

        LinearLayout.LayoutParams ContentLayoutParams, nextImageLayoutParams, deleteImageParams,badgeLayoutParams,
        calculatedContentLayoutParams,calculatedNextImageLayoutParams,calculatedDeleteImageParams, calculatedBadgeLayoutParam;


        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                ContentLayoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                nextImageLayoutParams = (LinearLayout.LayoutParams) nextCardimageView.getLayoutParams();
                deleteImageParams = (LinearLayout.LayoutParams) deleteCardimageView.getLayoutParams();
                badgeLayoutParams = (LinearLayout.LayoutParams) quantityTextView.getLayoutParams();

                moveX = posX - ContentLayoutParams.leftMargin;
                moveImage1X = posX - nextImageLayoutParams.leftMargin;
                moveImage2X = posX - deleteImageParams.leftMargin;
                moveBadgeX = posX - badgeLayoutParams.leftMargin;

                Log.v("CardLayout","onTouchEvent - ACTION_DOWN -  moveX : " + moveX);
                Log.v("CardLayout","onTouchEvent - ACTION_DOWN -  moveImage1X : " + moveImage1X);
                Log.v("CardLayout","onTouchEvent - ACTION_DOWN -  moveImage2X : " + moveImage2X);
                break;

            case MotionEvent.ACTION_MOVE:
                moveDelta = posX - moveX;
                int deltaImage1 = posX - moveImage1X;
                int deltaImage2 = posX - moveImage2X;
                int deltaBadge = posX - moveBadgeX;

                Log.v("CardLayout","onTouchEvent - ACTION_MOVE -  delta : " + moveDelta);

                ContentLayoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                nextImageLayoutParams = (LinearLayout.LayoutParams) nextCardimageView.getLayoutParams();
                deleteImageParams = (LinearLayout.LayoutParams) deleteCardimageView.getLayoutParams();
                badgeLayoutParams = (LinearLayout.LayoutParams) quantityTextView.getLayoutParams();

                ContentLayoutParams.width = LayoutParams.MATCH_PARENT;
                ContentLayoutParams.leftMargin = moveDelta;
                ContentLayoutParams.rightMargin = -moveDelta;
                linearLayout.setLayoutParams(ContentLayoutParams);

                nextImageLayoutParams.leftMargin = deltaImage1;
                nextImageLayoutParams.rightMargin = -deltaImage1;
                nextCardimageView.setLayoutParams(nextImageLayoutParams);

                deleteImageParams.leftMargin = deltaImage2;
                deleteImageParams.rightMargin = -deltaImage2;
                deleteCardimageView.setLayoutParams(deleteImageParams);

                badgeLayoutParams.leftMargin = deltaBadge;
                badgeLayoutParams.rightMargin = -deltaBadge;
                quantityTextView.setLayoutParams(badgeLayoutParams);

                break;

            case MotionEvent.ACTION_UP:
                Log.v("CardLayout","onTouchEvent - ACTION_UP -  delta : " + moveDelta);
                if (moveDelta > 0 && moveDelta > MIN_DISTANCE) {
                    // LEFT to RIGHT
                    linearLayout.animate().translationX(cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeRightAnimatorListenerAdapter);
                    nextCardimageView.animate().translationX(cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeRightAnimatorListenerAdapter);
                    deleteCardimageView.animate().translationX(cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeRightAnimatorListenerAdapter);
                    quantityTextView.animate().translationX(cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeRightAnimatorListenerAdapter);

                    setLayoutParams(cardViewLayoutParam);

                    invalidate();
                    linearLayout.invalidate();
                    nextCardimageView.invalidate();
                    deleteCardimageView.invalidate();
                    quantityTextView.invalidate();

                } else if (-moveDelta > MIN_DISTANCE) {
                    // RIGHT to LEFT
                    linearLayout.animate().translationX(-cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeLeftAnimatorListenerAdapter);
                    nextCardimageView.animate().translationX(-cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeLeftAnimatorListenerAdapter);
                    deleteCardimageView.animate().translationX(-cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeLeftAnimatorListenerAdapter);
                    quantityTextView.animate().translationX(-cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeLeftAnimatorListenerAdapter);

                    setLayoutParams(cardViewLayoutParam);

                    invalidate();
                    linearLayout.invalidate();
                    nextCardimageView.invalidate();
                    deleteCardimageView.invalidate();
                    quantityTextView.invalidate();

                } else if (moveDelta > 10 || moveDelta < -10) {
                    moveDelta = -moveDelta;
                    linearLayout.animate().translationX(moveDelta);
                    nextCardimageView.animate().translationX(moveDelta);
                    deleteCardimageView.animate().translationX(moveDelta);
                    quantityTextView.animate().translationX(moveDelta);
                } else {

                    if (iCardLayout != null) {
                        iCardLayout.onClick(currentCardModel);
                    }

                }
                moveDelta = 0;
                break;
        }

        return true;
    }

}

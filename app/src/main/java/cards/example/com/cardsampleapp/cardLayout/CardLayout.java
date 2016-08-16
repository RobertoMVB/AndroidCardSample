package cards.example.com.cardsampleapp.cardLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private Context currentContext;

    private int moveX,moveBadgeX, moveNextImageView, moveDeleteImageView;
    private int MIN_DISTANCE = 0, cardWidth, moveDelta=0, currentCard=0, totalCards=0, currentCardId =0;
    private CardModel currentCardModel;
    private ArrayList<CardModel> cardCollection = new ArrayList<CardModel>();
    private LinearLayout currentCardLinearLayout;
    private LinearLayout currentView;
    private TextView quantityTextView;
    private LinearLayout.LayoutParams cardViewLayoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    private ICardLayout iCardLayout = null;

    private AnimatorListenerAdapter cardViewSwipeRightAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            // Only execute if the current view is set
            if ( currentView != null) {
                Log.v("CardLayout","cardViewSwipeRightAnimatorListenerAdapter");
                if (iCardLayout!=null) {
                    iCardLayout.swipeRight(currentCardModel);
                }
                nextCardData();
            }

        }
    };

    AnimatorListenerAdapter cardViewSwipeLeftAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            Log.v("CardLayout","cardViewSwipeLeftAnimatorListenerAdapter");
            if (iCardLayout!=null) {
                iCardLayout.swipeLeft(currentCardModel);
            }
            removeCard(currentCardModel);

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

            totalCards = this.cardCollection.size();
            initCardLayout(getContext());
            currentCardModel = this.cardCollection.get(totalCards-1);
        }

        return addSuccess;
    }


    /**
     *
     * @param card
     * @return
     */
    public boolean addCard(CardModel card) {

        boolean addSuccess = this.cardCollection.add(card);

        if(addSuccess) {

            totalCards = this.cardCollection.size();
            initCardLayout(getContext());
            currentCardModel = this.cardCollection.get(totalCards-1);

        }

        return addSuccess;

    }

    /**
     *
     * @param cards
     */
    public boolean replaceCards (ArrayList<CardModel> cards) {
        this.cardCollection.clear();
        return addCards(cards);
    }

    /**
     *
     * @param card
     * @return
     */
    public boolean removeCard (CardModel card) {
        boolean success = this.cardCollection.remove(card);

        if (success) {

            totalCards = this.cardCollection.size();
            currentCard = totalCards -1;

            resetBadgePosition();
            // Check if the cards were added
            if (currentCardLinearLayout == null) {
                initCardLayout(getContext());
            } else if (totalCards > 0) {
                currentCardLinearLayout.removeView(currentView);
                nextCardData();
            } else {
                setVisibility(View.GONE);
                currentCardLinearLayout = null;
            }

        }

        return success;
    }

    /**
     *
      */
    private void nextCardData() {

        Log.v("CardLayout","nextCardData");

        if (totalCards > 0) {

            // reset the current view ( will be replaced )
            currentView = null;

            View movedView = currentCardLinearLayout.findViewById(currentCardId);

            // Check if the old card was deleted
            if (movedView != null) {

                currentCardLinearLayout.removeViewInLayout(movedView);

                CardModel movedCardModel = cardCollection.get(currentCard);

                LinearLayout movedCardView = newCardView(movedCardModel,totalCards,false);

                LayoutParams cardViewContentLinearLayoutParam = (LayoutParams) movedView.getLayoutParams();
                cardViewContentLinearLayoutParam.setMargins(size10Dp,(totalCards-1)*size10Dp,size10Dp,0);
                currentCardLinearLayout.addView(movedCardView,0,cardViewContentLinearLayoutParam);

                currentCard--;

                if (currentCard < 0 ) {
                    currentCard = totalCards-1;
                }

            }

            for (int i = 0; i < totalCards; i++) {

                View nextCardView = currentCardLinearLayout.getChildAt(i);

                LayoutParams nextCardLayoutParams = (LayoutParams) nextCardView.getLayoutParams();
                // First Card
                if (i == 0) {
                    nextCardLayoutParams.setMargins(size10Dp,(totalCards-1)*(size10Dp+size5Dp),size10Dp,0);
                } else {
                    nextCardLayoutParams.setMargins(size10Dp,-size80Dp-size10Dp,size10Dp,0);
                }
                nextCardView.setLayoutParams(nextCardLayoutParams);

                nextCardView.animate().scaleX(nextCardView.getScaleX() + 0.02f).scaleY(nextCardView.getScaleY() + 0.02f);
                nextCardView.invalidate();
            }

            currentCardModel = this.cardCollection.get(currentCard);
            resetBadgePosition();
        }


    }

    private void resetBadgePosition () {

        LayoutParams cardViewBadgeLayoutParam = new LayoutParams(size25Dp,size25Dp);
        cardViewBadgeLayoutParam.setMargins(-size50Dp-size25Dp,-size35Dp,0,0);
        quantityTextView.setText(String.valueOf(totalCards));
        quantityTextView.setLayoutParams(cardViewBadgeLayoutParam);
        quantityTextView.invalidate();
        invalidate();

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
     * @param card
     * @param numTotal
     * @param firstCard
     * @return
     */
    private LinearLayout newCardView (CardModel card, int numTotal,boolean firstCard) {

        // linear Layout with all views ( next, content and delete )
        LinearLayout cardViewLinearLayout = new LinearLayout(currentContext);

        // Image view that appears in swipe right
        ImageView cardViewNextImageView = new ImageView(currentContext);
        cardViewNextImageView.setImageResource(R.drawable.icon_action_next);
        LayoutParams cardViewNextLayoutParam = new LayoutParams(size50Dp,size50Dp);
        cardViewNextLayoutParam.setMargins(-size50Dp,size10Dp,0,0);
        cardViewNextImageView.setLayoutParams(cardViewNextLayoutParam);

        // Image view that appears in the card
        ImageView cardViewContentImageView = new ImageView(currentContext);
        cardViewContentImageView.setImageResource(card.getImageResourceId());
        LayoutParams cardViewContentImageLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT,size50Dp);
        cardViewContentImageLayoutParam.setMargins(size10Dp,size10Dp,size10Dp,size10Dp);
        cardViewContentImageView.setLayoutParams(cardViewContentImageLayoutParam);

        // Text view that appears in the card
        TextView cardViewContentTextView = new TextView(currentContext);
        LayoutParams cardViewContentTextLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        cardViewContentTextView.setPadding(size10Dp,size10Dp,size10Dp,size10Dp);
        cardViewContentTextView.setMaxLines(3);
        cardViewContentTextView.setLayoutParams(cardViewContentTextLayoutParam);
        cardViewContentTextView.setGravity(Gravity.CENTER_VERTICAL);
        cardViewContentTextView.setText(card.getCardText());

        // Linear layout with the content
        LinearLayout cardViewContentLinearLayout = new LinearLayout(currentContext);
        cardViewContentLinearLayout.setBackgroundResource(R.drawable.background_card);
        cardViewContentLinearLayout.setOrientation(HORIZONTAL);
        cardViewContentLinearLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams cardViewContentLinearLayoutParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,size80Dp);
        cardViewContentLinearLayout.setLayoutParams(cardViewContentLinearLayoutParam);


        float scaleValue = 1f-((numTotal)/50f);

        cardViewContentLinearLayout.setScaleX(scaleValue);
        cardViewContentLinearLayout.setScaleX(scaleValue);

        ImageView cardViewDeleteImageView = new ImageView(currentContext);
        LayoutParams cardViewDeleteLayoutParams = new LayoutParams(size50Dp,size50Dp);
        cardViewDeleteLayoutParams.setMargins(size25Dp,size10Dp,0,size10Dp);
        cardViewDeleteImageView.setLayoutParams(cardViewDeleteLayoutParams);
        cardViewDeleteImageView.setImageResource(R.drawable.icon_action_delete);

        LayoutParams cardViewLinearLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        // First Card
        if (firstCard) {
            cardViewLinearLayoutParam.setMargins(size10Dp,numTotal*(size10Dp+size5Dp),size10Dp,0);
        } else {
            cardViewLinearLayoutParam.setMargins(size10Dp,-size80Dp-size10Dp,size10Dp,0);
        }

        cardViewLinearLayout.setLayoutParams(cardViewLinearLayoutParam);

        cardViewContentLinearLayout.addView(cardViewContentImageView);
        cardViewContentLinearLayout.addView(cardViewContentTextView);

        cardViewLinearLayout.addView(cardViewNextImageView);
        cardViewLinearLayout.addView(cardViewContentLinearLayout);
        cardViewLinearLayout.addView(cardViewDeleteImageView);

        cardViewLinearLayout.setId(currentCardId);
        currentCardId--;
        return  cardViewLinearLayout;
    }

    /**
     *
     * @param context
     */
    private void initCardLayout(Context context) {

        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        cardWidth = size.x;

        currentContext = context;
        removeAllViews();
        currentCardId = totalCards-1;

        cardViewLayoutParam.setMargins(size10Dp,size10Dp,size10Dp,size10Dp);
        setGravity(Gravity.CENTER_VERTICAL);
        setLayoutParams(cardViewLayoutParam);

        if(totalCards <= 0) {
            setVisibility(View.GONE);
        } else {

            // Linear Layout that contains the entire card
            LinearLayout cardLinearLayout = new LinearLayout(getContext());
            LayoutParams cardLinearLayoutLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            cardLinearLayout.setLayoutParams(cardLinearLayoutLayoutParam);
            cardLinearLayout.setOrientation(VERTICAL);

            for (int i = 0; i< totalCards ; i++) {

                CardModel card = this.cardCollection.get(i);

                cardLinearLayout.addView(newCardView(this.cardCollection.get(i),totalCards-1-i,i==0));

            }

            TextView cardViewBadgeTextView = new TextView(context);
            LayoutParams cardViewBadgeLayoutParam = new LayoutParams(size25Dp,size25Dp);
            cardViewBadgeTextView.setBackgroundResource(R.drawable.background_card_badge);
            cardViewBadgeTextView.setTextSize(16);
            cardViewBadgeTextView.setText(String.valueOf(totalCards));
            cardViewBadgeTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            cardViewBadgeLayoutParam.setMargins(-size35Dp,-size25Dp-(size5Dp*totalCards),0,0);
            cardViewBadgeTextView.setLayoutParams(cardViewBadgeLayoutParam);
            cardViewBadgeTextView.setTextColor(Color.WHITE);

            addView(cardLinearLayout);
            addView(cardViewBadgeTextView);


            currentCard = totalCards-1;
            quantityTextView = cardViewBadgeTextView;
            currentCardLinearLayout = cardLinearLayout;

        }

    }

    /**
     *
     * @param context
     */
    private void initCardLayout(Context context, ArrayList<CardModel> cards) {
        initCardLayout(context);
        cardCollection.addAll(cards);

        quantityTextView.setText(cards.size());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.v("CardLayout","onTouchEvent");
        Log.v("CardLayout","onTouchEvent - MIN_DISTANCE : " + MIN_DISTANCE);
        final int posX = (int) event.getRawX();
        MIN_DISTANCE = cardWidth/2;

        currentView = (LinearLayout) currentCardLinearLayout.getChildAt(currentCardLinearLayout.getChildCount()-1);
        currentCardId = currentView.getId();

        ImageView nextImageView = (ImageView) currentView.getChildAt(0);
        ImageView deleteImageView = (ImageView) currentView.getChildAt(2);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                moveX = posX - (int) currentView.getX();
                moveBadgeX = posX - (int) quantityTextView.getX();
                moveNextImageView = posX - (int) nextImageView.getX();
                moveDeleteImageView = posX - (int) deleteImageView.getX();

               break;

            case MotionEvent.ACTION_MOVE:
                moveDelta = posX - moveX;
                int deltaBadge = posX - moveBadgeX;
                int deltaNextImageView = posX - moveNextImageView;
                int deltaDeleteImageView = posX - moveDeleteImageView;

                nextImageView.setX(deltaNextImageView);
                deleteImageView.setX(deltaDeleteImageView);
                currentView.setX(moveDelta);
                quantityTextView.setX(deltaBadge);

                break;

            case MotionEvent.ACTION_UP:
                if (moveDelta > 0 && moveDelta > MIN_DISTANCE) {

                    // LEFT to RIGHT
                    currentView.animate().translationX(cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeRightAnimatorListenerAdapter);
                    nextImageView.animate().translationX(cardWidth);
                    deleteImageView.animate().translationX(cardWidth);
                    quantityTextView.animate().translationX(cardWidth);

                } else if (-moveDelta > MIN_DISTANCE) {

                    // RIGHT to LEFT
                    currentView.animate().translationX(-cardWidth).setInterpolator(new LinearInterpolator()).setListener(cardViewSwipeLeftAnimatorListenerAdapter);
                    nextImageView.animate().translationX(-cardWidth);
                    deleteImageView.animate().translationX(-cardWidth);
                    quantityTextView.animate().translationX(-cardWidth);

                } else if (moveDelta > 30 || moveDelta < -30) {

                    moveDelta = -moveDelta;
                    currentView.animate().translationX(moveDelta);
                    nextImageView.animate().translationX(moveDelta);
                    deleteImageView.animate().translationX(moveDelta);
                    quantityTextView.animate().translationX(moveDelta);

                } else if (iCardLayout != null) {
                    iCardLayout.onClick(currentCardModel);
                }
                moveDelta = 0;
                break;
        }


        return true;
    }

}

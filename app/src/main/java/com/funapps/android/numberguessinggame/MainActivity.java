package com.funapps.android.numberguessinggame;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private int mUserAnswer = 0;
    private int mRandNumber;
    private int mRemainingGuesses = 10;
    private ArrayList<Integer> mArrayOfAnswers = new ArrayList<>(10);
    private TextView mTestingRandomGenerator;
    private TextView mRemainingGuessesText;
    private TextView mGameOverText;
    private TextView mAttemptedAnswersText;
    private EditText mUserSubmit;
    private Button mSubmitButton;
    private Button mResetButton;

    AnimationDrawable mAnimationDrawable;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare animation and LinearLayout
        mLinearLayout = (LinearLayout)findViewById(R.id.myLinearLayout);
        mAnimationDrawable = (AnimationDrawable)mLinearLayout.getBackground();

        //Add time changes
        mAnimationDrawable.setEnterFadeDuration(5000);
        mAnimationDrawable.setExitFadeDuration(2000);

        //And start the animation now
        mAnimationDrawable.start();

        mUserSubmit = (EditText) findViewById(R.id.user_value);
        mSubmitButton = (Button)findViewById(R.id.submit_button);
        mResetButton = (Button)findViewById(R.id.reset_button);
        mGameOverText = (TextView)findViewById(R.id.game_over_text);
        mAttemptedAnswersText = (TextView)findViewById(R.id.attempted_anwsers_text);

        generateRandomNumber();
        //mTestingRandomGenerator = (TextView) findViewById(R.id.numberTest_text_view);
        //mTestingRandomGenerator.setText(String.valueOf(mRandNumber));


        setSubmitButton();
        checkRemainingGuesses();


        mUserSubmit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSubmitButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    private void checkAnswer(){
        int messageResId = 0;
        mUserAnswer = Integer.parseInt(mUserSubmit.getText().toString());
        if(mUserAnswer == mRandNumber){
            messageResId = R.string.correct_toast;
            Toasty.success(this, getString(R.string.correct_toast), Toast.LENGTH_LONG).show();
            gameOver();
            mResetButton.setVisibility(View.VISIBLE);
        }else if(mUserAnswer > mRandNumber && mUserAnswer <= 1000){
            messageResId = R.string.incorrect_too_high_toast;
            mUserSubmit.setText("");
            Toast.makeText(this,messageResId,Toast.LENGTH_LONG).show();
        } else if(mUserAnswer<mRandNumber){
            messageResId = R.string.incorrect_too_low_toast;
            mUserSubmit.setText("");
            Toast.makeText(this,messageResId,Toast.LENGTH_LONG).show();
        }else{
            numberOutOfRange();
            return;
        }
        mArrayOfAnswers.add(mUserAnswer);
        displayAttemptedAnswers();
        mRemainingGuesses--;
        checkRemainingGuesses();
        setSubmitButton();

    }
    private void displayAttemptedAnswers(){
            mAttemptedAnswersText.setText(getString(R.string.attempted_answers_text) + mArrayOfAnswers);
    }
    private void generateRandomNumber(){
        Random randomNumber = new Random();
        mRandNumber = (Integer)randomNumber.nextInt(1000)+1;
    }
    private void gameOver(){
        hideKeyboard(this);
        if(mUserAnswer == mRandNumber){
            mGameOverText.setVisibility(View.VISIBLE);
            mGameOverText.setText(getString(R.string.player_won_text));
        }else if(mRemainingGuesses == 0){
            mGameOverText.setText(getString(R.string.game_over_text));
            mGameOverText.setVisibility(View.VISIBLE);
            mResetButton.setVisibility(View.VISIBLE);

        }
        mUserSubmit.setText("");
    }
    private void checkRemainingGuesses(){
        mRemainingGuessesText = (TextView)findViewById(R.id.guesses_remaining_text_view);
        mRemainingGuessesText.setText(getString(R.string.guesses_remaining_text_view, mRemainingGuesses));
        if(mRemainingGuesses == 0){
            gameOver();
        }
    }
    private void setSubmitButton(){
        if(mUserSubmit.getText().toString().equals("") || mRemainingGuesses == 0 || mUserAnswer == mRandNumber){
            mSubmitButton.setEnabled(false);
        }
        else{
            mSubmitButton.setEnabled(true);
        }
    }
    private void resetGame(){
        mRemainingGuesses = 10;
        mUserSubmit.setText("");
        mArrayOfAnswers.clear();
        displayAttemptedAnswers();
        mGameOverText.setVisibility(View.INVISIBLE);
        mResetButton.setVisibility(View.INVISIBLE);
        checkRemainingGuesses();
        generateRandomNumber();
        //mTestingRandomGenerator.setText(String.valueOf(mRandNumber));
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void numberOutOfRange(){
        Toasty.error(this,getString(R.string.number_out_of_range_toasty),Toast.LENGTH_LONG).show();
        mUserSubmit.setText("");
    }
}

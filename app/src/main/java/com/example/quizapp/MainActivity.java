package com.example.quizapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvQuestion, tvScore,tvTimer;
    Button btnOption1, btnOption2,btnOption3,btnOption4,btnNext,btnPrevious,btnReveal;
    int currentQuestion=0;
    int totalQuestions=20;
    String selectedAnswer="";
    String correctAnswer="";
    int score=0;
    CountDownTimer countDownTimer;
    long timeLeft=40000;
    Boolean[] answeredQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        custom_init();


    }
    private void custom_init(){
        tvScore=findViewById(R.id.tvscore);
        tvTimer=findViewById(R.id.tvTimer);
        tvQuestion=findViewById(R.id.tvQuestion);
        btnOption1=findViewById(R.id.btnOption1);
        btnOption2=findViewById(R.id.btnOption2);
        btnOption3=findViewById(R.id.btnOption3);
        btnOption4=findViewById(R.id.btnOption4);
        btnNext=findViewById(R.id.btnNext);
        btnPrevious=findViewById(R.id.btnPrevious);
        btnReveal=findViewById(R.id.btnReveal);


        btnOption1.setOnClickListener(this);
        btnOption2.setOnClickListener(this);
        btnOption3.setOnClickListener(this);
        btnOption4.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnReveal.setOnClickListener(this);

        answeredQuestions=new Boolean[totalQuestions];
        for (int i = 0; i < answeredQuestions.length; i++) {
            answeredQuestions[i] = false; // Initialize all to false
        }
        startTimer();
        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {
        Button clickedButton= (Button) view;
        if(clickedButton.getId()==R.id.btnNext){
            if(!answeredQuestions[currentQuestion]) {
                if (selectedAnswer.equals(QuestionAnswers.answers[currentQuestion])) {
                    score += 5;
                } else {
                    score -= 1;

                }
                tvScore.setText(String.valueOf("Score: " + score));
                answeredQuestions[currentQuestion] = true;
            }
            currentQuestion++;
            if(currentQuestion<totalQuestions){
                loadNewQuestion();
            }
            else{
                showFinalScore();
            }

        } else if (clickedButton.getId() == R.id.btnPrevious) {
            if (currentQuestion > 0) {
                currentQuestion--;
                loadNewQuestion();
                tvScore.setText(String.valueOf("Score: "+ score));
                btnOption1.setEnabled(false);
                btnOption2.setEnabled(false);
                btnOption3.setEnabled(false);
                btnOption4.setEnabled(false);


            }
        } else if (clickedButton.getId()==R.id.btnReveal) {
            System.out.println("Revel");
            correctAnswer=QuestionAnswers.answers[currentQuestion];
            revealCorrectAnswer();
            btnOption1.setEnabled(false);
            btnOption2.setEnabled(false);
            btnOption3.setEnabled(false);
            btnOption4.setEnabled(false);

        } else{
            selectedAnswer= clickedButton.getText().toString();

        }
    }

    void loadNewQuestion(){
        btnOption1.setBackgroundColor(Color.GRAY);
        btnOption2.setBackgroundColor(Color.GRAY);
        btnOption3.setBackgroundColor(Color.GRAY);
        btnOption4.setBackgroundColor(Color.GRAY);

        btnOption1.setEnabled(true);
        btnOption2.setEnabled(true);
        btnOption3.setEnabled(true);
        btnOption4.setEnabled(true);


        tvQuestion.setText(QuestionAnswers.question[currentQuestion]);
        btnOption1.setText(QuestionAnswers.options[currentQuestion][0]);
        btnOption2.setText(QuestionAnswers.options[currentQuestion][1]);
        btnOption3.setText(QuestionAnswers.options[currentQuestion][2]);
        btnOption4.setText(QuestionAnswers.options[currentQuestion][3]);

        selectedAnswer = "";
        correctAnswer = QuestionAnswers.answers[currentQuestion];


    }
    void revealCorrectAnswer(){
        if(btnOption1.getText().toString().equals(correctAnswer)){
            btnOption1.setBackgroundColor(Color.MAGENTA);
        } else if (btnOption2.getText().toString().equals(correctAnswer)){
            btnOption2.setBackgroundColor(Color.MAGENTA);
        } else if(btnOption3.getText().toString().equals(correctAnswer)){
            btnOption3.setBackgroundColor(Color.MAGENTA);
        } else if(btnOption4.getText().toString().equals(correctAnswer)){
            btnOption4.setBackgroundColor(Color.MAGENTA);
        }
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateTimerText();
                // When the timer finishes, show final score or timeout message
                timeUp();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText("Timer: " + timeFormatted);
    }


    void showFinalScore(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Finished!");
        builder.setMessage("Final Score is:"+score);

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    void timeUp(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Time Up!");
        builder.setMessage("Final Score is:"+score+"\nQuestions Attempted: "+currentQuestion+"/"+totalQuestions);

        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
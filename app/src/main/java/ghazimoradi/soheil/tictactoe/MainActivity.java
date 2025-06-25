package ghazimoradi.soheil.tictactoe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    int count = 0;
    int numberOFPlayerOneWins = 0;
    int numberOFPlayerTwoWins = 0;
    byte[][] cells = new byte[3][3];
    ImageView[] imageViews = new ImageView[9];
    Boolean isPlayerOne = true;
    View.OnClickListener imageListener;
    TextView numberOFPlayerOneWinsTextView;
    TextView numberOFPlayerTwoWinsTextView;
    TextView playerOneTurn;
    TextView playerTwoTurn;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createView();
    }

    private void createView() {
        setUpView();
        setImageListener();
        setImagesListenerAndTags();
    }

    private void setImagesListenerAndTags() {
        for (int i = 0; i < 9; i++) {
            imageViews[i].setTag(i);
            imageViews[i].setOnClickListener(imageListener);
        }
    }

    private void setImageListener() {
        imageListener = view -> {
            ImageView imageView = (ImageView) view;
            int tag = (int) imageView.getTag();
            /*
            imageViewTag     row   col
                 0            0     0
                 1            0     1
                 2            0     2
                 3            1     0
                 4            1     1
                 5            1     2
                 6            2     0
                 7            2     1
                 8            2     2
             */
            int row = (int) Math.floor((double) tag / 3);
            int col = tag % 3;
            if (cells[row][col] > 0) {
                Toast.makeText(MainActivity.this, "clicked is illegal!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isPlayerOne) {
                imageView.setImageResource(R.drawable.circle);
                playerOneTurn.setVisibility(View.INVISIBLE);
                playerTwoTurn.setVisibility(View.VISIBLE);
                cells[row][col] = 1;
            } else {
                imageView.setImageResource(R.drawable.cross);
                playerOneTurn.setVisibility(View.VISIBLE);
                playerTwoTurn.setVisibility(View.INVISIBLE);
                cells[row][col] = 2;
            }
            isPlayerOne = !isPlayerOne;
            checkWin();
        };
    }

    private void setUpView() {
        imageViews[0] = findViewById(R.id.img1);
        imageViews[1] = findViewById(R.id.img2);
        imageViews[2] = findViewById(R.id.img3);
        imageViews[3] = findViewById(R.id.img4);
        imageViews[4] = findViewById(R.id.img5);
        imageViews[5] = findViewById(R.id.img6);
        imageViews[6] = findViewById(R.id.img7);
        imageViews[7] = findViewById(R.id.img8);
        imageViews[8] = findViewById(R.id.img9);
        numberOFPlayerOneWinsTextView = findViewById(R.id.numberOFPlayerOneWinsTextView);
        numberOFPlayerTwoWinsTextView = findViewById(R.id.numberOFPlayerTwoWinsTextView);
        playerOneTurn = findViewById(R.id.playerOneTurn);
        playerTwoTurn = findViewById(R.id.playerTwoTurn);
        resetButton = findViewById(R.id.resetBtn);
        resetButton.setOnClickListener(v -> resetGame(true));
    }

    private void checkWin() {
        count++;
        //rows
        for (int i = 0; i < 3; i++) {
            if (cells[i][0] != 0 && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2]) {
                sayWinner(cells[i][0]);
                return;
            }
        }
        //columns
        for (int i = 0; i < 3; i++) {
            if (cells[0][i] != 0 && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i]) {
                sayWinner(cells[0][i]);
                return;
            }
        }
        //diameter
        if (cells[0][0] != 0 && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2]) {
            sayWinner(cells[0][0]);
            return;
        }
        if (cells[0][2] != 0 && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0]) {
            sayWinner(cells[0][2]);
            return;
        }
        //tie
        if (count == 9) {
            sayWinner(3);
        }
    }

    private void resetGame(boolean isCallFromResetButton) {
        playerOneTurn.setVisibility(View.VISIBLE);
        playerTwoTurn.setVisibility(View.INVISIBLE);
        count = 0;
        if (isCallFromResetButton) {
            numberOFPlayerOneWins = 0;
            numberOFPlayerTwoWins = 0;
            numberOFPlayerOneWinsTextView.setText(String.valueOf(numberOFPlayerOneWins));
            numberOFPlayerTwoWinsTextView.setText(String.valueOf(numberOFPlayerTwoWins));
        }
        isPlayerOne = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = 0;
            }
        }
        for (int i = 0; i < 9; i++) {
            imageViews[i].setImageResource(0);
        }
    }

    private void sayWinner(int winnerId) {
        if (winnerId == 1) {
            numberOFPlayerOneWins++;
            numberOFPlayerOneWinsTextView.setText(String.valueOf(numberOFPlayerOneWins));
            showDialog("Player One Won");
        } else if (winnerId == 2) {
            numberOFPlayerTwoWins++;
            numberOFPlayerTwoWinsTextView.setText(String.valueOf(numberOFPlayerTwoWins));
            showDialog("Player Two Won");
        } else {
            showDialog("Tie!");
        }
        resetGame(false);
    }

    private void showDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        Button neutralButton = dialogView.findViewById(R.id.neutralButton);
        neutralButton.setOnClickListener(v -> dialog.dismiss());
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        dialogTitle.setText(title);
        dialog.setCancelable(false);
        dialog.show();
    }
}
package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Struct;

public class MainActivity extends AppCompatActivity {

    //CONSTANTS
    public static final String EQUAL = "=";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String DOT = ".";
    public static final String CLEAR = "CLEAR";
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String ZEROTEXT = "0";
    public static final String DISPLAYERROR = "Math Error.";
    public static final double STARTING_SUM = 0.0;
    public static final String DISPLAY_EQUAL = " = ";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //Check if the user alraedy pressed on '=' once
    public void alreadyPressedEqual(String exp, TextView calcSpace){
        calcSpace.setText(exp);
    }

    //Clear writing space
    public void clearCalcSpace(TextView calcSpace) {
        calcSpace.setText(EMPTY);
    }

    //Prints "Math Error"
    public void printError(TextView calcSpace) {
        calcSpace.setText(DISPLAYERROR);
    }


    //calculate and prints the final sum
    public void finalCalc(String totalExp, TextView calcSpace) {
        double totalSum = STARTING_SUM;

        if(totalExp.isEmpty()){                                                      //If Empty display 0
            calcSpace.setText(ZEROTEXT);
        } else if(totalExp.length() == 1){                                          //If nothing but the number is there dispaly = Num
            calcSpace.setText(DISPLAY_EQUAL + totalExp);
        } else if(totalExp.contains(EQUAL)){                                       //If there's already '=' in there, send it to the method that handles it
            alreadyPressedEqual(totalExp, calcSpace);
        } else if(totalExp.equals(DISPLAYERROR)){                                 //If the display is the error message, keep it the error message.
            printError(calcSpace);
        }else{
            if(totalExp.contains(SPACE)) {                                                      //What I did here was I split the String by using the split method, to find out how many spaces I've in the String
                String dupliTotalExp = totalExp;                                               // And there I split the split array again to a different array in order to separated the String by " " into array
                String[] split = dupliTotalExp.split(SPACE);
                String[] splitArray = totalExp.split(SPACE, split.length+1);

                totalSum = Double.valueOf(splitArray[0]);

                for(int i = 1; i < splitArray.length; i+=2 ){                                   //I knew now that in my array, every positive is a number and every odd is a operation, so I designed the loop by that.
                    if(splitArray[i].equals(PLUS)){
                        totalSum += Double.valueOf(splitArray[i+1]);
                    } else {
                        totalSum -= Double.valueOf(splitArray[i+1]);
                    }
                }

            }
            String finalSum = String.valueOf(totalSum);
            totalExp = totalExp + DISPLAY_EQUAL + finalSum;
            calcSpace.setText(totalExp);
        }
    }


    //check and handle minues and plus
    public void checkPlusMinus(String exp, String totalExp, TextView calcSpace){

        if(!totalExp.isEmpty()) {                                                                                                                             //Display the error message if I press on '-' or '+' while there's no numbers.
            char first = totalExp.charAt(totalExp.length() - 1);
            String firstString = String.valueOf(first);
            String secondString = EMPTY;

            if(totalExp.length() >= 2) {
                char second = totalExp.charAt(totalExp.length() - 2);
                secondString = String.valueOf(second);
            }
                                                                                                                                                              // //I check if the last and previous to last string are equal to '+' or '-' or '=' or the error message
            if (firstString.equals(PLUS) || firstString.equals(MINUS) || secondString.equals(PLUS) || secondString.equals(MINUS) || totalExp.equals(DISPLAYERROR) || totalExp.contains(EQUAL)) {
                printError(calcSpace);
            } else{
                totalExp += SPACE + exp + SPACE;
                calcSpace.setText(totalExp);
            }

        } else {
            printError(calcSpace);
        }
    }


    //checks and deals with points
    public void checkPoints(String exp, String totalExp, TextView calcSpace){

        if(!totalExp.isEmpty()) {                                                                       //checks if the calculate space is empty, because I can't insert a dot if nothing is there.
            char first = totalExp.charAt(totalExp.length() - 1);
            String firstString = String.valueOf(first);

            if (firstString.equals(DOT)) {                                                              //Checks if the last character was a '.', if so it display the error message.
                printError(calcSpace);
            } else {
                if(totalExp.contains(SPACE)) {                                                          //basically checks if there's more than 1 '.' in the number, if so display the error message.
                    String dupliTotalExp = totalExp;
                    String[] split = dupliTotalExp.split(SPACE);
                    String[] splitBySpaces = totalExp.split(SPACE, split.length);

                    if(splitBySpaces[splitBySpaces.length - 1].contains(DOT)){
                        printError(calcSpace);
                    } else {
                        totalExp += exp;
                        calcSpace.setText(totalExp);
                    }
                } else{
                    if(totalExp.contains(DOT)){
                        printError(calcSpace);
                    } else {
                        totalExp += exp;
                        calcSpace.setText(totalExp);
                    }
                }
            }
        } else{
            printError(calcSpace);
        }
    }


    //Printing to the calculator
    public void printToCalcSpace(String exp, TextView calcSpace){
        String totalExp = calcSpace.getText().toString();
                                                                            //goes over all the possible inputs, and it sending it to the right method
        if(exp.equals(MINUS) || exp.equals(PLUS)){
            checkPlusMinus(exp, totalExp, calcSpace);
        } else if (exp.equals(DOT)){
            checkPoints(exp, totalExp, calcSpace);
        } else if(exp.equals(EQUAL)) {
           finalCalc(totalExp, calcSpace);
        } else if(exp.equals(CLEAR)){
            clearCalcSpace(calcSpace);
        } else if(totalExp.equals(DISPLAYERROR)){
            printError(calcSpace);
        } else if(totalExp.contains(EQUAL)){
            clearCalcSpace(calcSpace);
            printToCalcSpace(exp,calcSpace);
        }else{
            totalExp += exp;
            calcSpace.setText(totalExp);
        }
    }


    //Main method, almost every click will go here
    public void calcMain(View view){
        TextView calcSpace = findViewById(R.id.calc_space);
        Button pressedButton = (Button) view;
        String pressedButtonText = pressedButton.getText().toString();
        printToCalcSpace(pressedButtonText, calcSpace);
    }

}
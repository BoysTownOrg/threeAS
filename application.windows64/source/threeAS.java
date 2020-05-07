import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class threeAS extends PApplet {

int pic1dur = 400;  //in msec
int stimdur = 400;
int pic2dur = 400;
int endblankdur = 1300;
int bgcolor = 255; //black = 0, 128 gray, 255 white; 
char threekey = '3'; //changing this may require a change to instructionText below
char fourkey = '4';
char fivekey = '5';
char sixkey = '6';
String instructionText = "Whenever you see numbers appear on the screen,\n\n"+
  "you press the number keys 3, 4, 5, or 6 \n\n"+"according to HOW MANY numbers you see on the screen.\n\n"+
  "Try to respond as quickly and accurately as you can.\n\n"+
  "Press space to begin.";
int myframerate = 60;

PImage picture, stimulus, blank;
String path;
Table table, newTable;
TableRow row;
boolean stimflag=true, FirstPicFlag=true, noMore = true;
boolean showPic1=false, showStim=false, showPic2=false, showBlank=false;
int rowCount=0, answer, correct, index;
int saveTime = millis()+1000000;
int stimTime, respTime;
IntList trialnums = new IntList();
int tablesize = 500;
int [] corrects = new int[tablesize];
String [] pictures = new String[tablesize];
String [] stimuli = new String[tablesize];
boolean init = true;
public void setup() {
  frameRate(myframerate);
  
  background(bgcolor);

  table = loadTable("3as.csv", "header");
  newTable = new Table();
  newTable.addColumn("picture");
  newTable.addColumn("stimulus");
  newTable.addColumn("correctresponse");
  newTable.addColumn("distance");
  newTable.addColumn("emotion");
  newTable.addColumn("answer");
  newTable.addColumn("RT");
  newTable.addColumn("correct");
  textAlign(CENTER, CENTER);
  textSize(32); 
  for (int i = 0; i < table.getRowCount(); i++) {
    trialnums.append(i);
  }
  trialnums.shuffle();

  for (int i = 0; i < table.getRowCount(); i++) {
    index = trialnums.get(i);
    row = table.getRow(index);
    newTable.addRow(row);
    corrects[i] = PApplet.parseInt(row.getString("correctresponse"))+2;
    pictures[i] = trim(row.getString("picture"));
    stimuli[i] = trim(row.getString("stimulus"));
  }

  blank = loadImage("blank.png");
}

public void draw() {
  if (saveTime+pic1dur+stimdur+pic2dur+endblankdur<=millis()) {

    rowCount += 1;
    FirstPicFlag = true;
    noMore = true;

    if (rowCount >= table.getRowCount()) {
      exit();
    }
    saveTime = millis();
  } else if (saveTime+pic1dur+stimdur+pic2dur<=millis()) {

    showBlank = true;
    showPic1 = false;
    showStim = false;
    showPic2 = false;
  } else if (saveTime+stimdur+pic1dur<=millis()) {

    showPic2=true;
    showBlank = false;
    showPic1 = false;
    showStim = false;
  } else if (saveTime+pic1dur<=millis()) {

    showStim = true;
    showPic1 = false;
    showPic2 = false;
    showBlank = false;
    if (stimflag) {
      stimTime = millis();
      stimflag = false;
    }
  } else if (saveTime<=millis()) {
    if (FirstPicFlag) {
      stimflag = true;
      correct = corrects[rowCount];
      picture = loadImage(pictures[rowCount]);
      stimulus = loadImage(stimuli[rowCount]);

      FirstPicFlag = false;
      showPic1 = true;
      showStim = false;
      showPic2 = false;
      showBlank = false;
    }
  } else {
  }

  if (showBlank) {
    image(blank, width/4, height/4, width/2, height/2);
  } else if (showPic2) {
    image(picture, width/4, height/4, width/2, height/2);
  } else if (showStim) {
    image(stimulus, width/4, height/4, width/2, height/2);
  } else if (showPic1) {
    image(picture, width/4, height/4, width/2, height/2);
  }
  if (init) {
    fill(0);
    text(instructionText, width/2, height/2);
  }
}

public void keyPressed() {

  if (key == ' ') {
    saveTime = millis()+100;
    init = false;
    background(bgcolor);
  }
  if (key == threekey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 3);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(3==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == fourkey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 4);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(4==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == fivekey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 5);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(5==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == sixkey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 6);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(6==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
}

public void exit() {
  String dayS = String.valueOf(day());
  String hourS = String.valueOf(hour());
  String minuteS = String.valueOf(minute());
  String myfilename = "AS3out"+"-"+dayS+"-"+hourS+"-"+minuteS+".csv";
  saveTable(newTable, myfilename, "csv");
  
  super.exit();
}

  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "threeAS" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

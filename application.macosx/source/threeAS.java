import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import org.multiply.processing.TimedEventGenerator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class threeAS extends PApplet {


private TimedEventGenerator myTimedEventGenerator;
PImage picture, stimulus, blank;  // Declare variable "a" of type PImage
String path;
Table table, newTable;
TableRow row;
boolean stimflag=true, FirstPicFlag=true, noMore = true;
boolean showPic1=false, showStim=false, showPic2=false, showBlank=false;
int rowCount=0, answer, correct, index;
int saveTime = millis()+1000000;
int stimTime, respTime;
IntList trialnums = new IntList();
int pic1dur = 400;
int stimdur = 400;
int pic2dur = 400;
int endblankdur = 1300;
boolean init = true;
public void setup() {
  //size(800, 800);
  
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
  textAlign(CENTER,CENTER);
  textSize(32); 
  for (int i = 0; i < table.getRowCount(); i++) {
    trialnums.append(i);
  }
  trialnums.shuffle();
  println(trialnums.max());
  println(trialnums.min());
  blank = loadImage("blank.png");
  myTimedEventGenerator = new TimedEventGenerator(this);
  myTimedEventGenerator.setIntervalMs(1);
}

public void draw() {
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
    text("Press the numbers 3, 4, 5, or 6 \naccording to HOW MANY numbers you see.\nPress space to begin.", width/2, height/2);
  }
}

public void onTimerEvent() {
  if (saveTime+pic1dur+stimdur+pic2dur+endblankdur<millis()) {
    saveTime = millis();
    rowCount += 1;
    FirstPicFlag = true;
    noMore = true;
    if (rowCount >= table.getRowCount()) {
      myTimedEventGenerator.setEnabled(false); 
      String dayS = String.valueOf(day());
      String hourS = String.valueOf(hour());
      String minuteS = String.valueOf(minute());
      String myfilename = "AS3out"+"-"+dayS+"-"+hourS+"-"+minuteS+".csv";
      saveTable(newTable, myfilename, "csv");
      println("Exit");
      exit();
    }
  } else if (saveTime+pic1dur+stimdur+pic2dur<millis()) {
    showBlank = true;
    showPic1 = false;
    showStim = false;
    showPic2 = false;
  } else if (saveTime+stimdur+pic1dur<millis()) {
    showPic2=true;
    showBlank = false;
    showPic1 = false;
    showStim = false;
  } else if (saveTime+pic1dur<millis()) {
    showStim = true;
    showPic1 = false;
    showPic2 = false;
    showBlank = false;
    if (stimflag) {
      stimTime = millis();
      stimflag = false;
    }
  } else if (saveTime<millis()) {
    if (FirstPicFlag) {
      stimflag = true;
      index = trialnums.get(rowCount);
      println(rowCount);
      println(index);
      row = table.getRow(index);
      newTable.addRow(row);
      correct = PApplet.parseInt(row.getString("correctresponse"))+2;
      picture = loadImage(trim(row.getString("picture")));
      stimulus = loadImage(trim(row.getString("stimulus")));
      FirstPicFlag = false;
      showPic1 = true;
      showStim = false;
      showPic2 = false;
      showBlank = false;
    }
  } else {
  }
}

public void keyPressed() {

  if (key == ' ') {
    saveTime = millis()+1000;
    init = false;
  }
  if (key == '3' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 3);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(3==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == '4' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 4);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(4==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == '5' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 5);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(5==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == '6' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 6);
    newTable.setInt(rowCount, "correct", PApplet.parseInt(6==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
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

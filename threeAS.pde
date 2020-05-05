
//int pic1dur = 400;  //in msec
//int stimdur = 400;
//int pic2dur = 400;
//int endblankdur = 1300;
int pic1dur = 12;  //in frames
int stimdur = 12;
int pic2dur = 12;
int endblankdur = 39;
int bgcolor = 255; //black = 0, 128 gray, 255 white; 
char threekey = '3'; //changing this may require a change to instructionText below
char fourkey = '4';
char fivekey = '5';
char sixkey = '6';
String instructionText = "Whenever you see numbers appear on the screen,\n\nyou press the number keys 3, 4, 5, or 6 \n\naccording to HOW MANY numbers you see on the screen.\n\nTry to respond as quickly and accurately as you can.\n\nPress space to begin.";
int curtime,framenum,calctime,frameadj;
int myframerate = 30;

PImage picture, stimulus, blank, black;  // Declare variable "a" of type PImage
String path;
Table table, newTable;
TableRow row;
boolean stimflag=true, FirstPicFlag=true, noMore = true,f1=false,f2=false,f3=false,f4=false;
boolean showPic1=false, showStim=false, showPic2=false, showBlank=false;
int rowCount=0, answer, correct, index;
int saveTime = frameCount+1000000;
int stimTime, respTime;
IntList trialnums = new IntList();
boolean init = true;
void setup() {
  //size(800, 800);
  frameRate(myframerate);
  //println(frameRate);
  fullScreen();
  background(bgcolor);
  framenum = frameCount;
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
  //println(trialnums.max());
  //println(trialnums.min());
  blank = loadImage("blank.png");
  black = loadImage("black.png");
  curtime = millis();
}

void draw() {
  if (saveTime+pic1dur+stimdur+pic2dur+endblankdur<frameCount) {
    //println("xxxx");
    rowCount += 1;
    FirstPicFlag = true;
    noMore = true;

    if (rowCount >= table.getRowCount()) {
      String dayS = String.valueOf(day());
      String hourS = String.valueOf(hour());
      String minuteS = String.valueOf(minute());
      String myfilename = "AS3out"+"-"+dayS+"-"+hourS+"-"+minuteS+".csv";
      saveTable(newTable, myfilename, "csv");
      //println("Exit");
      exit();
    }
    saveTime = frameCount;
  } else if (saveTime+pic1dur+stimdur+pic2dur<frameCount) {
    //println(frameCount);
    showBlank = true;
    showPic1 = false;
    showStim = false;
    showPic2 = false;
  } else if (saveTime+stimdur+pic1dur<frameCount) {
    //println(frameCount);
    showPic2=true;
    showBlank = false;
    showPic1 = false;
    showStim = false;
  } else if (saveTime+pic1dur<frameCount) {
    //println(frameCount);
    showStim = true;
    showPic1 = false;
    showPic2 = false;
    showBlank = false;
    if (stimflag) {
      stimTime = frameCount;
      stimflag = false;
    }
  } else if (saveTime<frameCount) {
    if (FirstPicFlag) {
      stimflag = true;
      f4=true;
      int tmptime = millis();
      index = trialnums.get(rowCount);
      row = table.getRow(index);
      newTable.addRow(row);
      correct = int(row.getString("correctresponse"))+2;
      picture = loadImage(trim(row.getString("picture")));
      stimulus = loadImage(trim(row.getString("stimulus")));
      calctime = millis()-tmptime;
      frameadj=round(calctime/myframerate);
      //println(frameadj);
      saveTime -= frameadj;
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
    if (f1){
      //println(millis()-curtime,frameCount-framenum,frameRate);
      f1=false;
    }
  } else if (showPic2) {
    image(picture, width/4, height/4, width/2, height/2);
    if (f2){
      //println(millis()-curtime,frameCount-framenum,frameRate);
      f2=false;
      f1=true;
    }
  } else if (showStim) {
    image(stimulus, width/4, height/4, width/2, height/2);
      if (f3){
      //println(millis()-curtime,frameCount-framenum,frameRate);
      f3=false;
      f2=true;
    }
} else if (showPic1) {
    image(picture, width/4, height/4, width/2, height/2);
    if (f4){
      //println(millis()-curtime);
      curtime = millis();
      framenum = frameCount;   
      f4=false;
      f3=true;
    }
  }
  if (init) {
    fill(0);
    text(instructionText, width/2, height/2);
  }
}

void keyPressed() {

  if (key == ' ') {
    saveTime = frameCount+6;
    init = false;
    background(bgcolor);
  }
  if (key == threekey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 3);
    newTable.setInt(rowCount, "correct", int(3==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == fourkey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 4);
    newTable.setInt(rowCount, "correct", int(4==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == fivekey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 5);
    newTable.setInt(rowCount, "correct", int(5==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key == sixkey && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 6);
    newTable.setInt(rowCount, "correct", int(6==correct));
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
}

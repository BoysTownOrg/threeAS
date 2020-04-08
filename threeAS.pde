/**
 * Load and Display 
 * 
 * Images can be loaded and displayed to the screen at their actual size
 * or any other size. 
 */

PImage picture, stimulus, blank;  // Declare variable "a" of type PImage
String path;
Table table, newTable;
TableRow row;
boolean stimflag=true, FirstPicFlag=true,noMore = true;
int rowCount=0, answer, correct, index;
int saveTime = millis()+1000000;
int stimTime, respTime;
IntList trialnums = new IntList();
void setup() {
  size(800, 800);
  table = loadTable("3asNoDirShort.csv", "header");
  newTable = new Table();
  newTable.addColumn("picture");
  newTable.addColumn("stimulus");
  newTable.addColumn("correctresponse");
  newTable.addColumn("distance");
  newTable.addColumn("emotion");
  newTable.addColumn("answer");
  newTable.addColumn("RT");

  for (int i = 0; i < table.getRowCount(); i++) {
    trialnums.append(i);
  }
  //trialnums.shuffle();
  println(trialnums.max());
  println(trialnums.min());
  blank = loadImage("blank.png");
}

void draw() {
  if (saveTime+1600<millis()) {
    image(blank, width/4, height/4, width/2, height/2);
    saveTime = 600+millis();
    rowCount += 1;
    FirstPicFlag = true;
    noMore = true;
    if (rowCount >= table.getRowCount()-1) {
      saveTable(newTable, "new.csv", "csv");
      println("Exit");
      exit();
    }
  } else if (saveTime+1200<millis()) {
    image(picture, width/4, height/4, width/2, height/2);
  } else if (saveTime+800<millis()) {
    image(stimulus, width/4, height/4, width/2, height/2);
    if (stimflag) {
      stimTime = millis();
      stimflag = false;
    }
  } else if (saveTime+400<millis()) {
    if (FirstPicFlag) {
      stimflag = true;
      index = trialnums.get(rowCount);
      println(rowCount);
      println(index);
      row = table.getRow(index);
      newTable.addRow(row);
      correct = int(row.getString("correctresponse"))+2;
      picture = loadImage(trim(row.getString("picture")));
      stimulus = loadImage(trim(row.getString("stimulus")));
      image(picture, width/4, height/4, width/2, height/2);
      FirstPicFlag = false;
    }
  } else {
  }
}


void keyPressed() {

  if (key >= ' ') {
    saveTime = millis();
  }
  if (key >= '3' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 3);
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key >= '4' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 4);
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key >= '5' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 5);
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
  if (key >= '6' && noMore) {
    noMore = false;
    respTime = millis();
    newTable.setInt(rowCount, "answer", 6);
    newTable.setFloat(rowCount, "RT", respTime-stimTime);
  }
}

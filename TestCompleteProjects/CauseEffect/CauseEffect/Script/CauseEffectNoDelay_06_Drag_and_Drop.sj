﻿//USEUNIT CauseEffect_01_ContinuosMovement
//USEUNIT CauseEffect_03_OneClick
//USEUNIT CauseEffect_05_MultipleClicks
//USEUNIT CauseEffectNoDelay_02_Navigation_NextLevel
//USEUNIT CauseEffect_06_Navigation_NextLevel
//USEUNIT CheckRegions

function causeEffect_OneClick()
{
try{
  //Launch the tested application.
  launchApp();
  
  //Removing leaves from the top layer of the scene by continuous movement of the mouse
  Log.AppendFolder("This test is for ContinuousMovement of the mouse");
  eraser ();
 //Navigation towards next level 
  CauseEffect_06_Navigation_NextLevel.navigationNextLevel();
  Log.PopLogFolder();  
  
  //Click the object on the screen so that it reaches the target in one step
  Log.AppendFolder("This test is for Reaching the goal with one click");  
  oneClickCar();  
   
  
  //Navigation towards next level 
  CauseEffect_06_Navigation_NextLevel.navigationNextLevel();
  Log.PopLogFolder(); 
  
  //Click the object on the screen so that it moves forward 1/3 towards the target and reach the target in 3 steps
  Log.AppendFolder("This test is for Reaching the goal with multiple clicks");  
  multipleClicks();
 
    
  //Navigation towards next level 
  CauseEffectNoDelay_02_Navigation_NextLevel.navigationNextLevel();
  Log.PopLogFolder();
 
 Log.AppendFolder("This test is for Reaching the goal with drag and drop");  
  DragAndDrop();  
  Log.PopLogFolder();
 }
 catch(e)
  {
    // Posts an exception message to the test log
    Log.Error(e.description);
    closeApp();
    // Stops the test execution
    Runner.Stop();
    
  }
 finally
 {     
   closeApp();
 }
}

function DragAndDrop()
{
  var coorX = 450;
  var coorY = 500;
 sDelay=1500;
 //LLCollection.DragAndDrop.Execute();
 LLPlayer.MouseDown(MK_LBUTTON, coorX, coorY, sDelay);
 LLPlayer.MouseUp(MK_LBUTTON, coorX+300, coorY, sDelay);
 LLPlayer.MouseDown(MK_LBUTTON, coorX, coorY, sDelay);
 LLPlayer.MouseUp(MK_LBUTTON, coorX+300, coorY, sDelay);
 
 Regions.AnimationCar.Check(Aliases.javaw.wndLWJGL2, false, false, 46024);
}

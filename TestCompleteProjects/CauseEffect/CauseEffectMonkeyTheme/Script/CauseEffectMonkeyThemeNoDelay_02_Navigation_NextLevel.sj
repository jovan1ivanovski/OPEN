﻿//USEUNIT CauseEffectMonkeyTheme_01_ContinuosMovement
//USEUNIT MT_CheckRegions
function causeEffect_Navigation_NextLevel()
{
try{
  //Launch the tested application.
  launchApp();
  
  //Removing leaves from the top layer of the scene by continuous movement of the mouse
   eraser ();
 
  //Navigation on the next level
  navigationNextLevel();
 }
 catch(e)
  {
    // Posts an exception message to the test log
    Log.Error(e.description);
    // Stops the test execution
    Runner.Stop();
  }
 finally
 {     
   closeApp();
 }
}

function navigationNextLevel()
{
//create region of the nextLevelButton
Region_NextLevel();

//Specifies the coordinates of the first click (it depends on the screen resolution)
var coorX = 1030;
var coorY = 920;
sDelay = 1000 ;
 
// Simulates pressing and releasing the left mouse button
LLPlayer.MouseDown(MK_LBUTTON, coorX, coorY, sDelay);
LLPlayer.MouseUp(MK_LBUTTON, coorX, coorY, sDelay); 
aqUtils.Delay(50);
LLPlayer.MouseDown(MK_LBUTTON, coorX+10, coorY+10, sDelay);
LLPlayer.MouseUp(MK_LBUTTON, coorX+10, coorY+10, sDelay);
  
 
//validate if same theme is displayed (means you are still on the same theme but different level)
Region_AnimationCar();
//Regions.CarStreetActive.Check(Aliases.javaw.wndLWJGL2, false, false, 46024);
}
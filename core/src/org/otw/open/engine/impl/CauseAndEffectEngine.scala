package org.otw.open.engine.impl

/**
  * Created by smirakovska on 1/26/2016.
  */

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.{Vector3, Vector2}
import com.badlogic.gdx.{Gdx, InputAdapter}
import org.otw.open.dto.{HorizontalMovingObject, Drawing}
import org.otw.open.engine.Engine
import org.otw.open.controllers.{CauseAndEffectFinishedUnsuccessfully, CauseAndEffectFinishedSuccessfully, ScreenController, Event}
import org.otw.open.engine.util.Animator

/**
  * CauseAndEffectEngine - handles horizontal object movement
  */
class CauseAndEffectEngine(val xRange: Range, val yRange: Range, objectStandPoints: List[Vector2]) extends InputAdapter with Engine {

  /**
    * set current input processor
    */
  Gdx.input.setInputProcessor(this)

  /**
    * Max number of failed attempts allowed
    */
  private val maxFailedAttempts = 3
  /**
    * The background texture where the object moves on.
    */
  private val backgroundTexture = new Texture(Gdx.files.internal("street-background.png"))
  /**
    * Time interval on which the movingObject moves.
    */
  private val MOVE_TIME_IN_SECONDS: Float = 0.1F
  /**
    * Movement of the object
    */
  private val DELTA_MOVEMENT: Int = 30
  /**
    * The starting point of the object to be animated
    */
  private val objectStartingPoint = objectStandPoints.head
  /**
    * Animator object
    */
  private val animator: Animator = new Animator("vibrating-car.atlas")
  /**
    * Transforms the click coordinates based on the screen size. Uses the camera transformation.
    */
  var transformator: Option[((Vector3) => Vector2)] = None
  /**
    * Boolean flag that is set to true when object is clicked
    */
  private var objectClicked: Boolean = false
  /**
    * Counter for the number of failed attempts
    */
  private var numOfFailedAttempts = 0
  /**
    * Current time.
    */
  private var timer = MOVE_TIME_IN_SECONDS
  /**
    * Moving object.
    */
  private var movingObject: HorizontalMovingObject = new HorizontalMovingObject(objectStartingPoint.x.toInt, objectStartingPoint.y.toInt, DELTA_MOVEMENT)

  /**
    * Timer for the vibrating object
    */
  private var animationTime = 0f

  /**
    * Method that handles mouse click on screen
    *
    * @param screenX x coordinate of the mouse click
    * @param screenY y coordinate of the mouse click
    * @param pointer
    * @param button  information about the button clicked
    * @return true if method is overridden
    */
  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    if (objectIsClicked(screenX, screenY)) true
    else {
      numOfFailedAttempts += 1
      if (numOfFailedAttempts == 3 && !objectClicked) ScreenController.dispatchEvent(CauseAndEffectFinishedUnsuccessfully)
      false
    }
  }

  /**
    * @param x x coordinate of mouse click
    * @param y y coordinate of mouse click
    * @return true if movingObject object is clicked
    */
  def objectIsClicked(x: Int, y: Int): Boolean = {
    val transformedPosition: Vector2 = transformator.get(new Vector3(x, y, 0))
    if (xRange.contains(transformedPosition.x.toInt)
      && yRange.contains(transformedPosition.y.toInt)) {
      objectClicked = true
      true
    }
    else false
  }

  override def getDrawings(delta: Float): List[Drawing] = {
    animationTime += delta
    if (objectClicked) {
      if (objectShouldStopAnimating(movingObject.x, movingObject.y))
        ScreenController.dispatchEvent(CauseAndEffectFinishedSuccessfully)
      else {
        timer = timer - delta
        if (timer < 0) {
          timer = MOVE_TIME_IN_SECONDS
          movingObject = movingObject.moveObject
        }
      }
    }
    List(new Drawing(backgroundTexture, 0, 0), new Drawing(animator.getCurrentTexture(animationTime), movingObject.x, movingObject.y))
  }

  /**
    * @param carX x coordinate of the movingObject
    * @param carY y coordinate of the movingObject
    * @return true if movingObject has reached the end point
    */
  def objectShouldStopAnimating(carX: Int, carY: Int): Boolean = {
    carX >= objectStandPoints.reverse.head.x
  }

  /**
    * @param transformator - High order function that transforms 3D to 2D coordinates
    * @return Boolean value indicating if method is overridden
    */
  override def setMouseClickPositionTransformator(transformator: (Vector3) => Vector2): Boolean = {
    this.transformator = Some(transformator)
    true
  }

  override def dispose(): Unit = {
    backgroundTexture.dispose()
    animator.dispose()
  }

}

object CauseAndEffectEngine {

  /**
    * @param xRange
    * @param yRange
    * @param objectStandPoints Vector2 points where object should stop its movement
    * @return new CauseAndEffectEngine
    */
  def apply(xRange: Range, yRange: Range, objectStandPoints: List[Vector2]): CauseAndEffectEngine = new CauseAndEffectEngine(xRange, yRange, objectStandPoints)
}
package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private lateinit var game: Game

    val timerHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        game = Game(this, pointsView, secsView)

        //initialize the game view class and game class
        game.setGameView(gameView)
        gameView.setGame(game)
        startTimer()
        newGame()

        moveRight.setOnClickListener {
            game.currentDirection = Game.direction.RIGHT
        }
        moveLeft.setOnClickListener {
            game.currentDirection = Game.direction.LEFT
        }
        moveUp.setOnClickListener {
            game.currentDirection = Game.direction.UP
        }
        moveDown.setOnClickListener {
            game.currentDirection = Game.direction.DOWN
        }
        playpause.setOnClickListener {
            game.stopStartHandler()
            if(game.paused)
                timerHandler.removeCallbacksAndMessages(null)
            else
                startTimer()
        }
    }

    override fun onStop() {
        super.onStop()
        timerHandler.removeCallbacksAndMessages(null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            startTimer()
            newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startTimer() {
        timerHandler.post(object : Runnable {
            override fun run() {
                timerHandler.postDelayed(this, 1000)
                game.updateGameTime()
                if(game.secs > 60 * 1000) {
                    timerHandler.removeCallbacksAndMessages(null)
                }
            }
        })
    }

    private fun newGame() {
        game.newGame()
    }
}

package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.util.ArrayList

class Game(private var context: Context, private var pointsView: TextView, private var secsView: TextView) {

    var points: Int = 0
    var secs: Int = 0
    var pacBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
    var pacx: Int = 0
    var pacy: Int = 0

    var paused = false

    var difficulty: Int = 0

    val mainHandler = Handler(Looper.getMainLooper())

    enum class direction {
        LEFT, RIGHT, UP, DOWN
    }

    var currentDirection: direction = direction.RIGHT

    //did we initialize the coins?
    var coinsInitialized = false
    var enemiesInitialized = false

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()
    var enemies = ArrayList<Enemy>()

    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0 //height and width of screen

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins() {
        var x = 0
        while (x < 10) {
            var coin = GoldCoin(context)
            coin.coinX = (0..1000).random()
            coin.coinY = (0..1000).random()
            coins.add(coin)
            x++
        }
        //DO Stuff to initialize the array list with some coins.
        coinsInitialized = true
    }

    fun initializeEnemies() {

            var enemy = Enemy(context)
            enemy.enemyX = w - 200
            enemy.enemyY = 450
            enemies.add(enemy)

        //DO Stuff to initialize the array list with some coins.
        enemiesInitialized = true
    }

    fun newGame() {
        mainHandler.removeCallbacksAndMessages(null)
        pacx = 50
        pacy = 400
        coinsInitialized = false
        enemiesInitialized = false
        points = 0
        secs = 0
        coins.clear()
        enemies.clear()
        pointsView.text = "Points: ${points}"
        secsView.text = "Seconds: ${secs}"
        gameView?.invalidate() //redraw screen
        update()
    }

    private fun update() {
        mainHandler.post(object : Runnable {
            override fun run() {
                movePacman()
                moveEnemies()
                updatePoints()
                mainHandler.postDelayed(this, 400)
            }
        })
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun updatePoints() {
        pointsView.text = "Points: ${points}"
    }

    fun updateGameTime() {
        secs++
        secsView.text = "Seconds: ${secs}"
    }

    fun movePacman() {
        val movement = 100
        when(currentDirection) {
            direction.RIGHT ->
                if (pacx + movement + pacBitmap.width < w) {
                    pacx += movement // pacx = pacx + pixels
                    gameView!!.invalidate()
                }

            direction.LEFT ->
                if (pacx - movement >= 0) {
                    pacx -= movement // pacx = pacx - pixels
                    gameView!!.invalidate()
                }

            direction.UP ->
                if (pacy - movement >= 0) {
                    pacy -= movement // pacy = pacy - pixels
                    gameView!!.invalidate()
                }

            direction.DOWN ->
                if (pacy + movement + pacBitmap.height < h) {
                    pacy += movement // pacy = pacy + pixels
                    gameView!!.invalidate()
                }
        }
    }

    fun moveEnemies() {
        for (enemy in enemies) {
            val movement = 50 + difficulty
            var randomMovement = (1..4).random()
            when(randomMovement) {
                1 ->
                    if (enemy.enemyX + movement + enemy.enemyBitmap.width < w) {
                        enemy.enemyX += movement
                        gameView!!.invalidate()
                    }

                2 ->
                    if (enemy.enemyX - movement >= 0) {
                        enemy.enemyX -= movement
                        gameView!!.invalidate()
                    }

                3 ->
                    if (enemy.enemyY - movement >= 0) {
                        enemy.enemyY -= movement
                        gameView!!.invalidate()
                    }

                4 ->
                    if (enemy.enemyY + movement + enemy.enemyBitmap.height < h) {
                        enemy.enemyY += movement
                        gameView!!.invalidate()
                    }
            }
        }
    }

    fun stopStartHandler() {
        paused = if(paused) {
            update()
            false
        } else {
            mainHandler.removeCallbacksAndMessages(null)
            true
        }
    }
}
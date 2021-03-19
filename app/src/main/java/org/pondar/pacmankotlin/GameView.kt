package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlin.math.sqrt


//note we now create our own view class that extends the built-in View class
class GameView : View {

    private lateinit var game: Game
    private var h: Int = 0
    private var w: Int = 0 //used for storing our height and width of the view

    fun setGame(game: Game) {
        this.game = game
    }

    fun distance (x1: Int, y1:Int, x2:Int, y2:Int):Float {
        var result = (((y2-y1)*(y2-y1))+((x2-x1)*(x2-x1)))
        return sqrt(result.toDouble()).toFloat()
    }

    /* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //In the onDraw we put all our code that should be
    //drawn whenever we update the screen.
    override fun onDraw(canvas: Canvas) {
        //Here we get the height and weight
        h = height
        w = width
        //update the size for the canvas to the game.
        game.setSize(h, w)
        Log.d("GAMEVIEW", "h = $h, w = $w")




        //Making sure that the game is over or continuing
        if (game.points < 10 && game.secs < 60){

            //Making a new paint object
            val paint = Paint()
            canvas.drawColor(Color.WHITE) //clear entire canvas to white color

            //are the coins initiazlied?
            //if not initizlise them
            if (!(game.coinsInitialized))
                game.initializeGoldcoins()

            if (!(game.enemiesInitialized))
                game.initializeEnemies()

            canvas.drawBitmap(game.pacBitmap, game.pacx.toFloat(),
                    game.pacy.toFloat(), paint)

            for(coin in game.coins) {

                if (!(coin.taken)) {
                    if (distance(game.pacx, game.pacy, coin.coinX, coin.coinY) < 150) {
                        coin.taken = true
                        game.points ++
                    } else {canvas.drawBitmap(coin.goldCoinBitmap, coin.coinX.toFloat(),
                            coin.coinY.toFloat(), paint)
                    }
                }
            }

            for(enemy in game.enemies) {
                 if (distance(game.pacx, game.pacy, enemy.enemyX, enemy.enemyY) < 150) {
                     game.newGame()
                        Toast.makeText(context, "Game Over", Toast.LENGTH_LONG).show()
                    } else {canvas.drawBitmap(enemy.enemyBitmap, enemy.enemyX.toFloat(),
                         enemy.enemyY.toFloat(), paint)
                    }
            }
        } else {
            if(game.secs > 60) {
                game.newGame()
                Toast.makeText(context, "Game Over", Toast.LENGTH_LONG).show()
            }
            if(game.points > 9)
                game.difficulty += 20
                game.newGame()
                Toast.makeText(context, "You won!", Toast.LENGTH_LONG).show()
        }

        game.updatePoints()
        super.onDraw(canvas)
    }

}

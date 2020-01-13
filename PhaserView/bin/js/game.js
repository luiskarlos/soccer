var SimpleGame = (function () {
    function SimpleGame() {
        // create our phaser game
        // 800 - width
        // 600 - height
        // Phaser.AUTO - determine the renderer automatically (canvas, webgl)
        // { preload:this.preload, create:this.create} - functions to call for our states
        this.game = new Phaser.Game(800, 600, Phaser.AUTO, 'content', { preload: this.preload, create: this.create, update: this.update });
    }
    SimpleGame.prototype.preload = function () {
        // add our logo image to the assets class under the
        // key 'logo'. We're also setting the background colour
        // so it's the same as the background colour in the image
        this.game.load.image('player', 'assets/sprites/redPlayer.png');
        this.game.stage.backgroundColor = 0xB20059;
    };
    SimpleGame.prototype.update = function () {
        if (parent["engine"].update) {
            parent["engine"].update();
        }
    };
    SimpleGame.prototype.create = function () {
        this.s = this.game.add.sprite(0, 0, 'player');
        this.s.anchor.setTo(0.5, 0.5);
        var simpleGame = this;
        parent["engine"] = parent["engine"] || {};
        parent["engine"].updatePosition = function (id, x, y, angle) {
            if (id === 'blue.player1') {
                simpleGame.s.angle = simpleGame.game.math.radToDeg(angle);
                simpleGame.s.x = x;
                simpleGame.s.y = y;
            }
            //console.log('Update position ' + id + ' x = ' + x + ' y = ' + y);
        };
    };
    return SimpleGame;
})();
var game = new SimpleGame();

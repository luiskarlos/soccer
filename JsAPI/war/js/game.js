var Soccer = (function () {
    function Soccer() {
      // create our phaser game
      // 800 - width
      // 600 - height
      // Phaser.AUTO - determine the renderer automatically (canvas, webgl)
      // { preload:this.preload, create:this.create} - functions to call for our states
      this.game = new Phaser.Game(800, 600, Phaser.AUTO, 'content', { preload: this.preload, create: this.create, update: this.update });
    }

    Soccer.prototype.preload = function () {
      this.game.load.image('dust', 'assets/sprites/fireblob.png');
      this.game.load.image('REDPlayer', 'assets/sprites/red-player.png');
      this.game.load.image('REDgp', 'assets/sprites/red-gp.png');
      this.game.load.image('BLUEPlayer', 'assets/sprites/blue-player.png');
      this.game.load.image('BLUEgp', 'assets/sprites/blue-gp.png');
      //this.game.load.image('ball', 'assets/sprites/ball-small.png');
      this.game.load.spritesheet('ball', 'assets/sprites/ball-roll.png', 12, 12, 10);
      this.game.stage.backgroundColor = 0xB20059;
    }

    Soccer.prototype.update = function () {
      if (parent["engine"].update) {
        parent["engine"].update();
      }
    }

    Soccer.prototype.create = function () {
      var self = this;
      self.entities = {};

      self.game.input.onTap.add(function onTap(pointer, doubleTap) {
        if (doubleTap) {
          self.game.debug.text( "Double Tab");
        } else {
          self.game.debug.text( "Tab ------------- > ");
          parent["engine"].click(pointer.x, pointer.y);
        }
      }, self);
      debugger;
      parent["engine"] = parent["engine"] || {};
      parent["engine"].addBall = function (x, y, angle) {
        var image = self.game.add.sprite(200, 360, 'ball');
        self.game.anim = image.animations.add('roll');
        self.game.anim.play(10, true);

        var emitter = self.game.add.emitter(0, 0, 10);
        emitter.makeParticles('dust');
        image.addChild(emitter);
        emitter.y = 0;
        emitter.x = -16;
        emitter.lifespan = 500;
        emitter.maxParticleSpeed = new Phaser.Point(-100,50);
        emitter.minParticleSpeed = new Phaser.Point(-200,-50);

        emitter.start(false, 5000, 100);
        addEntity(self, "ball", x, y, angle, image);
      };

      parent["engine"].addGoalkeeper = function (team, id, x, y, angle) {
        var image = self.game.add.sprite(0, 0, team + 'gp');
        addEntity(self, id, x, y, angle, image);
      };

      parent["engine"].addPlayer = function (team, id, x, y, angle) {
        var image = self.game.add.sprite(0, 0, team + 'Player');
        addEntity(self, id, x, y, angle, image);
      };

      parent["engine"].updatePosition = function (id, x, y, angle, speed) {
    	if (id === "ball") {
    	  //console.log("ball speed = ", ((speed*25)/4));
    	  //emitter.start(false, 5000, 100);
   	      self.game.anim.speed = 18*((speed*25)/4);
   	      //self.entities['ball'].getChildAt(0).start();
    	}

        self.entities[id].angle = self.game.math.radToDeg(angle);
        self.entities[id].x = x;
        self.entities[id].y = y;
      };
    };

    function addEntity(self, id, x, y, angle, image) {
      self.entities[id] = image;
      self.entities[id].anchor.setTo(0.5, 0.5);
      self.entities[id].angle = self.game.math.radToDeg(angle);
      self.entities[id].x = x;
      self.entities[id].y = y;
    }

    return Soccer;
})();
var game = new Soccer();

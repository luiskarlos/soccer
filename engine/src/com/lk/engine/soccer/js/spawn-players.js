
function gameSpawnPlayers(game) {
    game.spawn("blue.team", "GOALKEEPER", "blue.goalkeeper", 1);
    game.spawn("blue.team", "DEFENDER", "blue.player1", 6);
    game.spawn("blue.team", "DEFENDER", "blue.player2", 8);
    game.spawn("blue.team", "ATTACKER", "blue.player3", 3);
    game.spawn("blue.team", "ATTACKER", "blue.player4", 5);

    game.spawn("red.team", "GOALKEEPER", "red.goalkeeper", 16);
    game.spawn("red.team", "DEFENDER", "red.player1", 9);
    game.spawn("red.team", "DEFENDER", "red.player2", 11);
    game.spawn("red.team", "ATTACKER", "red.player3", 12);
    game.spawn("red.team", "ATTACKER", "red.player4", 14);

    game.entity('blue.goalkeeper').getParams().setAttackRegion(1);
    game.entity('blue.player1').getParams().setAttackRegion(12);
    game.entity('blue.player2').getParams().setAttackRegion(13);
    game.entity('blue.player3').getParams().setAttackRegion(6);
    game.entity('blue.player4').getParams().setAttackRegion(4);

    game.entity('blue.goalkeeper').getParams().setDefenseRegion(1);
    game.entity('blue.player1').getParams().setDefenseRegion(6);
    game.entity('blue.player2').getParams().setDefenseRegion(8);
    game.entity('blue.player3').getParams().setDefenseRegion(3);
    game.entity('blue.player4').getParams().setDefenseRegion(5);

    game.entity('red.goalkeeper').getParams().setAttackRegion(16);
    game.entity('red.player1').getParams().setAttackRegion(4);
    game.entity('red.player2').getParams().setAttackRegion(6);
    game.entity('red.player3').getParams().setAttackRegion(9);
    game.entity('red.player4').getParams().setAttackRegion(11);

    game.entity('red.goalkeeper').getParams().setDefenseRegion(16);
    game.entity('red.player1').getParams().setDefenseRegion(9);
    game.entity('red.player2').getParams().setDefenseRegion(11);
    game.entity('red.player3').getParams().setDefenseRegion(12);
    game.entity('red.player4').getParams().setDefenseRegion(14);

    game.before('Wait', 'ChaseBall');
    game.before('SupportAttacker', 'ChaseBall', 'ReturnToHomeRegion');
    game.after('Dribble', 'ChaseBall');

    game.before('ChaseBall', 'KickBall');
    game.after('ChaseBall', 'ReturnToHomeRegion');

    game.before('ReceiveBall', 'ChaseBall');
    game.before('ReturnToHomeRegion', 'ChaseBall');

    game.change('referee', 'referee.prepareForKickOff');
    game.change('RED', 'PrepareForKickOff');
    game.change('BLUE', 'PrepareForKickOff');

}

$doc.gameSpawnPlayers = gameSpawnPlayers;

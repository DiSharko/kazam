KAZAM! - README
=====

Kazam! is an online multiplayer game set in a magical universe. All players
specialize in a certain realms of magic such that they are masters of 8
powerful spells. They must wield these spells in the arena with equal amounts of
vigilance, valor and selflessness, for this is the only way in which they can
bring their team victory and attain the Kazam Kup!

STARTUP
------
To run the game, the user must execute ./kazam.jar within a shell. The game will launch into
a splash screen, after which they must click "Play" to enter the matchmaking screen. At this
stage, the user is presented with two options: "Join Game" and "Host Game". In order to play
as a group, the group must decide beforehand on a "host" player who is responsible for choosing
the map and clicking "Host Game". Note that there can only be one host player per group. 

Both the Join Game and Host Game screens require the user to choose a set of 8 spells
for their endowment during the game. They can choose from a list of 17 spells detailed
later in the document.

Once all players have chosen their spells, the host player must click "Host Game", and 
then proceed to distribute his IP address to the other players who want to join the game.
Those players must enter the IP address in the "Join Game" screen and then proceed to click
"Connect". This was launch each player into a screen that displays whether the server
connection has been made, and if so, whether they are waiting for the host player to start
the game. Once all players are in the waiting screen, the host player may click "Start Game"
to start the game.

GAMEPLAY
-------
Upon joining the world for the game, the players are immediately placed into two teams, each
having their own respective spawn (starting) points for their players. Depending on the map
chosen, the players will play either Capture the Flag (CTF), or Team Deathmatch (TD). Currently,
Kazam has support for CTF in the "Department of Secrets" map, and for TD in the "Chamber of Mysteries"
map.

Capture the Flag involves a single flag object centralized between the teams' respective bases and 
spawn points, and teams score points when their players collect this flag and deposit it into their
designated flag zone. In the Department of Secrets map, the designated flag zones for each team are
diagonally across the map.

----------------------------------
| DFZ 2  |				| DFZ 1  |
----------------------------------
| TEAM 1 |				| TEAM 2 |
| SPAWN  |      FLAG    | SPAWN  |
----------------------------------
Figure 1 - Department of Secrets

Team Deathmatch involves a much simpler objective, simply killing players of the opposing team. The
Chamber of Mysteries map is much larger than the Department of Secrets, and features a number of
obstacles and shortcuts to excite the game. Each team gets 1 point for killing their opponents, and the
game ends after a team has reached a set number of points.

PLAYERS
------
Every player has 2 main attributes controlling their gameplay: health and mana. Health, put simply, is 
the amount of life a player has available; when it becomes 0, the player dies. Mana is the amount of energy
or vigor the player has when casting spells: each spell requires a certain amount of energy to be casted, and
if the spell is casted the player's energy reserve (mana) will be depleted by the respective amount. In the
case that the spell's requirement for mana exceeds the player's total mana, the player will not be able to
cast that spell. Note that casting the same spell repeatedly makes the caster more and more fatigued with
every cast due to repetitiveness; the mana bar is depleted progresively more accordingly. There is a smaller
penalty for casting different spells in extremely quick succession, as this also fatigues the player.

For players, one more important thing to consider during gameplay is the "cooldown" for every spell. The laws
of the Kazam universe referee the number of times a player can cast a spell within a set period of time, thus
a spell directly goes into a "cooldown" mode after casting, where it cannot be cast for a set duration of time depending on the spell. 
SPELLS
------
These are the spells available to each wizard/witch :
	Abracadabra 	-> 	The killing spell, immediately kills an enemy. It is on a 10 second
						cooldown and costs 30 mana.
	Burn			->	Performs damage to health and mana over time upon hitting an enemy.
						It is on a 1.5 second cooldown and costs 12 mana.
	Cleanse			->	When self-casted on a player, immediately removes all de-buffs (roots,
						silences, burns, etc.). It is on a 5 second cooldown and costs 20 mana.
	Clone			->	Sends of a clone of the player in the direction of casting, which
						immediately disappears upon collision with any object. It is on a 1
						second cooldown but costs 30 mana.
	Confuse 		->	Silences a target and sends them moving in random directions for the
						course of 3 seconds. It is on a 3 second cooldown and costs 30 mana.
	Dash			->	When self-casted on a player. , immdiately propels them a large distance
						in the direction of the cursor. It is on a 4 second cooldown, and costs 10
						mana.
	Disarm			->	Pushes the target's wand out of their hands, silencing them such that
						they cannot cast spells for 2.5 seconds. It is on a 3 second cooldown, and
						costs 10 mana.
	Felify			->	Turns a target object, whether it be a player, rock, or flag, into a cat.
						If it hits a player, they are silenced for 3 seconds. It is on a 3 second
						cooldown and costs 10 mana.
	Hide			->	When self-casted on a player, causes them to fade out of visibility and back
						in over the course of 3.5 seconds. It is on a 6 second cooldown and costs
						20 mana.
	Lock			->	Upon collision with a door, immediately locks/closes the door. It is on a 1
						second cooldown and costs 10 mana.
	Open			->	Upon collision with a door, immediately unlocks/opens the door. It is on a 1
						second cooldown and costs 10 mana.
	Push 			->	Propels a target ally or enemy directly away from the caster through a certain
						distance. It is on a 1 second cooldown and costs 10 mana.
	Rejuvenate		->	Heals the target player by 30 health over the course of 2 seconds. Note, this
						can be cast on enemies. It is on a 2 second cooldown and costs 10 mana.
	Root			->	Causes the target player to grow roots into the ground, preventing them from moving
						for 3 seconds. It is on a 3 second cooldown and costs 10 mana.
	Shield			->	When self-casted on a player, erects a forceful shield in the direction of the
						cursor that deflects all incoming spells in that direction. It is on a 3 second
						cooldown and costs 20 mana.
	Stun			->	Causes a target player to be silenced and rooted into the ground, preventing them from
						performing any actions for 2 seconds. It is on a 3 second cooldown and costs 25
						mana.
	Summon			->	Causes a target flag to quickly gravitate towards the caster. It is on a 1 second
						cooldown and costs 10 mana.

STRATEGY
------
Just have fun! :)
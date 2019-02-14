package edu.smith.cs.csc212.p4;

import java.util.HashMap;
import java.util.Map;

/**
 * SpookyMansion, the game.
 * @author jfoley
 *
 */
public class SpookyMansion implements GameWorld {
	private Map<String, Place> places = new HashMap<>();
	
	/**
	 * Where should the player start?
	 */
	@Override
	public String getStart() {
		return "entranceHall";
	}

	/**
	 * This constructor builds our SpookyMansion game.
	 */
	public SpookyMansion() {
		
		// In the entranceHall, create an room and different exits to other areas of the mansion
		Place entranceHall = insert(
				Place.create("entranceHall", "You are in the grand entrance hall of a large building.\n"
						+ "The front door is locked. How did you get here?"));
		entranceHall.addExit(new Exit("basement", "There are stairs leading down."));
		entranceHall.addExit(new Exit("attic", "There are stairs leading up."));
		entranceHall.addExit(new Exit("kitchen", "There is a red door."));
		
		// In the basement, create a room and different exits to other areas of the mansion
		Place basement = insert(
				Place.create("basement", "You have found the basement of the mansion.\n" + 
		                           "It is darker down here.\n" +
						"You get the sense a secret is nearby, but you only see the stairs you came from."
						));
		basement.addExit(new Exit("entranceHall", "There are stairs leading up."));
		basement.addExit(new Exit("basement2", "There is a pathway going to a smaller room attached to the basement."));

		// In the second basement, create a room and different exits to other areas of the mansion
		Place basement2 = insert(Place.create("basement2", "You have found a smaller room of the basement which is used for storage."));
		basement2.addExit(new Exit("basement", "There is a pathway going back to the main basement."));
		
		// In the attic, create a room and different exits to other areas of the mansion
		Place attic = insert(Place.create("attic",
				"Something rustles in the rafters as you enter the attic. Creepy.\n" + "It's big up here."));
		attic.addExit(new Exit("entranceHall", "There are stairs leading down."));
		attic.addExit(new Exit("attic2", "There is more through an archway"));

		// In the second attic, create a room and different exits to other areas of the mansion
		Place attic2 = insert(Place.create("attic2", "There's definitely a bat in here somewhere.\n"
				+ "This part of the attic is brighter, so maybe you're safe here."));
		attic2.addExit(new Exit("attic", "There is more back through the archway"));
		attic2.addExit(new Exit("attic3", "There is a small coridoor to go through."));
		
		// In the third attic, create a room and different exits to other areas of the mansion
		Place attic3 = insert(Place.create("attic3", "You've found a small dusty room in the attic, filled with spider webs. "
				+ "This part of the attic has old furniture under cloths, which is scary."));
		attic3.addExit(new Exit("attic2", "There is a small coridoor to go through."));
		
		// In the kitchen, create a room and different exits to other areas of the mansion
		Place kitchen = insert(Place.create("kitchen", "You've found the kitchen. You smell old food and some kind of animal."));
		kitchen.addExit(new Exit("entranceHall", "There is a red door."));
		kitchen.addExit(new Exit("dumbwaiter", "There is a dumbwaiter."));
		
		// In the dumbwaiter, create a place and different exits to other areas of the mansion
		Place dumbwaiter = insert(Place.create("dumbwaiter", "You crawl into the dumbwaiter. What are you doing?"));
		dumbwaiter.addExit(new Exit("secretRoom", "Take it to the bottom."));
		dumbwaiter.addExit(new Exit("kitchen", "Take it to the middle-level."));
		dumbwaiter.addExit(new Exit("attic2", "Take it up to the top."));
		
		// In the secretRoom, create a room and different exits to other areas of the mansion
		Place secretRoom = insert(Place.create("secretRoom", "You have found the secret room."));
		secretRoom.addExit(new Exit("hallway0", "There is a long hallway."));
		secretRoom.addExit(new Exit("basement", "Exit the secret room to another room."));
		
		
		int hallwayDepth = 5;
		int lastHallwayPart = hallwayDepth - 1;
		
		// depending on how far the user is in the hallway, prints different exit suggestions
		for (int i=0; i<hallwayDepth; i++) {
			Place hallwayPart = insert(Place.create("hallway" + i, "This is a very long hallway."));
			if (i == 0) {
				hallwayPart.addExit(new Exit("secretRoom", "Go back."));
			} else {
				hallwayPart.addExit(new Exit("hallway"+(i-1), "Go back."));
			}
			if (i != lastHallwayPart) {
				int step = i + 1;
				hallwayPart.addExit(new Exit("hallway"+(i+1), "Go forward. You have taken "+ step + " out of 6 steps"));

			} else {
				hallwayPart.addExit(new Exit("crypt", "There is darkness ahead. You have taken 5 out of 6 steps. You have one step left."));
			}
		}
		
		// In the crypt, create a place which marks the end of the game
		Place crypt = insert(Place.terminal("crypt", "You have found the crypt.\n"
				+"It is scary here, but there is an exit to outside.\n"+
				"Maybe you'll be safe out there."));
		
		// Make sure your graph makes sense!
		checkAllExitsGoSomewhere();
	}

	/**
	 * This helper method saves us a lot of typing. We always want to map from p.id
	 * to p.
	 * 
	 * @param p - the place.
	 * @return the place you gave us, so that you can store it in a variable.
	 */
	private Place insert(Place p) {
		places.put(p.getId(), p);
		return p;
	}

	/**
	 * I like this method for checking to make sure that my graph makes sense!
	 */
	private void checkAllExitsGoSomewhere() {
		boolean missing = false;
		// For every place:
		for (Place p : places.values()) {
			// For every exit from that place:
			for (Exit x : p.getVisibleExits()) {
				// That exit goes to somewhere that exists!
				if (!places.containsKey(x.getTarget())) {
					// Don't leave immediately, but check everything all at once.
					missing = true;
					// Print every exit with a missing place:
					System.err.println("Found exit pointing at " + x.getTarget() + " which does not exist as a place.");
				}
			}
		}
		
		// Now that we've checked every exit for every place, crash if we printed any errors.
		if (missing) {
			throw new RuntimeException("You have some exits to nowhere!");
		}
	}

	/**
	 * Get a Place object by name.
	 */
	public Place getPlace(String id) {
		return this.places.get(id);		
	}
}
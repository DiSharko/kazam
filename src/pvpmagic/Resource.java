package pvpmagic;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import screen.BorderScreen;

public class Resource {


	public static boolean _useNativeBorder = false;
	public static int _borderHeight;

	public static String _title = "PvP Magic, the world's lamest name for a game ever";


	public static int _fps = 0;

	public static String _buttonFontName = "Times";

	public static ArrayList<String> _characters;
	public static ArrayList<String> _spells;
	public static ArrayList<String> _gameTypes;
	public static ArrayList<String> _maps;

	public static HashMap<String, Image> _gameImages;
	public static HashMap<String, Image> _ui;

	public Resource(){
		if (_useNativeBorder) _borderHeight = 0;
		else _borderHeight = BorderScreen._topBarHeight;

		_characters = new ArrayList<String>();
		_spells = new ArrayList<String>();
		_gameTypes = new ArrayList<String>();
		_maps = new ArrayList<String>();
		
		_gameImages = new HashMap<String, Image>();
		
		_ui = new HashMap<String, Image>();

		try {
			BufferedReader charactersFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/characters.txt")));
			String line;
			while ((line = charactersFile.readLine()) != null){
				_characters.add(line);
			}

		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}


		try {
			BufferedReader spellsFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/spells.txt")));
			String line;
			while ((line = spellsFile.readLine()) != null){
				_spells.add(line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}


		try {
			BufferedReader gameTypesFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/gameTypes.txt")));
			String line;
			while ((line = gameTypesFile.readLine()) != null){
				_gameTypes.add(line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}


		try {
			BufferedReader mapsFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/maps.txt")));
			String line;
			while ((line = mapsFile.readLine()) != null){
				_maps.add(line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}



//		try {
//			Image tempSheet = new ImageIcon(Resource.class.getResource("/media/images/ui.png")).getImage();
//			BufferedImage sheet = new BufferedImage(tempSheet.getWidth(null),tempSheet.getHeight(null),BufferedImage.TYPE_INT_ARGB);
//			Graphics g = sheet.getGraphics();
//			g.drawImage(tempSheet, 0, 0, null);
//			g.dispose();
//
//			BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/images/ui.txt")));
//
//			String line;
//			while ((line = file.readLine()) != null){
//				try {
//					String[] part = line.split(" ");
//					_ui.put(part[4], sheet.getSubimage(Integer.parseInt(part[0]), Integer.parseInt(part[1]), Integer.parseInt(part[2]), Integer.parseInt(part[3])));
//				} catch (NumberFormatException e) {
//					e.printStackTrace();
//				} catch (IndexOutOfBoundsException e){
//					e.printStackTrace();
//				}
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NullPointerException e){
//			e.printStackTrace();
//		}
//		
//		try {
//			Image tempSheet = new ImageIcon(Resource.class.getResource("/media/images/gameImagesAlpha.png")).getImage();
//			BufferedImage sheet = new BufferedImage(tempSheet.getWidth(null),tempSheet.getHeight(null),BufferedImage.TYPE_INT_ARGB);
//			Graphics g = sheet.getGraphics();
//			g.drawImage(tempSheet, 0, 0, null);
//			g.dispose();
//			
//			BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/images/gameImagesAlpha.txt")));
//			
//			String line;
//			while ((line = file.readLine()) != null){
//				try {
//					String[] part = line.split(" ");
//					_gameImages.put(part[4], sheet.getSubimage(Integer.parseInt(part[0]), Integer.parseInt(part[1]), Integer.parseInt(part[2]), Integer.parseInt(part[3])));
//				} catch (NumberFormatException e) {
//					e.printStackTrace();
//				} catch (IndexOutOfBoundsException e){
//					e.printStackTrace();
//				}
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (NullPointerException e){
//			e.printStackTrace();
//		}
		
		String line = "";
		try {
			BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/images/game/gameImages.txt")));
			while ((line = file.readLine()) != null){
				_gameImages.put(line, new ImageIcon(Resource.class.getResource("/media/images/game/"+line+".png")).getImage());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			System.out.println(line);
			e.printStackTrace();
		}
		
		try {
			BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/images/ui/ui.txt")));
			while ((line = file.readLine()) != null){
				_ui.put(line, new ImageIcon(Resource.class.getResource("/media/images/ui/"+line+".png")).getImage());
			}
		} catch (IOException e) {
			System.out.println(line);
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		
	}
}

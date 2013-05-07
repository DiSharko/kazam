package pvpmagic;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import screen.BorderScreen;

public class Resource {

	public static char[] _spellKeys = {'Q','W','E','R','A','S','D','F'};

	public static boolean _useNativeBorder = false;
	public static int _borderHeight;

	public static String _title = "Kazam";


	public static int _fps = 0;

	public static String _buttonFontName = "Times";

	public static ArrayList<String> _characters;
	public static ArrayList<String> _spells;
	public static ArrayList<String> _gameTypes;
	public static ArrayList<String> _maps;

	private static HashMap<String, Image> _images;
	public static Image get(String imageName){
		if (_images.containsKey(imageName)){
			return _images.get(imageName);
		}
		return null;
	}


	public Resource(){
		if (_useNativeBorder) _borderHeight = 0;
		else _borderHeight = BorderScreen._topBarHeight;

		_characters = new ArrayList<String>();
		_spells = new ArrayList<String>();
		_gameTypes = new ArrayList<String>();
		_maps = new ArrayList<String>();

		_images = new HashMap<String, Image>();

		try {
			BufferedReader charactersFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/characters.txt")));
			String line;
			while ((line = charactersFile.readLine()) != null){
				_characters.add(line);
			}

		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		}


		try {
			BufferedReader spellsFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/spells.txt")));
			String line;
			while ((line = spellsFile.readLine()) != null){
				_spells.add(line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		}


		try {
			BufferedReader gameTypesFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/gameTypes.txt")));
			String line;
			while ((line = gameTypesFile.readLine()) != null){
				_gameTypes.add(line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		}


		try {
			BufferedReader mapsFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/maps.txt")));
			String line;
			while ((line = mapsFile.readLine()) != null){
				_maps.add(line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		}




		String line = "";
		try {
			BufferedReader file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/images/index.txt")));
			while ((line = file.readLine()) != null){
				try {

					String[] parts = line.split("/");
					String name = parts[parts.length-1];
					Image image = new ImageIcon(Resource.class.getResource("/media/images/"+line+".png")).getImage();
					_images.put(name, image);
					if (parts[0].equals("spells") && !parts[1].equals("icon")){
						Image icon = get("icon");
						BufferedImage newIcon = new BufferedImage(icon.getWidth(null), icon.getHeight(null), BufferedImage.TYPE_INT_ARGB);
						Graphics g = newIcon.getGraphics();
						g.drawImage(icon, 0, 0, null);
						Vector size = new Vector(image.getWidth(null), image.getHeight(null)).normalize().mult(newIcon.getWidth()*0.7);
						g.drawImage(image, (int) (newIcon.getWidth()/2-size.x/2), (int) (newIcon.getHeight()/2-size.y/2)-15, (int)size.x, (int)size.y, null);
						g.dispose();
						_images.put(name+"Icon", newIcon);
					}
				} catch (NullPointerException e){
					System.out.println("Couldn't find "+line);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

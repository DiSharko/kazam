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


	public static boolean _useNativeBorder = false;
	public static int _borderHeight;

	public static String _title = "PvP Magic, the world's lamest name for a game ever";


	public static int _fps = 0;

	public static String _buttonFontName = "Times";

	public static ArrayList<String> _characters;
	public static ArrayList<String> _spells;
	public static ArrayList<String> _gameTypes;
	public static ArrayList<String> _maps;

	public static HashMap<String, Image> _ui;

	public Resource(){
		if (_useNativeBorder) _borderHeight = 0;
		else _borderHeight = BorderScreen._topBarHeight;

		_characters = new ArrayList<String>();
		_spells = new ArrayList<String>();
		_gameTypes = new ArrayList<String>();
		_maps = new ArrayList<String>();
		
		_ui = new HashMap<String, Image>();

		try {
			BufferedReader charactersFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/characters.txt")));
			String _line;
			while ((_line = charactersFile.readLine()) != null){
				_characters.add(_line);
			}

		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}


		try {
			BufferedReader spellsFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/spells.txt")));
			String _line;
			while ((_line = spellsFile.readLine()) != null){
				_spells.add(_line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}


		try {
			BufferedReader gameTypesFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/gameTypes.txt")));
			String _line;
			while ((_line = gameTypesFile.readLine()) != null){
				_gameTypes.add(_line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}


		try {
			BufferedReader mapsFile = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/data/maps.txt")));
			String _line;
			while ((_line = mapsFile.readLine()) != null){
				_maps.add(_line);
			}
		} catch (IOException e){ e.printStackTrace();
		} catch (NullPointerException e){ e.printStackTrace();
		} catch (IndexOutOfBoundsException e){ e.printStackTrace();
		} catch (NumberFormatException e) { e.printStackTrace();
		}



		try {

			Image _tempSheet = new ImageIcon(Resource.class.getResource("/media/images/ui.png")).getImage();
			BufferedImage _sheet = new BufferedImage(_tempSheet.getWidth(null),_tempSheet.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			Graphics _g = _sheet.getGraphics();
			_g.drawImage(_tempSheet, 0, 0, null);
			_g.dispose();

			BufferedReader _file = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/media/images/ui.txt")));

			String _line;
			while ((_line = _file.readLine()) != null){
				try {
					String[] _part = _line.split(" ");
					_ui.put(_part[4], _sheet.getSubimage(Integer.parseInt(_part[1]), Integer.parseInt(_part[0]), Integer.parseInt(_part[2]), Integer.parseInt(_part[3])));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e){
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
}

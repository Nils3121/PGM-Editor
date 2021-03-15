package prj.test.framework;

import java.awt.*;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.util.List;


public class ClsMain extends JFrame {

	// FRAMEWORK RELATED CONSTANTS AND VARIABLES

	private static int[][] aiGrauWert;
	private static int iSizeY;
	private static int iSizeX;
	private static String strPfad;
    private static String strDateiPfad;
	private static boolean boolIsSaved = false;

	// FRAMEWORK RELATED CONSTANTS AND VARIABLES ------------- END

	// FRAMEWORK RELATED METHODS

	private static void fnSaveToFile(String sFileNamePath) throws IOException {


		File saveFile = new File(sFileNamePath);

		BufferedWriter w = new BufferedWriter(new PrintWriter(saveFile));

		w.write("P2" + System.lineSeparator());
		w.write("# CREATOR: The GIMP's PNM Filter Version 1.0" + System.lineSeparator());

		w.write("400 300" + System.lineSeparator());
		w.write("255" + System.lineSeparator());

		for (int[] line : aiGrauWert) {
			for (int i:line) {
                w.write(i + System.lineSeparator());
            }
		}
		System.out.println("Saved to: " + sFileNamePath);
		boolIsSaved = true;
		w.flush();
		w.close();



	}

	private static void fnChooseFile() throws InvocationTargetException, InterruptedException {

                JFileChooser fileChooser = new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PGM FILES", "pgm");

                /*FileFilter fileFilter = new FileFilter() {
                    public String getDescription() {
                        return "PGM Documents (*.pgm)";
                    }

                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else {
                            return f.getName().toLowerCase().endsWith(".pdf");
                        }
                    }
                };*/

                fileChooser.setFileFilter(filter);
                fileChooser.setDialogTitle("Open File");
                fileChooser.setDialogTitle("Open File");

                int userSelection = fileChooser.showOpenDialog(fileChooser);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToLoad = fileChooser.getSelectedFile();
                    if (fileToLoad.getAbsolutePath().endsWith(".pgm")) {
                        strDateiPfad = fileToLoad.getAbsolutePath();
                    } else {
                        strDateiPfad = "";
                    }
                }
                fileChooser.setVisible(true);
    }
	
	private static void fnLoadFromFile() throws IOException {

	    if (strDateiPfad.equals("")) {
	        System.out.println("Das nächste mal bitte eine PGM Datei auswählen");
	        System.exit(-1);
        }

		File file = new File(strDateiPfad);

		System.out.println(file);


		BufferedReader reader = new BufferedReader(new FileReader(file));
        String[] lines = new String[4];

        for (int i = 0;i<=3;i++) {
            lines[i] = reader.readLine();
        }

        lines = lines[2].split(" ");
        System.out.println(Arrays.toString(lines));


        iSizeX = Integer.parseInt(lines[0]);
        iSizeY = Integer.parseInt(lines[1]);

        aiGrauWert = new int[iSizeY][iSizeX];

        for (int y = 0; y <= iSizeY -1; y++) {
            for (int x = 0; x <= iSizeX -1;x++) {
                aiGrauWert[y][x] = Integer.parseInt(reader.readLine());
            }
        }


		reader.close();

	}

	private static void fnClear() {
		for (int i = 0; i < 25; i++) {
			System.out.println("");
		}
	}

	private static void fnLongline() {
		System.out.println("----");
	}

	private static void fnToImage(int[][] file) throws IOException {
			int bild = 0;
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < file.length; i++) {
				for (int j = 0; j < file[i].length; j++) {
					list.add(file[i][j]);
				}
			}

			int[] vector = new int[list.size()];
			for (int i = 0; i < vector.length; i++) {
				vector[i] = list.get(i);
			}

			//System.out.println(Arrays.toString(vector));
			//for (int i = 0; i < vector.length; i++) {
			//	if (vector[i]  null) {
			//		vector[i] = 0;
			//	}
			//}

	}

	private static void fnImageViewer(int[][] aiGrauWert)  {

					try {
						fnToImage(aiGrauWert);
					} catch (IOException e) {
						e.printStackTrace();
					}
					JFrame frame = new JFrame();
					frame.setTitle("ImageViewer");
					frame.setSize(iSizeX, iSizeY);
					BufferedImage bImage = new BufferedImage(iSizeX, iSizeY , BufferedImage.TYPE_INT_RGB);

						Color myColor; // Color white


						for (int y= 0; y <= iSizeY-1; y++) {
							for (int x= 0; x <= iSizeX-1; x++) {
								myColor = new Color(aiGrauWert[y][x], aiGrauWert[y][x], aiGrauWert[y][x]);
								bImage.setRGB(x, y, myColor.getRGB());
							}
						}

					frame.getContentPane().add(new JLabel(new ImageIcon(bImage)));
					frame.setVisible(true);
	}


	private static void fnHeller(double dHeller) {
		int x;
		int iX;
		dHeller = dHeller * 2.55;

        for (int y=0; y <= iSizeY -1; y++)
		    for (x=0;x <=iSizeX -1;x++) {
                iX = aiGrauWert[y][x];
                iX += dHeller;
                if (iX < 0) {
                    iX = 0;
                }
				if (iX > 255) {
					iX = 255;
				}
                aiGrauWert[y][x] = iX;
            }
	}

	private static void fnInverter() {
		int x;
		int iX = 0;
		for (int y=0; y <= iSizeY -1; y++)
			for (x=0;x <=iSizeX -1;x++) {
				iX = aiGrauWert[y][x];
				iX = -iX + 255;

				aiGrauWert[y][x] = iX;
		}

	}

	private static void fnBlur(float faktor) {

		try {
			fnToImage(aiGrauWert);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage bImage = new BufferedImage(iSizeX, iSizeY , BufferedImage.TYPE_INT_RGB);

		Color myColor; // Color white

		for (int y= 0; y <= iSizeY-1; y++) {
			for (int x= 0; x <= iSizeX-1; x++) {
				myColor = new Color(aiGrauWert[y][x], aiGrauWert[y][x], aiGrauWert[y][x]);
				bImage.setRGB(x, y, myColor.getRGB());

			}
		}

		faktor = 1/faktor;
		float[] data = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,
				0.0625f, 0.125f, 0.0625f };
		float[] data2 = { faktor, faktor, faktor, faktor, faktor, faktor,
				faktor, faktor, faktor };
		Kernel kernel = new Kernel(3, 3, data2);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		BufferedImage bDest = new BufferedImage(iSizeX, iSizeY , BufferedImage.TYPE_INT_RGB);
		convolve.filter(bImage, bDest);

		for (int y=0; y<=iSizeY-1;y++) {
			for (int x=0; x<=iSizeX-1;x++) {
				Color c = new Color(bDest.getRGB(x,y));
				int red = (int) (c.getRed()*0.35);
				int green = (int) (c.getGreen()*0.85);
				int blue = (int) (c.getBlue()*0.20);

				Color newC = new Color(red, green, blue);
				bDest.setRGB(x,y,newC.getRGB());


				//if (bDest.getRGB(x,y) < 0) {
				//	aiGrauWert[x][y] = 0;
				//} else if (bDest.getRGB(x,y) > 255) {
				//	aiGrauWert[x][y] = 255;
				//} else {
				//	aiGrauWert[y][x] = bDest.getRGB(x, y);
				//}

				aiGrauWert[y][x] = (red+green+blue)/3;
			}
		}


			//File input = new File(“location of your RGB image”);
			//image = ImageIO.read(input);
			//width = image.getWidth();
			//height = image.getHeight();
			//for(int i=0; i<height; i++) {
			//	for(int j=0; j<width; j++) {
			//		Color c = new Color(image.getRGB(j, i));
			//		int red = (int)(c.getRed() * 0.299);
			//		int green = (int)(c.getGreen() * 0.587);
		//			int blue = (int)(c.getBlue() *0.114);
		//			Color newColor = new Color(red+green+blue, red+green+blue,red+green+blue);
	//				image.setRGB(j,i,newColor.getRGB());
	//			}
	//		}
	//		File ouptut = new File(“Where to be placed and its name”);
//			System.out.println(“Done”);
//			ImageIO.write(image, “jpg”, ouptut);

	}


	private static void fnSaveFileDialog() throws InvocationTargetException, InterruptedException {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save File");

				int userSelection = fileChooser.showOpenDialog(fileChooser);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					strPfad = fileToSave.getAbsolutePath();
				}
				fileChooser.setVisible(true);

	}

	private static void fnKontrast(int[][] file) {

		int iX;
		for (int y = 0; y <= iSizeY - 1; y++)
			for (int x = 0; x <= iSizeX - 1; x++) {
				iX = aiGrauWert[y][x];

				if (iX < 85) {
					iX -= 10;
				}
				if (iX > 110) {
					iX += 10;
				}
				if (iX < 0) {
					iX = 0;
				}
				if (iX > 255) {
					iX = 255;
				}
				aiGrauWert[y][x] = iX;
			}
	}



	// FRAMEWORK RELATED METHODS ------------- END

	public static void main(String[] args) throws Exception {
		// TODO ENTER YOUR CODE BELOW
		 Scanner sc = new Scanner(System.in);
		 int iInput1 = 0;
		 double iHelligkeit;
		 int iRuntime = 1;
		 int iRuntime2 = 1;
		 String strAntwort = "";
		 String strDateiPfad = null;
		 String strAbbrechenSicher;
		 String strTemp;
		 String strAbbrechenSicherSicher;
		 int faktor;
		 System.out.println("Datei wählen");
		 while (iRuntime2 == 1){
			 try {
				 fnChooseFile();
				 fnLoadFromFile();
				 iRuntime2 = 0;
			 } catch (NullPointerException e) {
				 System.out.println("Bitte eine Datei auswählen");
			 }
		 }

		 while (iRuntime == 1) {

			 System.out.println("Was wollen sie machen? Heller(1), Invertieren(2), Blur(3), Speichern(4), Anzeigen lassen(5), Abbrechen(6), Kontrast erhöhen(7)");
			 try {
				 iInput1 = sc.nextInt();
			 } catch (InputMismatchException e) {
			 	sc.next();
			 }
			 switch (iInput1) {
				 case 1: {
					 System.out.println("Wieviel heller? (Prozent): ");
					 strTemp = sc.next().replace("%","");
                     iHelligkeit = Double.parseDouble(strTemp);

					 fnHeller(iHelligkeit);
					 System.out.println("Fertig? (y/n)");
					 strAntwort = sc.next();
					 if (strAntwort.equals("y")) {
					 	if (!boolIsSaved) {
							 System.out.println("Datei ist nicht gespeichert. Jetzt speichern? (y/n)");
							 strAbbrechenSicherSicher = sc.next();
							 if (strAbbrechenSicherSicher.equals("y")) {
								fnSaveFileDialog();
								 try {
									 fnSaveToFile(strPfad);
								 } catch (NullPointerException e) {
									 System.out.println("Dann halt nicht");
									 break;
								 }
							 	System.exit(0);
							 }
							 else {
								 break;
							 }
						 } else {
							 System.exit(0);
						 }
					 }
					 break;
				 }
				 case 2: {
					 fnInverter();
					 System.out.println("Fertig? (y/n)");
					 strAntwort = sc.next();
					 if (strAntwort.equals("y")) {
						if (!boolIsSaved) {
							System.out.println("Datei ist nicht gespeichert. Jetzt speichern? (y/n)");
							strAbbrechenSicherSicher = sc.next();
							if (strAbbrechenSicherSicher.equals("y")) {
								fnSaveFileDialog();
								try {
									fnSaveToFile(strPfad);
								} catch (NullPointerException e) {
									System.out.println("Dann halt nicht");
									break;
								}
								System.exit(0);
							}
							else {
								break;
							}
						} else {
							System.exit(0);
						}
					 }
					 break;
				 }
				 case 3: {
				 	 System.out.println("Um welchen Faktor? (Ganzzahl): ");
				 	 faktor = sc.nextInt();
					 fnBlur(faktor);
					 System.out.println("Fertig? (y/n)");
					 strAntwort = sc.next();
					 if (strAntwort.equals("y")) {
						 if (!boolIsSaved) {
							 System.out.println("Datei ist nicht gespeichert. Jetzt speichern? (y/n)");
							 strAbbrechenSicherSicher = sc.next();
							 if (strAbbrechenSicherSicher.equals("y")) {
								 fnSaveFileDialog();
								 try {
									 fnSaveToFile(strPfad);
								 } catch (NullPointerException e) {
									 System.out.println("Dann halt nicht");
									 break;
								 }
								 System.exit(0);
							 }
							 else {
								 break;
							 }
						 } else {
							 System.exit(0);
						 }
					 }
					 break;
				 }
				 case 4: {
					 fnSaveFileDialog();
					 try {
						 fnSaveToFile(strPfad);
						 break;
					 } catch (NullPointerException e) {
						 System.out.println("Dann halt nicht");
						 break;
					 }
				 }
				 case 5: {
				 	fnImageViewer(aiGrauWert);
				 	break;
				 }
				 case 6: {
				 	System.out.println("Sicher? (y/n)");
						strAbbrechenSicher = sc.next();
						if (strAbbrechenSicher.equals("y")) {
							iRuntime = 0;
							break;
						}
					 break;

				 }
				 case 7: {
					 fnKontrast(aiGrauWert);
					 break;
				 }
				 default: {
					 System.out.println("Bitte eine valide Ganzzahl angeben");
				 }
			 }
		 }




		// TODO ENTER YOUR CODE ABOVE ------------- END

	}

}
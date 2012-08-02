package main;

import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.effects.Decorator;
import org.apache.pivot.wtk.media.Image;
import org.apache.pivot.beans.*;
import org.apache.pivot.collections.*;
import org.apache.pivot.util.*;
import org.apache.pivot.io.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewerWindow extends Window implements Bindable{
	@BXML PushButton btnOpenFile;
	@BXML ImageView imageView;
	@BXML PushButton btnPaste;
	@BXML PushButton btnCopy;
	@BXML TextArea textEditor;
	@BXML ScrollPane textFrame;
	
	@Override
	public void initialize( Map<String,Object> namespace, URL url, Resources resources)	{
		
		// Handling of file opening
		btnOpenFile.getButtonPressListeners().add(new ButtonPressListener()	{
			public void buttonPressed(Button button)	{
				btnOpenFile.setButtonData("Open different File");
				final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();	// final because of sheetClosed-Methode
				fileBrowserSheet.open(ViewerWindow.this, new SheetCloseListener()	{
					@Override
					public void sheetClosed(Sheet sheet)	{
						if(sheet.getResult())	{
							File file = fileBrowserSheet.getSelectedFile();
							try {
								openFile(file.toURI().toURL());
							} catch (MalformedURLException e) {
								Prompt.prompt("File couln't be opened", ViewerWindow.this);
							}	
						}
					}
				});
			}
		});
		
		// Handling of pasting
		btnPaste.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button)	{
				Manifest clipboardContent = Clipboard.getContent();
				if (clipboardContent != null)	{
					try {
						if (clipboardContent.containsImage())	{
							Image img = clipboardContent.getImage();
							imageView.setImage(img);
							imageMode();
						}
						else if (clipboardContent.containsText())	{
							String text = clipboardContent.getText();
							textEditor.setText(text);
							textMode();
						}
						else if (clipboardContent.containsFileList())	{
							FileList fileList = clipboardContent.getFileList();
							if (fileList.getLength() == 1)	{
								openFile(fileList.get(0).toURI().toURL());
							}
							else	{
								Prompt.prompt("You can only paste in a single file", ViewerWindow.this);
							}
								
						}
						else {
							Prompt.prompt("Clipboard content has wrong format", ViewerWindow.this);
						}
					} catch (IOException e) {
						System.out.println("Error: File couln't be pasted in");
					}
				}
				else	{	// never reached because in Windows Clipboard is never empty
					Prompt.prompt("Clipboard empty", ViewerWindow.this);	
				}
			}	
		});
		
		// Handling of copying
		btnCopy.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button)	{
				LocalManifest content = new LocalManifest();
				if (!textEditor.isVisible())	{
					Image img = imageView.getImage();
					if (img != null)	{
					content.putImage(imageView.getImage());
					}
					else	{
						Prompt.prompt("Nothing there to copy", ViewerWindow.this);
					}
				}
				else if (!imageView.isVisible())	{
					content.putText(textEditor.getText());
				}
				Clipboard.setContent(content);
				DesktopApplicationContext.setFullScreen(!DesktopApplicationContext.isFullScreen());
				
			}
		});
	}
	
	// opens the content of the file and displays it on screen if it has the correct format
	private void openFile (URL path)	{
		try {
			if (path.toString().matches(".*(txt|cfg|cpp)"))	{	// checks if file is a text-file
				System.out.println("text-file loaded");
				textEditor.setText(path);
				textMode();
			}
			else if(path.toString().matches(".*(svg|jpg|png|bmp)"))	{
				System.out.println("Image loaded");
				imageView.setImage(path);
				imageMode();
			}
		} catch (IOException e) {
		Prompt.prompt("Error: File couln't be loaded", ViewerWindow.this);
	 	}
	}
	private void textMode()	{
		imageView.setVisible(false);
		textFrame.setVisible(true);
		textEditor.setVisible(true);
	}
	private void imageMode()	{
		textFrame.setVisible(false);
		textEditor.setVisible(false);
		imageView.setVisible(true);
	}
}

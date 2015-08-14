package jp.swest.ledcamp.sakuratools.actions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class WriteAction implements IPluginActionDelegate{
	private static final String BINARY_FILE = "sketch.bin";

	@Override
	public Object run(IWindow window) throws UnExpectedException {
		Path sakuraDrive = Paths.get(Settings.getInstance().getE2makePath());
		Path binaryFile = Paths.get(SettingManager.getInstance().getCurrentSetting().getTargetPath()).getParent().resolve(BINARY_FILE);
		if(Files.notExists(binaryFile)){
			JOptionPane.showMessageDialog(window.getParent(), BINARY_FILE+" not found in :"+binaryFile.getParent()+". Please rebuild.");
			return null;
		}
		if(Files.notExists(sakuraDrive, LinkOption.NOFOLLOW_LINKS)){
			JOptionPane.showMessageDialog(window.getParent(), "Sakura not found. Please connect Sakura and push reset.");
			return null;
		}
		try {
			Files.copy(binaryFile, sakuraDrive.resolve(BINARY_FILE), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(window.getParent(), "IOException.");
			return null;			
		}
		JOptionPane.showMessageDialog(window.getParent(), "Write complite.");
		return null;
	}
}

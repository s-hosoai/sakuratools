package jp.swest.ledcamp.sakuratools.actions;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import jp.swest.ledcamp.setting.SettingManager;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class BuildAction implements IPluginActionDelegate {
	@Override
	public Object run(IWindow win) throws UnExpectedException {
		Builder builder = new Builder(win.getParent(), null, null);
		builder.execute();
		return null;
	}

	class Builder extends SwingWorker<Integer, Void> {
		private Window window;
		private File projectDir;
		private String command = Settings.getInstance().getE2makePath();

		public Builder(Window window, Path projectPath, String command) {
			this.window = window;
			projectDir = Paths
					.get(SettingManager.getInstance().getCurrentSetting()
							.getTargetPath()).getParent()
					.resolve("release_sakura").toFile();
		}

		@Override
		protected Integer doInBackground() throws Exception {
			ProcessBuilder processBuilder = new ProcessBuilder(command, "all");
			processBuilder.directory(projectDir);
			processBuilder.redirectErrorStream(true);
			System.out.println("building..");
			Process process = processBuilder.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while (process.isAlive()) {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
			br.close();
			return process.exitValue();
		}

		@Override
		protected void done() {
			try {
				if (get() == 0) {
					JOptionPane.showMessageDialog(window, "Build Finish");
				} else {
					JOptionPane.showMessageDialog(window, "Build Fail : "
							+ get());
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(window, "Execution Exception."
						+ e.getMessage());
			}
		}
	}
}

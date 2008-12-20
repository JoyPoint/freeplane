package plugins.script;

import javax.swing.JDialog;

import org.freeplane.controller.resources.WindowConfigurationStorage;
import org.freeplane.io.xml.n3.nanoxml.IXMLElement;

public class ScriptEditorWindowConfigurationStorage extends WindowConfigurationStorage {
	public static ScriptEditorWindowConfigurationStorage decorateDialog(final String marshalled,
	                                                                    final JDialog dialog) {
		final ScriptEditorWindowConfigurationStorage storage = new ScriptEditorWindowConfigurationStorage();
		final IXMLElement xml = storage.unmarschall(marshalled, dialog);
		if (xml != null) {
			storage.leftRatio = Integer.parseInt(xml.getAttribute("left_ratio", null));
			storage.topRatio = Integer.parseInt(xml.getAttribute("top_ratio", null));
			return storage;
		}
		return null;
	}

	protected int leftRatio;
	protected int topRatio;

	public int getLeftRatio() {
		return leftRatio;
	}

	public int getTopRatio() {
		return topRatio;
	}

	@Override
	protected void marschallSpecificElements(final IXMLElement xml) {
		xml.setName("manage_style_editor_window_configuration_storage");
		xml.setAttribute("left_ratio", Integer.toString(leftRatio));
		xml.setAttribute("top_ratio", Integer.toString(topRatio));
	}

	public void setLeftRatio(final int leftRatio) {
		this.leftRatio = leftRatio;
	}

	public void setTopRatio(final int topRatio) {
		this.topRatio = topRatio;
	}
}

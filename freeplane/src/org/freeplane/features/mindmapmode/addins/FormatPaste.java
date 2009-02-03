/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.mindmapmode.addins;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.model.NodeModel;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.ActionDescriptor;
import org.freeplane.core.ui.FreeplaneAction;
import org.freeplane.core.ui.MenuBuilder;
import org.freeplane.core.ui.MultipleNodeAction;
import org.freeplane.features.mindmapnode.pattern.MPatternController;
import org.freeplane.features.mindmapnode.pattern.Pattern;
import org.freeplane.features.mindmapnode.pattern.StylePatternFactory;

/**
 * @author foltin
 */
@ActionDescriptor(tooltip = "accessories/plugins/FormatCopy.properties_documentation", //
name = "accessories/plugins/FormatCopy.properties_name", //,
iconPath = "/images/colorpicker.png", //
keyStroke = "keystroke_accessories/plugins/FormatCopy.properties.properties_key", //
locations = { "/menu_bar/edit/paste" })
class FormatCopy extends FreeplaneAction {
	private static Pattern pattern = null;

	public static Pattern getPattern() {
		return pattern;
	}

	public FormatCopy(final Controller controller) {
		super(controller);
	}

	public void actionPerformed(final ActionEvent e) {
		copyFormat(getModeController().getMapController().getSelectedNode());
	}

	/**
	 */
	private void copyFormat(final NodeModel node) {
		FormatCopy.pattern = StylePatternFactory.createPatternFromNode(node);
	}
}

/**
 * @author foltin
 */
@ActionDescriptor(tooltip = "accessories/plugins/FormatPaste.properties_documentation", //
name = "accessories/plugins/FormatPaste.properties_name", //
keyStroke = "keystroke_accessories/plugins/FormatPaste.properties.properties_key", //
iconPath = "/images/color_fill.png", //
locations = { "/menu_bar/edit/paste" })
public class FormatPaste extends MultipleNodeAction {
	public FormatPaste(final Controller controller, final MenuBuilder menuBuilder) {
		super(controller);
		menuBuilder.addAnnotatedAction(new FormatCopy(controller));
		menuBuilder.addAnnotatedAction(this);
	}

	@Override
	protected void actionPerformed(final ActionEvent e, final NodeModel node) {
		pasteFormat(node);
	}

	/**
	 */
	private void pasteFormat(final NodeModel node) {
		final Pattern pattern = FormatCopy.getPattern();
		if (pattern == null) {
			JOptionPane.showMessageDialog(getController().getViewController().getContentPane(), ResourceController
			    .getText("no_format_copy_before_format_paste"), "" /*=Title*/, JOptionPane.ERROR_MESSAGE);
			return;
		}
		MPatternController.getController((getModeController())).applyPattern(node, pattern);
	}
}

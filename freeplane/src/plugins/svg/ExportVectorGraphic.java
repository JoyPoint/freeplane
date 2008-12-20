/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file author is Christian Foltin
 *  It is modified by Dimitry Polivaev in 2008.
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
package plugins.svg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGeneratorContext.GraphicContextDefaults;
import org.apache.batik.util.SVGConstants;
import org.freeplane.addins.mindmapmode.export.ExportAction;
import org.freeplane.controller.Controller;
import org.freeplane.map.tree.view.MapView;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * @author foltin
 */
abstract class ExportVectorGraphic extends ExportAction {
	public ExportVectorGraphic() {
		super();
	}

	/**
	 */
	protected SVGGraphics2D fillSVGGraphics2D(final MapView view) {
		final DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
		final String namespaceURI = SVGConstants.SVG_NAMESPACE_URI;
		final Document domFactory = impl.createDocument(namespaceURI, "svg", null);
		final SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(domFactory);
		ctx.setEmbeddedFontsOn(true);
		final GraphicContextDefaults defaults = new GraphicContextDefaults();
		defaults.setFont(new Font("Arial", Font.PLAIN, 12));
		ctx.setGraphicContextDefaults(defaults);
		ctx.setPrecision(12);
		final SVGGraphics2D g2d = new SVGGraphics2D(ctx, false);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		    RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		view.preparePrinting();
		final Rectangle innerBounds = view.getInnerBounds();
		g2d.setSVGCanvasSize(new Dimension(innerBounds.width, innerBounds.height));
		g2d.translate(-innerBounds.x, -innerBounds.y);
		view.print(g2d);
		return g2d;
	}

	public void transForm(final Source xmlSource, final InputStream xsltStream,
	                      final File resultFile, final String areaCode) {
		final Source xsltSource = new StreamSource(xsltStream);
		final Result result = new StreamResult(resultFile);
		try {
			final TransformerFactory transFact = TransformerFactory.newInstance();
			final Transformer trans = transFact.newTransformer(xsltSource);
			trans.setParameter("destination_dir", resultFile.getName() + "_files/");
			trans.setParameter("area_code", areaCode);
			trans.setParameter("folding_type", Controller.getResourceController().getProperty(
			    "html_export_folding"));
			trans.transform(xmlSource, result);
		}
		catch (final Exception e) {
			org.freeplane.main.Tools.logException(e);
		};
		return;
	}
}

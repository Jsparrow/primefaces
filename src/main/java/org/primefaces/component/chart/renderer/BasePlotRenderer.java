/**
 * The MIT License
 *
 * Copyright (c) 2009-2019 PrimeTek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.component.chart.renderer;

import org.primefaces.component.chart.Chart;
import org.primefaces.model.chart.ChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.util.EscapeUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public abstract class BasePlotRenderer {

    public void render(FacesContext context, Chart chart) throws IOException {
        encodeData(context, chart);
        encodeOptions(context, chart);
    }

    protected abstract void encodeData(FacesContext context, Chart chart) throws IOException;

    protected void encodeOptions(FacesContext context, Chart chart) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ChartModel model = chart.getModel();
        String legendPosition = model.getLegendPosition();
        String title = model.getTitle();
        String seriesColors = model.getSeriesColors();
        String negativeSeriesColors = model.getNegativeSeriesColors();
        String extender = model.getExtender();

        if (title != null) {
            writer.write(new StringBuilder().append(",title:\"").append(EscapeUtils.forJavaScript(title)).append("\"").toString());
        }

        if (!model.isShadow()) {
            writer.write(",shadow:false");
        }

        if (seriesColors != null) {
            writer.write(new StringBuilder().append(",seriesColors:[\"#").append(seriesColors.replaceAll("[ ]*,[ ]*", "\",\"#")).append("\"]").toString());
        }

        if (negativeSeriesColors != null) {
            writer.write(new StringBuilder().append(",negativeSeriesColors:[\"#").append(negativeSeriesColors.replaceAll("[ ]*,[ ]*", "\",\"#")).append("\"]").toString());
        }

        if (legendPosition != null) {
            LegendPlacement legendPlacement = model.getLegendPlacement();
            writer.write(new StringBuilder().append(",legendPosition:\"").append(legendPosition).append("\"").toString());

            if (model.getLegendCols() != 0) {
                writer.write(",legendCols:" + model.getLegendCols());
            }

            if (model.getLegendRows() != 0) {
                writer.write(",legendRows:" + model.getLegendRows());
            }

            if (legendPlacement != null) {
                writer.write(new StringBuilder().append(",legendPlacement:\"").append(legendPlacement).append("\"").toString());
            }

            if (model.isLegendEscapeHtml()) {
                writer.write(",escapeHtml:true");
            }
        }

        if (!model.isMouseoverHighlight()) {
            writer.write(",highlightMouseOver:" + false);
        }

        if (extender != null) {
            writer.write(",extender:" + extender);
        }

        if (!model.isResetAxesOnResize()) {
            writer.write(",resetAxesOnResize:" + false);
        }

        writer.write(new StringBuilder().append(",dataRenderMode:\"").append(model.getDataRenderMode()).append("\"").toString());
    }

    protected String escapeChartData(Object value) {
        // default to "null" if null
        String result = String.valueOf(value);

        // do NOT quote numbers but quote all other objects
        if (!(value instanceof Number)) {
            result = new StringBuilder().append("\"").append(EscapeUtils.forJavaScript(result)).append("\"").toString();
        }

        return result;
    }
}

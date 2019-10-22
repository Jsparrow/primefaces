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
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.util.EscapeUtils;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BarRenderer extends CartesianPlotRenderer {

    @Override
    protected void encodeData(FacesContext context, Chart chart) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        BarChartModel model = (BarChartModel) chart.getModel();
        boolean horizontal = "horizontal".equals(model.getOrientation());

        //data
        writer.write(",data:[");
        for (Iterator<ChartSeries> it = model.getSeries().iterator(); it.hasNext(); ) {
            ChartSeries series = it.next();
            int i = 1;

            writer.write("[");
            for (Iterator<Map.Entry<Object, Number>> its = series.getData().entrySet().iterator(); its.hasNext(); ) {
                Map.Entry<Object, Number> entry = its.next();
                Number value = entry.getValue();
                String valueToRender = value != null ? value.toString() : "null";

                if (horizontal) {
                    writer.write("[");
                    writer.write(new StringBuilder().append(valueToRender).append(",").append(i).toString());
                    writer.write("]");

                    i++;
                }
                else {
                    if ("key".equals(model.getDataRenderMode())) {
                        writer.write(new StringBuilder().append("'").append((String) entry.getKey()).append("',").append(valueToRender).toString());
                    }
                    else {
                        writer.write(valueToRender);
                    }
                }

                if (its.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");

            if (it.hasNext()) {
                writer.write(",");
            }
        }
        writer.write("]");
    }

    @Override
    protected void encodeOptions(FacesContext context, Chart chart) throws IOException {
        super.encodeOptions(context, chart);

        ResponseWriter writer = context.getResponseWriter();
        BarChartModel model = (BarChartModel) chart.getModel();
        String orientation = model.getOrientation();
        int barPadding = model.getBarPadding();
        int barMargin = model.getBarMargin();
        int barWidth = model.getBarWidth();
        List<String> ticks = model.getTicks();
        String legendLabel = model.getLegendLabel();

        writer.write(",series:[");
        if ("key".equals(model.getDataRenderMode()) && legendLabel != null) {
            writer.write("{");
            writer.write(new StringBuilder().append("label:\"").append(EscapeUtils.forJavaScript(legendLabel)).append("\"").toString());
            writer.write("}");
        }
        else {
            for (Iterator<ChartSeries> it = model.getSeries().iterator(); it.hasNext(); ) {
                ChartSeries series = it.next();
                series.encode(writer);

                if (it.hasNext()) {
                    writer.write(",");
                }
            }
        }
        writer.write("]");

        writer.write(",ticks:[");
        for (Iterator<String> tickIt = ticks.iterator(); tickIt.hasNext();) {
            writer.write(new StringBuilder().append("\"").append(EscapeUtils.forJavaScript(tickIt.next())).append("\"").toString());
            if (tickIt.hasNext()) {
                writer.write(",");
            }
        }
        writer.write("]");

        if (orientation != null) {
            writer.write(new StringBuilder().append(",orientation:\"").append(orientation).append("\"").toString());
        }
        if (barPadding != 8) {
            writer.write(",barPadding:" + barPadding);
        }
        if (barMargin != 10) {
            writer.write(",barMargin:" + barMargin);
        }
        if (barWidth != 0) {
            writer.write(",barWidth:" + barWidth);
        }
        if (model.isStacked()) {
            writer.write(",stackSeries:true");
        }
        if (model.isZoom()) {
            writer.write(",zoom:true");
        }
        if (model.isAnimate()) {
            writer.write(",animate:true");
        }
        if (model.isShowPointLabels()) {
            writer.write(",showPointLabels:true");
        }

        if (!model.isShowDatatip()) {
			return;
		}
		writer.write(",datatip:true");
		if (model.getDatatipFormat() != null) {
		    writer.write(new StringBuilder().append(",datatipFormat:\"").append(model.getDatatipFormat()).append("\"").toString());
		}
    }
}

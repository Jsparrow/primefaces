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
package org.primefaces.component.datalist;

import java.io.IOException;
import javax.faces.FacesException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.DataRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class DataListRenderer extends DataRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        DataList list = (DataList) component;

        if (list.isPaginationRequest(context)) {
            list.updatePaginationData(context, list);

            if (list.isLazy()) {
                list.loadLazyData();
            }

            if ("none".equals(list.getType())) {
                encodeFreeList(context, list);
            }
            else {
                encodeStrictList(context, list);
            }

            if (list.isMultiViewState()) {
                DataListState ls = list.getDataListState(true);
                ls.setFirst(list.getFirst());
                ls.setRows(list.getRows());
            }
        }
        else {
            if (list.isMultiViewState() && list.isPaginator()) {
                int firstOld = list.getFirst();
                int rowsOld = list.getRows();

                list.restoreDataListState();

                if (list.isLazy() && (firstOld != list.getFirst() || rowsOld != list.getRows())) {
                    list.loadLazyData();
                }
            }

            encodeMarkup(context, list);
            encodeScript(context, list);
        }
    }

    protected void encodeMarkup(FacesContext context, DataList list) throws IOException {
        if (list.isLazy()) {
            list.loadLazyData();
        }

        ResponseWriter writer = context.getResponseWriter();
        String clientId = list.getClientId(context);
        boolean hasPaginator = list.isPaginator();
        boolean empty = (list.getRowCount() == 0);
        String paginatorPosition = list.getPaginatorPosition();
        String styleClass = list.getStyleClass() == null ? DataList.DATALIST_CLASS : new StringBuilder().append(DataList.DATALIST_CLASS).append(" ").append(list.getStyleClass()).toString();
        String style = list.getStyle();

        if (hasPaginator) {
            list.calculateFirst();
        }

        writer.startElement("div", list);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeFacet(context, list, "header", DataList.HEADER_CLASS);

        if (hasPaginator && !"bottom".equalsIgnoreCase(paginatorPosition)) {
            encodePaginatorMarkup(context, list, "top");
        }

        writer.startElement("div", list);
        writer.writeAttribute("id", clientId + "_content", "id");
        writer.writeAttribute("class", DataList.CONTENT_CLASS, "styleClass");

        if (empty) {
            writer.startElement("div", list);
            writer.writeAttribute("class", DataList.DATALIST_EMPTY_MESSAGE_CLASS, null);
            writer.writeText(list.getEmptyMessage(), "emptyMessage");
            writer.endElement("div");
        }
        else {
            if ("none".equals(list.getType())) {
                encodeFreeList(context, list);
            }
            else {
                encodeStrictList(context, list);
            }
        }

        writer.endElement("div");

        if (hasPaginator && !"top".equalsIgnoreCase(paginatorPosition)) {
            encodePaginatorMarkup(context, list, "bottom");
        }

        encodeFacet(context, list, "footer", DataList.FOOTER_CLASS);

        writer.endElement("div");
    }

    protected void encodeScript(FacesContext context, DataList list) throws IOException {
        String clientId = list.getClientId(context);
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("DataList", list.resolveWidgetVar(context), clientId);

        if (list.isPaginator()) {
            encodePaginatorConfig(context, list, wb);
        }

        encodeClientBehaviors(context, list);

        wb.finish();
    }

    /**
     * Renders items with no strict markup
     *
     * @param context FacesContext instance
     * @param list    DataList component
     * @throws IOException
     */
    protected void encodeStrictList(FacesContext context, DataList list) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = list.getClientId(context);
        boolean isDefinition = list.isDefinition();
        UIComponent definitionFacet = list.getFacet("description");
        boolean renderDefinition = isDefinition && ComponentUtils.shouldRenderFacet(definitionFacet);
        String itemType = list.getItemType();
        String listClass = DataList.LIST_CLASS;
        if (itemType != null && "none".equals(itemType)) {
            listClass = new StringBuilder().append(listClass).append(" ").append(DataList.NO_BULLETS_CLASS).toString();
        }

        String listTag = list.getListTag();
        String listItemTag = isDefinition ? "dt" : "li";

        writer.startElement(listTag, null);
        writer.writeAttribute("id", clientId + "_list", null);
        writer.writeAttribute("class", listClass, null);
        if (list.getItemType() != null) {
            writer.writeAttribute("type", list.getItemType(), null);
        }

        list.visitRows((status) -> {
            try {
                String itemStyleClass = list.getItemStyleClass();
                itemStyleClass = (itemStyleClass == null) ? DataList.LIST_ITEM_CLASS : new StringBuilder().append(DataList.LIST_ITEM_CLASS).append(" ").append(itemStyleClass).toString();

                writer.startElement(listItemTag, null);
                writer.writeAttribute("class", itemStyleClass, null);
                renderChildren(context, list);
                writer.endElement(listItemTag);

                if (renderDefinition) {
                    writer.startElement("dd", null);
                    definitionFacet.encodeAll(context);
                    writer.endElement("dd");
                }
            }
            catch (IOException e) {
                throw new FacesException(e);
            }
        });

        writer.endElement(listTag);
    }

    /**
     * Renders items with no strict markup
     *
     * @param context FacesContext instance
     * @param list    DataList component
     * @throws IOException
     */
    protected void encodeFreeList(FacesContext context, DataList list) throws IOException {
        list.visitRows((status) -> {
            try {
                renderChildren(context, list);
            }
            catch (IOException e) {
                throw new FacesException(e);
            }
        });
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Do Nothing
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}

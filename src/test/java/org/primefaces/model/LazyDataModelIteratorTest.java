/* 
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
package org.primefaces.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazyDataModelIteratorTest {

    private static final Logger logger = LoggerFactory.getLogger(LazyDataModelIteratorTest.class);

	@Test
    public void testIteratorPageSizeLessThanTotalItems() {
        logger.info("\ntestIteratorPageSizeLessThanTotalItems");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(3);
        dataModel.totalItems = 10;
        List<Integer> items = new ArrayList<>();
        for (Integer item : dataModel) {
            logger.info(String.valueOf(item));
            items.add(item);
        }
        Assertions.assertEquals(10, items.size());
        Assertions.assertEquals(4, dataModel.getLoadCount());
    }

    @Test
    public void testIteratorPageSizeEqualToTotalItems() {
        logger.info("\ntestIteratorPageSizeEqualToTotalItems");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(20);
        dataModel.totalItems = 20;
        List<Integer> items = new ArrayList<>();
        for (Integer item : dataModel) {
            logger.info(String.valueOf(item));
            items.add(item);
        }
        Assertions.assertEquals(20, items.size());
        Assertions.assertEquals(2, dataModel.getLoadCount());
    }

    @Test
    public void testIteratorPageSizeGreaterThanTotalItems() {
        logger.info("\ntestIteratorPageSizeGreaterThanTotalItems");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(10);
        dataModel.totalItems = 9;
        List<Integer> items = new ArrayList<>();
        for (Integer item : dataModel) {
            logger.info(String.valueOf(item));
            items.add(item);
        }
        Assertions.assertEquals(9, items.size());
        Assertions.assertEquals(1, dataModel.getLoadCount());
    }

    @Test
    public void testIteratorZeroTotalItems() {
        logger.info("\ntestIteratorZeroTotalItems");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(10);
        dataModel.totalItems = 0;
        List<Integer> items = new ArrayList<>();
        for (Integer item : dataModel) {
            logger.info(String.valueOf(item));
            items.add(item);
        }
        Assertions.assertEquals(0, items.size());
        Assertions.assertEquals(1, dataModel.getLoadCount());
    }

    @Test
    public void testIteratorWhileHasNext() {
        logger.info("\ntestIteratorWhileHasNext");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(10);
        dataModel.totalItems = 5;
        List<Integer> items = new ArrayList<>();
        for (Integer item : dataModel) {
            logger.info(String.valueOf(item));
            items.add(item);
        }
        Assertions.assertEquals(5, items.size());
        Assertions.assertEquals(1, dataModel.getLoadCount());
    }

    @Test
    public void testIteratorWhileNoSuchElementException() {
        logger.info("\ntestIteratorWhileNoSuchElementException");
        
        // Act
        NoSuchElementException thrown = Assertions.assertThrows(NoSuchElementException.class, () -> {
            LazyDataModelImpl dataModel = new LazyDataModelImpl();
            dataModel.setPageSize(2);
            dataModel.totalItems = 2;
            dataModel.iterator().next();
        });
        

        // Assert (expected exception)
        assertNull(thrown.getMessage());
    }

    @Test
    public void testIteratorWhileRemoveUnsupportedOperationException() {
        logger.info("\ntestIteratorWhileRemoveUnsupportedOperationException");
        
        UnsupportedOperationException thrown = Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            LazyDataModelImpl dataModel = new LazyDataModelImpl();
            dataModel.setPageSize(2);
            dataModel.totalItems = 2;
            Iterator<Integer> it = dataModel.iterator();
            while (it.hasNext()) {
                it.remove();
            }
        });
       
        // Assert (expected exception)
        assertNull(thrown.getMessage());
    }

    @Test
    public void testOverloadedIteratorSingleSorting() {
        logger.info("\ntestOverloadedIteratorSingleSorting");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(2);
        dataModel.totalItems = 1;
        Iterator<Integer> it = dataModel.iterator("foo", SortOrder.ASCENDING, Collections.singletonMap("foo", (Object) "bar"));
        while (it.hasNext()) {
            Integer item = it.next();
            Assertions.assertNotNull(item);
        }
        Assertions.assertEquals(1, dataModel.singleSortingLoadCounter);
    }

    @Test
    public void testOverloadedIteratorMultiSorting() {
        logger.info("\ntestOverloadedIteratorMultiSorting");
        LazyDataModelImpl dataModel = new LazyDataModelImpl();
        dataModel.setPageSize(2);
        dataModel.totalItems = 1;
        Iterator<Integer> it = dataModel.iterator(Collections.singletonList(new SortMeta()), Collections.singletonMap("foo", (Object) "bar"));
        while (it.hasNext()) {
            Integer item = it.next();
            Assertions.assertNotNull(item);
        }
        Assertions.assertEquals(1, dataModel.multiSortingLoadCounter);
    }

    private static class LazyDataModelImpl extends LazyDataModel<Integer> {

        private static final long serialVersionUID = 1L;

		private final Logger logger1 = LoggerFactory.getLogger(LazyDataModelImpl.class);

		int totalItems;

		int multiSortingLoadCounter;

		int singleSortingLoadCounter;

		int getLoadCount() {
            return multiSortingLoadCounter + singleSortingLoadCounter;
        }

		@Override
        public List<Integer> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
            logger1.info(String.format("Loading %d items from offset %d", pageSize, first));
            multiSortingLoadCounter++;
            List<Integer> page = new ArrayList<>();
            for(int i = first; i < first + pageSize && i < totalItems; i++) {
                page.add(i);
            }
            return page;
        }

		@Override
        public List<Integer> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            logger1.info(String.format("Loading %d items from offset %d", pageSize, first));
            singleSortingLoadCounter++;
            List<Integer> page = new ArrayList<>();
            for(int i = first; i < first + pageSize && i < totalItems; i++) {
                page.add(i);
            }
            return page;
        }
    }

}
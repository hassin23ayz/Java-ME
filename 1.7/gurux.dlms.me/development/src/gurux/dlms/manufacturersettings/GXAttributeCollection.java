//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.manufacturersettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GXAttributeCollection
        implements java.util.List<GXDLMSAttributeSettings> {
    private List<GXDLMSAttributeSettings> base =
            new ArrayList<GXDLMSAttributeSettings>();

    private Object parent;

    /**
     * @return Parent object.
     */
    public final Object getParent() {
        return parent;
    }

    public final void setParent(final Object value) {
        parent = value;
    }

    public final GXDLMSAttributeSettings find(final int index) {
        if (index < 1) {
            throw new IllegalArgumentException("Invalid attribute Index.");
        }
        for (GXDLMSAttributeSettings it : this) {
            if (it.getIndex() == index) {
                return it;
            }
        }
        return null;
    }

    @Override
    public boolean add(GXDLMSAttributeSettings e) {
        return base.add(e);
    }

    @Override
    public void add(int index, GXDLMSAttributeSettings element) {
        base.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends GXDLMSAttributeSettings> c) {
        return base.addAll(c);
    }

    @Override
    public boolean addAll(int index,
            Collection<? extends GXDLMSAttributeSettings> c) {
        return base.addAll(index, c);
    }

    @Override
    public void clear() {
        base.clear();

    }

    @Override
    public boolean contains(Object o) {
        return base.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return base.containsAll(c);
    }

    @Override
    public GXDLMSAttributeSettings get(int index) {
        return base.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return base.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return base.isEmpty();
    }

    @Override
    public Iterator<GXDLMSAttributeSettings> iterator() {
        return base.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return base.lastIndexOf(o);
    }

    @Override
    public ListIterator<GXDLMSAttributeSettings> listIterator() {
        return base.listIterator();
    }

    @Override
    public ListIterator<GXDLMSAttributeSettings> listIterator(int index) {
        return base.listIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        return base.remove(o);
    }

    @Override
    public GXDLMSAttributeSettings remove(int index) {
        return base.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return base.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return base.retainAll(c);
    }

    @Override
    public GXDLMSAttributeSettings set(int index,
            GXDLMSAttributeSettings element) {
        return base.set(index, element);
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public List<GXDLMSAttributeSettings> subList(int fromIndex, int toIndex) {
        return base.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return base.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return base.toArray(a);
    }
}
/*
 * Copyright (C) 2007-2013 Crafter Software Corporation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.engine.model;

import freemarker.ext.util.ModelFactory;
import freemarker.template.*;
import org.craftercms.engine.freemarker.CrafterObjectWrapper;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Freemarker template model for Dom4j {@link org.dom4j.Node}s.
 *
 * @author Alfonso Vásquez
 */
public class Dom4jNodeModel implements TemplateNodeModel, TemplateSequenceModel, TemplateHashModel, AdapterTemplateModel,
        TemplateScalarModel {

    public static final ModelFactory FACTORY = new ModelFactory() {

        public TemplateModel create(Object object, ObjectWrapper wrapper) {
            return new Dom4jNodeModel((Node) object, wrapper);
        }

    };

    private Node node;
    private ObjectWrapper wrapper;

    public Dom4jNodeModel(Node node) {
        this.node = node;
        this.wrapper = new CrafterObjectWrapper();
    }

    public Dom4jNodeModel(Node node, ObjectWrapper wrapper) {
        this.node = node;
        this.wrapper = wrapper;
    }

    @Override
    public TemplateModel get(int index) throws TemplateModelException {
        return index == 0? this : null;
    }

    @Override
    public int size() throws TemplateModelException {
        return 1;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        Object result = node.selectObject(key);
        if (result != null) {
            if ((result instanceof Collection) && ((Collection) result).isEmpty()) {
                return null;
            } else {
                return wrapper.wrap(result);
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return !node.hasContent();
    }

    @Override
    public TemplateNodeModel getParentNode() {
        Element parent = node.getParent();
        if (parent != null) {
            return new Dom4jNodeModel(parent, wrapper);
        } else {
            return null;
        }
    }

    @Override
    public TemplateSequenceModel getChildNodes() throws TemplateModelException {
        if (node instanceof Branch) {
            List childNodes = ((Branch) node).content();
            if (childNodes != null) {
                return new SimpleSequence(childNodes, wrapper);
            } else {
                return null;
            }
        } else {
            return new SimpleSequence(Collections.emptyList(), wrapper);
        }
    }

    @Override
    public String getNodeName() {
        return node.getName();
    }

    @Override
    public String getNodeType() {
        return node.getNodeTypeName();
    }

    @Override
    public String getNodeNamespace() {
        String namespaceUri = null;

        if (node instanceof Element) {
            namespaceUri = ((Element) node).getNamespaceURI();
        } else if (node instanceof Attribute) {
            namespaceUri = ((Attribute) node).getNamespaceURI();
        }

        return namespaceUri;
    }

    @Override
    public Object getAdaptedObject(Class hint) {
        return node;
    }

    @Override
    public String getAsString() throws TemplateModelException {
        return node.getText();
    }

}
